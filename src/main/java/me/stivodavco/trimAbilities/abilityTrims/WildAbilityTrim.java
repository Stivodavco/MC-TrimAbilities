package me.stivodavco.trimAbilities.abilityTrims;

import com.destroystokyo.paper.ParticleBuilder;
import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.utilities.ParticleUtils;
import me.stivodavco.trimAbilities.utilities.RandomnessUtils;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class WildAbilityTrim extends AbilityTrim {
    public final double RADIUS = 5;
    public final int POISON_CHANCE = 30;

    public static final PotionEffect POISON = new PotionEffect(PotionEffectType.POISON, 5 * 20, 2);

    private final List<Player> vinedPlrs = new ArrayList<>();

    private BukkitTask runnable;

    public WildAbilityTrim(Player newOwner) {
        super(newOwner, 190, AbilityType.LIMITED_TIME, 5, 0, "Wild", "Vines");
    }

    @Override
    public void enable() {}

    @Override
    public void customAbility() {
        ParticleBuilder particle = Particle.DUST.builder().data(new Particle.DustOptions(Color.GREEN, 2));
        ParticleUtils.cylinder(getOwner().getLocation(), RADIUS, 2, particle);

        vinedPlrs.clear();
        for (Entity entity : getOwner().getNearbyEntities(RADIUS, RADIUS, RADIUS)) {
            if (entity instanceof Player player) {
                vinedPlrs.add(player);

                runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!active) {
                            runnable = null;
                            cancel();
                        }

                        ParticleUtils.cylinder(player.getLocation(), 0.5, 1, particle);
                    }
                }.runTaskTimer(TrimAbilities.getInstance(), 1, 10);
            }
        }
    }

    @Override
    public void internalDisable() {
        if (runnable != null) {
            runnable.cancel();
        }
    }

    @Override
    public void internalUsed() {}

    /* EVENTS */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent e) {
        if (vinedPlrs.contains(e.getPlayer()) && active) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() == getOwner()
                && RandomnessUtils.chance(POISON_CHANCE)
                && e.getEntity() instanceof LivingEntity livingEntity
                && !livingEntity.hasPotionEffect(PotionEffectType.POISON)
        ) {
            livingEntity.addPotionEffect(POISON);
        }
    }
}
