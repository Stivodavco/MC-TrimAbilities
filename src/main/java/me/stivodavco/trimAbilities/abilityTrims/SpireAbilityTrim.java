package me.stivodavco.trimAbilities.abilityTrims;

import com.destroystokyo.paper.ParticleBuilder;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.utilities.ParticleUtils;
import me.stivodavco.trimAbilities.utilities.RandomnessUtils;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpireAbilityTrim extends AbilityTrim {
    public final double RADIUS = 15;

    public SpireAbilityTrim(Player newOwner) {
        super(newOwner, 90, AbilityType.INSTANT, 0, 0, "Spire", "Levitation Shroud");
    }

    @Override
    public void enable() {}

    @Override
    public void customAbility() {

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
