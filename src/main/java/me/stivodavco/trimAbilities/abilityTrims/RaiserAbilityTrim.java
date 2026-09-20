package me.stivodavco.trimAbilities.abilityTrims;

import com.destroystokyo.paper.ParticleBuilder;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.utilities.ParticleUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;

public class RaiserAbilityTrim extends AbilityTrim {
    public static final int RADIUS = 6;
    public static final double KNOCKBACK_POWER = 2;
    public static final int DMG = 14;

    public RaiserAbilityTrim(Player newOwner) {
        super(newOwner, 120, AbilityType.INSTANT, 0, 0, "Raiser", "Ground Slam");
    }

    @Override
    public void enable() {}

    @Override
    public void customAbility() {
        for (Entity entity : getOwner().getNearbyEntities(RADIUS, RADIUS, RADIUS)) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.damage(DMG, getOwner());
                livingEntity.setVelocity(livingEntity.getVelocity().add(new Vector(0,KNOCKBACK_POWER,0)));
            }
        }

        ParticleBuilder particle = Particle.BLOCK.builder()
                .count(4)
                .data(getOwner().getLocation().subtract(0, 1, 0).getBlock().getType().createBlockData())
                .extra(0);

        ParticleUtils.cylinder(getOwner().getLocation(), RADIUS, 2, particle);

        getOwner().getWorld().playSound(getOwner().getLocation(), Sound.ITEM_MACE_SMASH_GROUND, 1, 0);
    }

    @Override
    public void internalDisable() {}

    @Override
    public void internalUsed() {}

    /* EVENTS */
    @EventHandler(ignoreCancelled = true)
    public void onEntityKnockback(EntityKnockbackEvent e) {
        if (e.getEntity() == getOwner()) {
            e.setCancelled(true);
        }
    }
}
