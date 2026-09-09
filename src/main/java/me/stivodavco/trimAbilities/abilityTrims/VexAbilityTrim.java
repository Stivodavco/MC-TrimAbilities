package me.stivodavco.trimAbilities.abilityTrims;

import com.destroystokyo.paper.ParticleBuilder;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.utilities.ParticleUtils;
import me.stivodavco.trimAbilities.utilities.RandomnessUtils;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

public class VexAbilityTrim extends AbilityTrim {
    public static final int SURVIVAL_CHANCE = 15;

    public VexAbilityTrim(Player newOwner) {
        super(newOwner, 45, AbilityType.INSTANT, 0, 0, "Vex", "Crushing Fangs");
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
}
