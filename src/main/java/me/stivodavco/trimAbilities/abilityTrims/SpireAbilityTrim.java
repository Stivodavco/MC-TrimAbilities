package me.stivodavco.trimAbilities.abilityTrims;

import com.destroystokyo.paper.ParticleBuilder;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.utilities.ParticleUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class SpireAbilityTrim extends AbilityTrim {
    public final int RADIUS = 15;
    public final PotionEffect LEVITATION = new PotionEffect(PotionEffectType.LEVITATION, 10 * 20, 1);

    public SpireAbilityTrim(Player newOwner) {
        super(newOwner, TrimPattern.SPIRE, AbilityType.INSTANT);
    }

    @Override
    public void enable() {}

    @Override
    public void customAbility() {
        for (Entity entity : getOwner().getNearbyEntities(RADIUS, RADIUS, RADIUS)) {
            if (entity instanceof Player player) {
                player.addPotionEffect(LEVITATION);
            }
        }

        ParticleBuilder particle = Particle.END_ROD.builder().extra(0);

        ParticleUtils.cylinder(getOwner().getLocation(), RADIUS, 3, particle);
    }

    @Override
    public void internalDisable() {}

    @Override
    public void internalUsed() {}

    /* EVENTS */
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() == getOwner() && e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);

            ParticleBuilder particle = Particle.POOF.builder().extra(0.2).count(15).location(getOwner().getLocation()).receivers(50);
            particle.spawn();

            getOwner().getLocation().getWorld().playSound(getOwner().getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1, 2);
            getOwner().setVelocity(new Vector(0, 0.6, 0));
        }
    }
}
