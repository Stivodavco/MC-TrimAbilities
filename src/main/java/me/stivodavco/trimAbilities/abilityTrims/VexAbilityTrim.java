package me.stivodavco.trimAbilities.abilityTrims;

import com.destroystokyo.paper.ParticleBuilder;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.utilities.ParticleUtils;
import me.stivodavco.trimAbilities.utilities.RandomnessUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

public class VexAbilityTrim extends AbilityTrim {
    public static final int SURVIVAL_CHANCE = 30;
    public static final int HEAL_HEALTH = 8;

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

    /* EVENTS */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (e.getEntity() == getOwner() && RandomnessUtils.chance(SURVIVAL_CHANCE)) {
            e.setCancelled(true);

            double maxHealth = getOwner().getAttribute(Attribute.MAX_HEALTH).getBaseValue();
            e.setReviveHealth(Math.clamp(HEAL_HEALTH, 1, maxHealth));

            ParticleBuilder particle = new ParticleBuilder(Particle.TOTEM_OF_UNDYING);
            ParticleUtils.cylinder(getOwner().getLocation(), 0.5, 2, particle);

            getOwner().getWorld().playSound(getOwner().getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
        }
    }
}
