package me.stivodavco.trimAbilities.abilityTrims;

import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ShaperAbilityTrim extends AbilityTrim {
    public static final PotionEffect HASTE = new PotionEffect(PotionEffectType.HASTE, -1, 255, true, false);

    public ShaperAbilityTrim(Player newOwner) {
        super(newOwner, 120, AbilityType.LIMITED_TIME, 3, 0, "Shaper", "Short Flight");
    }

    @Override
    public void enable() {
        getOwner().addPotionEffect(HASTE);
    }

    @Override
    public void customAbility() {

    }

    @Override
    public void internalDisable() {
        getOwner().removePotionEffect(PotionEffectType.HASTE);
    }

    @Override
    public void internalUsed() {

    }
}
