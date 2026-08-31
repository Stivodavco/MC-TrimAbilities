package me.stivodavco.trimAbilities.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.utilities.ArmorUtils;
import me.stivodavco.trimAbilities.utilities.Storage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

public class AbilityListener implements Listener {
    // GENERAL (FOR EVERY TRIM)
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && e.getPlayer().isSneaking()) { //  || e.getAction().equals(Action.RIGHT_CLICK_BLOCK) )
            AbilityTrim trim = Storage.getAbilityTrim(e.getPlayer());
            if (trim != null && trim.isAbilityReady()) {
                trim.ability();

                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        ArmorUtils.equipTrimIfPresent(e.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (Storage.getAbilityTrim(e.getPlayer()) != null) {
            Storage.setActiveTrim(e.getPlayer(), null);
        }
    }

    @EventHandler
    public void onArmorChange (PlayerArmorChangeEvent e) {
        ArmorUtils.equipTrimIfPresent(e.getPlayer());
    }
}
