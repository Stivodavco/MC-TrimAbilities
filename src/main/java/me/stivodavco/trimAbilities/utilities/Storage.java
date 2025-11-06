package me.stivodavco.trimAbilities.utilities;

import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class Storage {
    private static HashMap<Player, AbilityTrim> activeTrim = new HashMap<>();

    public static @Nullable AbilityTrim getAbilityTrim(@NotNull Player player) {
        return activeTrim.get(player);
    }

    public static void setActiveTrim(Player player, AbilityTrim trim) {
        if (trim != null) {
            if (activeTrim.get(player) == null) {
                activeTrim.put(player, trim);
            } else {
                activeTrim.replace(player, trim);
            }
        } else {
            activeTrim.remove(player);
        }
    }

    public static HashMap<Player, AbilityTrim> getActiveTrims() {
        return activeTrim;
    }
}
