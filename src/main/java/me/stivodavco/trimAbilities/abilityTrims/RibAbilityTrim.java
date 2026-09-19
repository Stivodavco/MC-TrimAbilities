package me.stivodavco.trimAbilities.abilityTrims;

import com.destroystokyo.paper.event.block.AnvilDamagedEvent;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class RibAbilityTrim extends AbilityTrim {
    public RibAbilityTrim(Player newOwner) {
        super(newOwner, 0, AbilityType.NONE, 0, 0, "Rib", "Tough as Bricks");
    }

    @Override
    public void enable() {}

    @Override
    public void customAbility() {}

    @Override
    public void internalDisable() {}

    @Override
    public void internalUsed() {}

    /* EVENTS */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemDamage(PlayerItemDamageEvent e) {
        if (e.getPlayer() == getOwner()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onAnvilDamagedEvent(AnvilDamagedEvent e) {
        if (e.getViewers().contains(getOwner())) {
            e.setCancelled(true);
        }
    }
}
