package me.stivodavco.trimAbilities.inventoryHolders;

import me.stivodavco.trimAbilities.TrimAbilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class BarterDiscardInvHolder implements InventoryHolder {

    private final Inventory inventory;

    public BarterDiscardInvHolder() {
        inventory = TrimAbilities.getInstance().getServer().createInventory(this, 9, Component.text("Place items here to refund them back for gold.").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
