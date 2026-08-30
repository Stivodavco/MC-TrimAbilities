package me.stivodavco.trimAbilities.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import io.papermc.paper.persistence.PersistentDataContainerView;
import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.abilityTrims.SentryAbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.SnoutAbilityTrim;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.inventoryHolders.BarterDiscardInvHolder;
import me.stivodavco.trimAbilities.utilities.ArmorUtils;
import me.stivodavco.trimAbilities.utilities.RandomnessUtils;
import me.stivodavco.trimAbilities.utilities.Storage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PiglinBarterEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.List;

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
