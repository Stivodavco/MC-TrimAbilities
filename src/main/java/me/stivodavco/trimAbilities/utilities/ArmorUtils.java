package me.stivodavco.trimAbilities.utilities;

import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.SentryAbilityTrim;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static org.bukkit.inventory.meta.trim.TrimPattern.SENTRY;

public class ArmorUtils {
    public static ArmorTrim getTrim(ItemStack item) {
        Optional<ArmorTrim> targetPattern = Optional.ofNullable(item)
                .map(ItemStack::getItemMeta)
                .map(itemMeta -> (ArmorMeta) itemMeta)
                .map(ArmorMeta::getTrim);

        return targetPattern.orElse(null);
    }

    public static AbilityTrim getAbilityTrim(TrimPattern pattern) {
        return (pattern.equals(SENTRY) ? new SentryAbilityTrim(null) : null);
    }

    public static AbilityTrim getActiveTrim(@NotNull Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        TrimPattern targetPattern = null;
        if (helmet != null) {
            ArmorTrim trim = ArmorUtils.getTrim(helmet);
            if (trim != null) {
                targetPattern = trim.getPattern();
            }
        }

        if (targetPattern != null) {
            boolean equal = true;

            for (ItemStack armor : player.getInventory().getArmorContents()) { // check if all armor has the same trim, if not, set equal to false
                if (armor != null) {
                    if (ArmorUtils.getTrim(armor).getPattern() != targetPattern) {
                        equal = false;
                    }
                } else {
                    equal = false;
                }
            }

            if (equal) { // if all armor patterns are equal
                return ArmorUtils.getAbilityTrim(targetPattern);
            }
        }

        return null;
    }
}
