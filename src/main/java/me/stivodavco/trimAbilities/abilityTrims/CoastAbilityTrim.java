package me.stivodavco.trimAbilities.abilityTrims;

import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class CoastAbilityTrim extends AbilityTrim {
    public static final int LASER_RANGE = 10;
    public static final PotionEffect WATER_BREATHING = new PotionEffect(PotionEffectType.WATER_BREATHING, -1, 255, true, false);
    public static final PotionEffect DOLPHINS_GRACE = new PotionEffect(PotionEffectType.DOLPHINS_GRACE, -1, 2, true, false);

    public CoastAbilityTrim(Player newOwner) {
        super(newOwner, 120, AbilityType.LIMITED_TIME, 8, 0, "Coast", "Guardian Laser");
    }

    @Override
    public void enable() {
        getOwner().addPotionEffects(List.of(WATER_BREATHING, DOLPHINS_GRACE));
    }

    @Override
    public void customAbility() {

    }

    @Override
    public void internalDisable() {
        getOwner().removePotionEffect(PotionEffectType.WATER_BREATHING);
        getOwner().removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
    }

    @Override
    public void internalUsed() {

    }
}
