package me.stivodavco.trimAbilities.abilityTrims;

import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.enums.AbilityActivationType;
import org.bukkit.entity.Player;

public class WildAbilityTrim extends AbilityTrim {

    public WildAbilityTrim(Player newOwner) {
        super(newOwner, 180, AbilityActivationType.INSTANT, 0, "Wild", "Sensitive Shriek");
    }

    @Override
    public void enable() {}

    @Override
    public void customAbility() {

    }

    @Override
    public void internalDisable() {

    }
}
