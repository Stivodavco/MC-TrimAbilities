package me.stivodavco.trimAbilities.abilityTrims;

import com.destroystokyo.paper.ParticleBuilder;
import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.utilities.ParticleUtils;
import me.stivodavco.trimAbilities.utilities.RandomnessUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class VexAbilityTrim extends AbilityTrim {
    public static final int SURVIVAL_CHANCE = 30;
    public static final int HEAL_HEALTH = 8;
    public static final int RADIUS = 10;
    public static final int EXTRA_DMG = 16;

    public VexAbilityTrim(Player newOwner) {
        super(newOwner, 45, AbilityType.INSTANT, 0, 0, "Vex", "Crushing Fangs");
    }

    @Override
    public void enable() {}

    private void spawnFangsAtLocation(Location location) {
        World world = location.getWorld();
        List<Location> locOffsets = List.of(
                new Location(world, 1,0,0),
                new Location(world, 1,0,-1),
                new Location(world, 1,0,1),
                new Location(world, 0,0,1),
                new Location(world, 0,0,-1),
                new Location(world, 0,0,0),
                new Location(world, -1,0,0),
                new Location(world, -1,0,1),
                new Location(world, -1,0,-1)
        );

        for (Location offset : locOffsets) {
            Location targetLoc = location.clone().add(offset);
            world.spawn(targetLoc, EvokerFangs.class);
        }
    }

    @Override
    public void customAbility() {
        for (Entity entity : getOwner().getNearbyEntities(RADIUS, RADIUS, RADIUS)) {
            if (entity instanceof LivingEntity) {
                spawnFangsAtLocation(entity.getLocation());
                ((LivingEntity) entity).damage(EXTRA_DMG);
            }
        }

        ParticleBuilder particle = new ParticleBuilder(Particle.END_ROD).extra(0);

        ParticleUtils.circle(getOwner().getLocation(), RADIUS, particle);
    }

    @Override
    public void internalDisable() {}

    @Override
    public void internalUsed() {}

    /* EVENTS */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (e.getEntity() == getOwner() && RandomnessUtils.chance(SURVIVAL_CHANCE)) {
            e.setCancelled(true);

            double maxHealth = getOwner().getAttribute(Attribute.MAX_HEALTH).getBaseValue();
            e.setReviveHealth(Math.clamp(HEAL_HEALTH, 1, maxHealth));

            ParticleBuilder particle = new ParticleBuilder(Particle.TOTEM_OF_UNDYING);
            ParticleUtils.cylinder(getOwner().getLocation(), 0.5, 2, particle);

            getOwner().getWorld().playSound(getOwner().getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
        }
    }
}
