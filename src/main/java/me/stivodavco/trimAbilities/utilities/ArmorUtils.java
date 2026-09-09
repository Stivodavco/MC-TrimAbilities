package me.stivodavco.trimAbilities.utilities;

import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.abilityTrims.*;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
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
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

import static org.bukkit.inventory.meta.trim.TrimPattern.*;

public class ArmorUtils {
    public static ArmorTrim getTrim(ItemStack item) {
        Optional<ArmorTrim> targetPattern = Optional.ofNullable(item)
                .map(ItemStack::getItemMeta)
                .map(itemMeta -> (ArmorMeta) itemMeta)
                .map(ArmorMeta::getTrim);

        return targetPattern.orElse(null);
    }

    private static final Map<TrimPattern, Class< ? extends AbilityTrim>> PATTERN_MAP = Map.of(
            SENTRY, SentryAbilityTrim.class,
            DUNE, DuneAbilityTrim.class,
            SNOUT, SnoutAbilityTrim.class,
            WARD, WardAbilityTrim.class,
            BOLT, BoltAbilityTrim.class
    );
    public static @Nullable Class< ? extends AbilityTrim> getAbilityTrimClass(@NotNull TrimPattern pattern) {
        return PATTERN_MAP.getOrDefault(pattern, null);
    }

    public static @Nullable Class< ? extends AbilityTrim> getTrimClassIfValid(@NotNull Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        TrimPattern targetPattern = null;

        ArmorTrim trim = ArmorUtils.getTrim(helmet);
        if (trim != null) {
            targetPattern = trim.getPattern();
        }

        if (targetPattern != null) {
            boolean equal = true;

            for (ItemStack armor : player.getInventory().getArmorContents()) { // check if all armor has the same trim, if not, set equal to false
                if (armor != null) {
                    if (getTrim(armor).getPattern() != targetPattern) {
                        equal = false;
                    }
                } else {
                    equal = false;
                }
            }

            if (equal) { // if all armor patterns are equal
                return getAbilityTrimClass(targetPattern);
            }
        }

        return null;
    }

    public static @Nullable AbilityTrim createInstance(@NotNull Class< ? extends AbilityTrim> clazz, @NotNull Player owner) {
        try {
            return clazz.getDeclaredConstructor(Player.class).newInstance(owner);
        } catch (Exception e) {
            TrimAbilities.getInstance().getLogger().log(Level.SEVERE, "Failed to create an instance of class " + clazz.getSimpleName(), e.getCause());
            return null;
        }
    };

    public static void equipTrimIfPresent(@NotNull Player plr) {
        Class< ? extends AbilityTrim> trimClass = getTrimClassIfValid(plr);

        if (trimClass != null && Storage.getAbilityTrim(plr) == null) {
            AbilityTrim trim = createInstance(trimClass, plr);

            if (trim != null) {
                // Send message & play sound that the trim was equipped if it was not already equipped
                plr.sendMessage(Component.text("Trim Equipped ").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD)
                        .append(Component.text("You gained the abilities of the " + trim.getName() + " armor trim.").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, false))
                );
                plr.playSound(plr.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 1);

                // register the trim
                Storage.setActiveTrim(plr, trim);
            }

        } else {
            AbilityTrim earlierTrim = Storage.getAbilityTrim(plr);
            if (earlierTrim != null && (trimClass == null || earlierTrim.getClass() != trimClass)) {
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
}
