package me.stivodavco.trimAbilities.abilityTrims;

import com.destroystokyo.paper.ParticleBuilder;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.utilities.ParticleUtils;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class DuneAbilityTrim extends AbilityTrim {
    public static final double RADIUS = 10;

    public static final PotionEffect SATURATION = new PotionEffect(PotionEffectType.SATURATION, -1, 255, false, false);

    // potion effects (in ticks)
    public static final int BLINDNESS_DURATION = 100;

    public static final int SLOWNESS_DURATION = 100;
    public static final int SLOWNESS_AMPLIF = 2;

    public static final int SPEED_DURATION = 100;
    public static final int SPEED_AMPLIF = 2;

    public DuneAbilityTrim(Player newOwner) {
        super(newOwner, 85, AbilityType.LIMITED_TIME, 5, 0, "Dune", "Sandstorm");
    }

    @Override
    public void enable() {
        getOwner().addPotionEffect(SATURATION);
    }

    @Override
    public void customAbility() {
        List<Entity> nearby =  getOwner().getNearbyEntities(RADIUS, RADIUS, RADIUS);

        for (Entity entity : nearby) {
            if (entity instanceof Player player) {
                List<PotionEffect> effects = new ArrayList<>();
                effects.add(new PotionEffect(PotionEffectType.BLINDNESS, BLINDNESS_DURATION, 1, false));
                effects.add(new PotionEffect(PotionEffectType.SLOWNESS, SLOWNESS_DURATION, SLOWNESS_AMPLIF, false));

                player.addPotionEffects(effects);
            }
        }

        ParticleBuilder particle = new ParticleBuilder(Particle.DUST_COLOR_TRANSITION)
                .colorTransition(Color.fromRGB(189, 169, 57), Color.fromRGB(239, 245, 76), 4)
                .count(3);

        ParticleUtils.sphere(getOwner().getLocation(), RADIUS, particle);

        PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, SPEED_DURATION, SPEED_AMPLIF);

        getOwner().addPotionEffect(speed);
    }

    @Override
    public void internalDisable() {
        getOwner().removePotionEffect(PotionEffectType.SATURATION);
    }

    @Override
    public void internalUsed() {}
}
