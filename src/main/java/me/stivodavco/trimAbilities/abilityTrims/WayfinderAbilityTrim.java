package me.stivodavco.trimAbilities.abilityTrims;

import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

public class WayfinderAbilityTrim extends AbilityTrim {
    public WayfinderAbilityTrim(Player newOwner) {
        super(newOwner, 960, AbilityType.INSTANT, 0, 0, "Wayfinder", "Lodewarp");
    }

    @Override
    public void enable() {}

    @Override
    public void customAbility() {
        ItemStack heldItem = getOwner().getInventory().getItemInMainHand();
        if (heldItem.getType() == Material.COMPASS) {
            if (heldItem.getItemMeta() instanceof CompassMeta meta) {
                Location lodestone = meta.getLodestone();
                if (lodestone != null) {
                    getOwner().teleportAsync(lodestone);
                }
            }
        }
    }

    @Override
    public void internalDisable() {}

    @Override
    public void internalUsed() {}
}
