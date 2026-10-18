package me.stivodavco.trimAbilities.abilityTrims;

import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class FlowAbilityTrim extends AbilityTrim {
    public static final double DASH_POWER = 1;
    public static final PotionEffect JUMP_BOOST = new PotionEffect(PotionEffectType.JUMP_BOOST, 10, 4, true, false);

    public FlowAbilityTrim(Player newOwner) {
        super(newOwner, TrimPattern.FLOW, AbilityType.INSTANT);
    }

    @Override
    public void enable() {}

    @Override
    public void customAbility() {
        Vector lookVector = getOwner().getLocation().getDirection().normalize();
        getOwner().setVelocity(getOwner().getVelocity().add(lookVector.multiply(DASH_POWER)));
    }

    @Override
    public void internalDisable() {

    }

    @Override
    public void internalUsed() {

    }

    /* EVENTS */
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
        if (e.isSneaking() && e.getPlayer() == getOwner() && !getOwner().hasPotionEffect(PotionEffectType.JUMP_BOOST)) {
            getOwner().addPotionEffect(JUMP_BOOST);
        }
    }
}
