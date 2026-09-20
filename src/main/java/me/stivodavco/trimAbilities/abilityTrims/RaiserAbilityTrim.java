package me.stivodavco.trimAbilities.abilityTrims;

import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import io.papermc.paper.event.entity.EntityKnockbackEvent;

public class RaiserAbilityTrim extends AbilityTrim {

    public RaiserAbilityTrim(Player newOwner) {
        super(newOwner, 120, AbilityType.INSTANT, 0, 0, "Raiser", "Ground Slam");
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

    /* EVENTS */
    @EventHandler(ignoreCancelled = true)
    public void onEntityKnockback(EntityKnockbackEvent e) {
        if (e.getEntity() == getOwner()) {
            e.setCancelled(true);
        }
    }
}
