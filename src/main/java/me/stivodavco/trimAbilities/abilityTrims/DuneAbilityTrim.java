package me.stivodavco.trimAbilities.abilityTrims;

import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.enums.AbilityActivationType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class DuneAbilityTrim extends AbilityTrim {
    public final int RADIUS = 10;

    // potion effects (in ticks)
    public final int BLINDNESS_DURATION = 100;

    public final int SLOWNESS_DURATION = 100;
    public final int SLOWNESS_AMPLIF = 2;

    public final int SPEED_DURATION = 100;
    public final int SPEED_AMPLIF = 2;

    public DuneAbilityTrim(Player newOwner) {
        super(newOwner, 85, AbilityActivationType.LIMITED_TIME, 5, "Dune", "Sandstorm");
    }

    @Override
    public void ability() {
        if (getOwner() == null) {
            throw new Error("Attempt to use the .ability() method on an AbilityTrim without an owner!");
        }
        resetActivatedTime();
        resetCooldown();

        active = true;

        List<Entity> nearby =  getOwner().getNearbyEntities(RADIUS, RADIUS, RADIUS);

        for (Entity entity : nearby) {
            if (entity instanceof Player player) {
                List<PotionEffect> effects = new ArrayList<>();
                effects.add(new PotionEffect(PotionEffectType.BLINDNESS, BLINDNESS_DURATION, 1, false));
                effects.add(new PotionEffect(PotionEffectType.SLOWNESS, SLOWNESS_DURATION, SLOWNESS_AMPLIF, false));

                player.addPotionEffects(effects);
            }
        }

        PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, SPEED_DURATION, SPEED_AMPLIF, false);

        getOwner().addPotionEffect(speed);
    }

    @Override
    public void disable() {

    }
}
