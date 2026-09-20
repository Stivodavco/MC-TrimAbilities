package me.stivodavco.trimAbilities.abilityTrims;

import com.destroystokyo.paper.ParticleBuilder;
import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.utilities.ParticleUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class TideAbilityTrim extends AbilityTrim {
    public static final int KNOCKBACK_RADIUS = 2;
    public static final double KNOCKBACK_POWER = 2;
    public static final PotionEffect REGEN = new PotionEffect(PotionEffectType.REGENERATION, 40, 1, true, true);

    private BukkitTask passiveRunnable;
    private BukkitTask abilityRunnable;

    public TideAbilityTrim(Player newOwner) {
        super(newOwner, 120, AbilityType.LIMITED_TIME, 6, 0, "Tide", "Water Tornado");
    }

    @Override
    public void enable() {
        passiveRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                boolean inWater = getOwner().getLocation().getBlock().getType() == Material.WATER;
                boolean inRain = getOwner().getWorld().hasStorm();

                if ( (inWater || inRain) && !getOwner().hasPotionEffect(PotionEffectType.REGENERATION) ) {
                    getOwner().addPotionEffect(REGEN);
                }
            }
        }.runTaskTimer(TrimAbilities.getInstance(), 0, 10);
    }

    @Override
    public void customAbility() {
        getOwner().setGravity(false);
        getOwner().setAllowFlight(true);
        abilityRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!active) {
                    getOwner().setGravity(true);
                    getOwner().setAllowFlight(false);
                    getOwner().setFlying(false);
                    abilityRunnable = null;
                    cancel();
                }

                for (Entity entity : getOwner().getNearbyEntities(KNOCKBACK_RADIUS, KNOCKBACK_RADIUS, KNOCKBACK_RADIUS)) {
                    if (entity instanceof LivingEntity livingEntity) {
                        Vector knockbackVector = livingEntity.getVelocity().normalize().multiply(-KNOCKBACK_POWER);
                        livingEntity.setVelocity(knockbackVector);

                        getOwner().getWorld().playSound(getOwner().getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1, 0);
                    }
                }

                getOwner().getWorld().playSound(getOwner().getLocation(), Sound.ENTITY_BREEZE_IDLE_GROUND, 1, 0);

                ParticleBuilder particle = Particle.DUST.builder()
                        .data(new Particle.DustOptions(Color.AQUA, 4))
                        .extra(0.2);

                ParticleUtils.sphere(getOwner().getLocation(), KNOCKBACK_RADIUS, particle);
            }
        }.runTaskTimer(TrimAbilities.getInstance(), 0, 10);
    }

    @Override
    public void internalDisable() {
        getOwner().setGravity(true);
        getOwner().setAllowFlight(false);
        getOwner().setFlying(false);
        if (passiveRunnable != null) {
            passiveRunnable.cancel();
        }
        if (abilityRunnable != null) {
            abilityRunnable.cancel();
        }
    }

    @Override
    public void internalUsed() {}

    /* EVENTS */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent e) {
        if (active && e.getPlayer() == getOwner()) {
            e.setCancelled(true);
        }
    }
}
