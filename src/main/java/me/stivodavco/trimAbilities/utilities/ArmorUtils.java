package me.stivodavco.trimAbilities.utilities;

import me.stivodavco.trimAbilities.abilityTrims.DuneAbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.SnoutAbilityTrim;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.SentryAbilityTrim;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static org.bukkit.inventory.meta.trim.TrimPattern.*;

public class ArmorUtils {
    public static ArmorTrim getTrim(ItemStack item) {
        Optional<ArmorTrim> targetPattern = Optional.ofNullable(item)
                .map(ItemStack::getItemMeta)
                .map(itemMeta -> (ArmorMeta) itemMeta)
                .map(ArmorMeta::getTrim);

        return targetPattern.orElse(null);
    }

    public static AbilityTrim getAbilityTrim(TrimPattern pattern) {
        return (pattern.equals(SENTRY) ? new SentryAbilityTrim(null)
                : pattern.equals(DUNE) ? new DuneAbilityTrim(null)
                : pattern.equals(SNOUT) ? new SnoutAbilityTrim(null)
                : null
                );
    }

    public static AbilityTrim getTrimIfValid(@NotNull Player player) {
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

    public static void equipTrimIfPresent(Player plr) {
        AbilityTrim trim = ArmorUtils.getTrimIfValid(plr);

        if (trim != null && Storage.getAbilityTrim(plr) == null) {
            // Send message & play sound that the trim was equipped if it was not already equipped
            plr.sendMessage(Component.text("Trim Equipped ").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD)
                    .append(Component.text("You gained the abilities of the " + trim.getName() + " armor trim.").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, false))
            );
            plr.playSound(plr.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 1);

            // register the trim
            trim.setOwner(plr);
            Storage.setActiveTrim(plr, trim);
        } else {
            AbilityTrim earlierTrim = Storage.getAbilityTrim(plr);
            if (earlierTrim != null) {
                // Send message & play sound that the trim was unequipped if it was already equipped
                plr.sendMessage(Component.text("Trim Unequipped ").color(NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD)
                        .append(Component.text("You lost the abilities of the " + earlierTrim.getName() + " armor trim.").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, false))
                );
                plr.playSound(plr.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, 1);

                earlierTrim.disable();
                Storage.setActiveTrim(plr, null);
                plr.sendActionBar(Component.empty());
            }
        }
    }

    public static void equipTrimOverride(Player plr) {
        if (Storage.getAbilityTrim(plr) != null) {
            Storage.setActiveTrim(plr, null);
            equipTrimIfPresent(plr);
        }
    }
}
