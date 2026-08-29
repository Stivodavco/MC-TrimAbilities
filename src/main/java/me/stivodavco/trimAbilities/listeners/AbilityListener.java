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
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
                NamespacedKey stackKey = new NamespacedKey(TrimAbilities.getInstance(), "barter_stack_size");
                pdc.set(stackKey, PersistentDataType.INTEGER, item.getAmount());
            });
            ItemMeta meta = item.getItemMeta();
            meta.lore(List.of(
                    Component.text("Bartered Item ").color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD),
                    Component.text("Refundable with Snout Ability Trim").color(NamedTextColor.GRAY),
                    Component.text("Accept [Left Click]").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false),
                    Component.text("Refund [Shift + Left Click]").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)
            ));
            item.setItemMeta(meta);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) { // SNOUT
        Player player = TrimAbilities.getInstance().getServer().getPlayer(e.getWhoClicked().getUniqueId());
        ItemStack item = e.getCurrentItem();

        if (item != null && player != null && Storage.getAbilityTrim(player) instanceof SnoutAbilityTrim) {
            TrimAbilities.getInstance().getLogger().info("Everything valid");
            List<InventoryAction> illegalActions = List.of(InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_SOME, InventoryAction.PICKUP_HALF, InventoryAction.PLACE_ONE, InventoryAction.PLACE_SOME, InventoryAction.PLACE_SOME_INTO_BUNDLE, InventoryAction.PICKUP_SOME_INTO_BUNDLE);

            final ClickType ACCEPT_CLICK = ClickType.LEFT;
            final ClickType REFUND_CLICK = ClickType.SHIFT_LEFT;

            NamespacedKey key = NamespacedKey.fromString("barter_stack_size", TrimAbilities.getInstance());
            assert key != null;

            PersistentDataContainerView pdc = item.getPersistentDataContainer();

            if (pdc.has(key)) {
                if (illegalActions.contains(e.getAction())) {
                    e.setCancelled(true);
                } else if (e.getClick().equals(ACCEPT_CLICK)) {
                    e.setCancelled(true);

                    item.editPersistentDataContainer(editpdc -> {
                        editpdc.remove(key);
                    });
                    ItemMeta meta = item.getItemMeta();
                    meta.lore(null);
                    item.setItemMeta(meta);
                } else if (e.getClick().equals(REFUND_CLICK)) {
                    e.setCancelled(true);

                    e.setCurrentItem(null);
                    player.give(ItemStack.of(Material.GOLD_INGOT).asQuantity(Math.floorDiv(item.getAmount(), pdc.get(key, PersistentDataType.INTEGER))));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) { // SNOUT
        ItemStack item = e.getItemDrop().getItemStack();

        NamespacedKey key = NamespacedKey.fromString("barter_stack_size", TrimAbilities.getInstance());
        assert key != null;

        PersistentDataContainerView pdc = item.getPersistentDataContainer();

        if (pdc.has(key)) {
            e.setCancelled(true);
        }
    }
}
