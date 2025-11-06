package me.stivodavco.trimAbilities.abilityTrims;

import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.enums.AbilityActivationType;
import org.bukkit.entity.Player;

public class DuneAbilityTrim extends AbilityTrim {
    public DuneAbilityTrim(Player newOwner) {
        super(newOwner, 85, AbilityActivationType.LIMITED_TIME, 5, "Dune", "Sandstorm");
    }

    @Override
    public void ability() {

    }

    @Override
    public void disable() {

    }
}
