package me.stivodavco.trimAbilities.abilityTrims;

import com.destroystokyo.paper.ParticleBuilder;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.utilities.ParticleUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HostAbilityTrim extends AbilityTrim {
    public static final int RADIUS = 8;
    public static final int EFFECT_DURATION = 8;

    public static final PotionEffect WEAKNESS = new PotionEffect(PotionEffectType.WEAKNESS, EFFECT_DURATION * 20, 0);

    public HostAbilityTrim(Player newOwner) {
        super(newOwner, TrimPattern.HOST, AbilityType.LIMITED_TIME);
    }

    @Override
    public void enable() {}

    @Override
    public void customAbility() {
        int amplifier = -1;

        ParticleBuilder particle = Particle.SCRAPE.builder().count(2).extra(0);

        for (Entity entity : getOwner().getNearbyEntities(RADIUS, RADIUS, RADIUS)) {
            if (entity instanceof Player player) {
                amplifier ++;
                player.addPotionEffect(WEAKNESS);

                ParticleUtils.beam(getOwner().getLocation().add(0, 1, 0), player.getLocation().add(0, 1, 0), particle);
            }
        }

        if (amplifier >= 0) {
            PotionEffect strength = new PotionEffect(PotionEffectType.STRENGTH, EFFECT_DURATION * 20, amplifier);
            getOwner().addPotionEffect(strength);
        }

        ParticleUtils.sphere(getOwner().getLocation(), RADIUS, particle);
    }

    @Override
    public void internalDisable() {}

    @Override
    public void internalUsed() {}


    /* EVENTS */
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() == getOwner() && e.getEntity() instanceof Player victim) {
            getOwner().heal(e.getFinalDamage() / 2);

            ParticleBuilder particle = Particle.ELECTRIC_SPARK.builder().extra(0);
            ParticleUtils.beam(getOwner().getLocation().add(0, 1, 0), victim.getLocation().add(0, 1, 0), particle);

            getOwner().getWorld().playSound(getOwner().getLocation(), Sound.ENTITY_GUARDIAN_HURT, 1, 2);
        }
    }
}
