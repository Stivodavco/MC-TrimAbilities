package me.stivodavco.trimAbilities.abilityTrims;

import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import org.bukkit.entity.Player;

public class SilenceAbilityTrim extends AbilityTrim {
    public SilenceAbilityTrim(Player newOwner) {
        super(newOwner, 0, AbilityType.NONE, 0, 0, "Silent", "Silent & Sneaky");
    }

    @Override
    public void enable() {
    }

    @Override
    public void customAbility() {

    }

    @Override
    public void internalDisable() {

    }

    @Override
    public void internalUsed() {

    }
}
