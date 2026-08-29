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
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

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

        // SNOUT PIGLIN BARTERING
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            ItemStack heldItem = e.getItem();
            if (heldItem != null && heldItem.getType() == Material.GOLD_INGOT) {
                Inventory barterInv = new BarterDiscardInvHolder().getInventory();
                e.getPlayer().openInventory(barterInv);
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


    // SPECIFIC
    @EventHandler
    public void onDamage(EntityDamageEvent e) { // SENTRY
        if (e.getEntity() instanceof Player player) {
            if (Storage.getAbilityTrim(player) instanceof SentryAbilityTrim) {
                if (e.getDamageSource().getDirectEntity() instanceof Arrow && RandomnessUtils.chance(80)) {
                    player.getWorld().playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1, 1);
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) { // SNOUT
        if (e.getTarget() instanceof Player player && e.getEntity() instanceof PiglinAbstract) {
            AbilityTrim abilityTrim = Storage.getAbilityTrim(player);
            if (abilityTrim instanceof SnoutAbilityTrim) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPiglinBarter(PiglinBarterEvent e) { // SNOUT
        for (ItemStack item : e.getOutcome()) {
            item.editPersistentDataContainer(pdc -> {
                NamespacedKey stackKey = new NamespacedKey(TrimAbilities.getInstance(), "barter_stack_amount");
                pdc.set(stackKey, PersistentDataType.INTEGER, item.getAmount());
            });
            ItemMeta meta = item.getItemMeta();
            item.lore(List.of(Component.text("Bartered Item")));
            item.setItemMeta(meta);
            TrimAbilities.getInstance().getLogger().info(item.toString());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof BarterDiscardInvHolder && (e.getAction().equals(InventoryAction.PLACE_SOME) || e.getAction().equals(InventoryAction.PLACE_ALL) || e.getAction().equals(InventoryAction.PLACE_ONE)) ) {
            TrimAbilities.getInstance().getLogger().info("Passed first");
            ItemStack droppedItem = e.getCursor();
            if (droppedItem != null) {
                TrimAbilities.getInstance().getLogger().info("Valid dropped item");
                PersistentDataContainerView pdc = droppedItem.getPersistentDataContainer();

                NamespacedKey key = NamespacedKey.fromString("barter_stack_amount", TrimAbilities.getInstance());
                if (key != null) {
                    TrimAbilities.getInstance().getLogger().info("Key: " + key.asString());
                    if (pdc.has(key)) {
                        TrimAbilities.getInstance().getLogger().info("Has key!");

                        if (pdc.get(key, PersistentDataType.INTEGER) < droppedItem.getAmount()) {
                            TrimAbilities.getInstance().getLogger().info("Cancelling, less than amount required");
                            e.setCancelled(true);
                        } else {
                            TrimAbilities.getInstance().getLogger().info("Give gold!");
                            ItemStack gold = new ItemStack(Material.GOLD_INGOT);
                            e.setCurrentItem(gold);
                            //e.getInventory().setItem(e.getSlot(), gold);
                            TrimAbilities.getInstance().getLogger().info(String.valueOf(e.getSlot()));
                        }
                    }
                };
            }
        }
    }
}
