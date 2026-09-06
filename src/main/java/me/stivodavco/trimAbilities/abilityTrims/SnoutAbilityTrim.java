package me.stivodavco.trimAbilities.abilityTrims;

import io.papermc.paper.persistence.PersistentDataContainerView;
import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.utilities.Storage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PiglinBarterEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class SnoutAbilityTrim extends AbilityTrim {
    private List<Entity> spawnedPiglins = new ArrayList<>();
    private BukkitTask task;

    public static TrimPattern PATTERN = null;

    public SnoutAbilityTrim(Player newOwner) {
        super(newOwner, 120, AbilityType.LIMITED_TIME, 20, 0, "Snout", "Call for help");
    }

    public void despawnPiglins() {
        for (Entity piglin : spawnedPiglins) {
            Particle.CLOUD.builder().
                    location(piglin.getLocation()).
                    receivers(20).
                    count(1).
                    spawn();
            piglin.remove();
        }
        spawnedPiglins.clear();
    }

    @Override
    public void enable() {}

    @Override
    public void customAbility() {
        Location ownerLoc = getOwner().getLocation();

        PiglinBrute brute = getOwner().getWorld().spawn(ownerLoc, PiglinBrute.class);
        brute.setImmuneToZombification(true);
        spawnedPiglins.add(brute);

        for (int i = 1; i <= 3; i++) {
            Piglin piglin = getOwner().getWorld().spawn(ownerLoc, Piglin.class);
            piglin.setImmuneToZombification(true);
            spawnedPiglins.add(piglin);
        }

        task = new BukkitRunnable() {
            @Override
            public void run() {
                despawnPiglins();
            }
        }.runTaskLater(TrimAbilities.getInstance(), getActivationTime() * 20L);
    }

    @Override
    public void internalDisable() {
        if (task != null) {
            task.cancel();
            despawnPiglins();
        }

        for (ItemStack item : getOwner().getInventory().getContents()) {
            if (item != null && item.getPersistentDataContainer().has(key)) {
                clearItem(item);
            }
        }
    }

    @Override
    public void internalUsed() {}

    /* EVENTS */
    public final NamespacedKey key = NamespacedKey.fromString("barter_stack_size", TrimAbilities.getInstance());

    // Helper functions
    public void clearItem(ItemStack item) {
        item.editPersistentDataContainer(pdc -> {
            pdc.remove(key);
        });
        ItemMeta meta = item.getItemMeta();
        meta.lore(null);
        item.setItemMeta(meta);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getTarget() instanceof Player player && e.getEntity() instanceof PiglinAbstract) {
            if (Storage.getAbilityTrim(player) instanceof SnoutAbilityTrim) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPiglinBarter(PiglinBarterEvent e) {
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

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent e) { // SNOUT
        Player player = TrimAbilities.getInstance().getServer().getPlayer(e.getWhoClicked().getUniqueId());
        ItemStack item = e.getCurrentItem();

        if (item != null && player != null && Storage.getAbilityTrim(player) instanceof SnoutAbilityTrim) {
            List<InventoryAction> illegalActions = List.of(InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_SOME, InventoryAction.PICKUP_HALF, InventoryAction.PLACE_ONE, InventoryAction.PLACE_SOME, InventoryAction.PLACE_SOME_INTO_BUNDLE, InventoryAction.PICKUP_SOME_INTO_BUNDLE);

            final ClickType ACCEPT_CLICK = ClickType.LEFT;
            final ClickType REFUND_CLICK = ClickType.SHIFT_LEFT;

            PersistentDataContainerView pdc = item.getPersistentDataContainer();

            if (pdc.has(key)) {
                if (illegalActions.contains(e.getAction())) {
                    e.setCancelled(true);
                } else if (e.getClick().equals(ACCEPT_CLICK)) {
                    e.setCancelled(true);
                    clearItem(item);
                } else if (e.getClick().equals(REFUND_CLICK)) {
                    e.setCancelled(true);

                    e.setCurrentItem(null);
                    player.give(ItemStack.of(Material.GOLD_INGOT).asQuantity(Math.floorDiv(item.getAmount(), pdc.get(key, PersistentDataType.INTEGER))));
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent e) { // SNOUT
        ItemStack item = e.getItemDrop().getItemStack();

        PersistentDataContainerView pdc = item.getPersistentDataContainer();

        if (pdc.has(key)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityPickupItem(EntityPickupItemEvent e) {
        ItemStack item = e.getItem().getItemStack();
        Player player = TrimAbilities.getInstance().getServer().getPlayer(e.getEntity().getUniqueId());

        if (item.getPersistentDataContainer().has(key)) {
            if (player != null && !(Storage.getAbilityTrim(player) instanceof SnoutAbilityTrim)) {
                clearItem(item);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryPickupItem(InventoryPickupItemEvent e) {
        ItemStack item = e.getItem().getItemStack();

        if (item.getPersistentDataContainer().has(key)) {
            clearItem(item);
        }
    }
}
