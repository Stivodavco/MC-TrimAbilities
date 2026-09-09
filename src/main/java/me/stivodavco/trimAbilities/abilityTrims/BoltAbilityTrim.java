package me.stivodavco.trimAbilities.abilityTrims;

import com.destroystokyo.paper.ParticleBuilder;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.utilities.ParticleUtils;
import me.stivodavco.trimAbilities.utilities.RandomnessUtils;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoltAbilityTrim extends AbilityTrim {
    public final double RADIUS = 10;
    public static final int LIGHTNING_CHANCE = 10;

    public BoltAbilityTrim(Player newOwner) {
        super(newOwner, 120, AbilityType.INSTANT, 0, 0, "Bolt", "Lightning Strike");
    }

    @Override
    public void enable() {}

    @Override
    public void customAbility() {
        List<Entity> nearby =  getOwner().getNearbyEntities(RADIUS, RADIUS, RADIUS);

        for (Entity entity : nearby) {
            entity.getWorld().spawn(entity.getLocation(), LightningStrike.class);
        }

        ParticleBuilder particle = new ParticleBuilder(Particle.GLOW_SQUID_INK).extra(0);

        ParticleUtils.circle(getOwner().getLocation(), RADIUS, particle);
    }

    @Override
    public void internalDisable() {}

    @Override
    public void internalUsed() {}

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() == getOwner() && RandomnessUtils.chance(LIGHTNING_CHANCE)) {
            e.getEntity().getWorld().spawn(e.getEntity().getLocation(), LightningStrike.class);
        }
    }
}
