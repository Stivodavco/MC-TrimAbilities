package me.stivodavco.trimAbilities.utilities;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.stivodavco.trimAbilities.TrimAbilities;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrimRegistry {
    public record PassiveInfo(boolean enabled, String name, String description) {}
    public record AbilityInfo(boolean enabled, String name, String description, int cooldown, int activationTime, int useTime) {}

    public static PassiveInfo passiveInfoFromConfig(String trimName) {
        trimName = trimName.toLowerCase();
        YamlConfiguration config = getConfig();

        boolean enabled = getConfig().getBoolean(trimName + ".passive.enabled");

        String name = getConfig().getString(trimName + ".passive.name");
        String desc = getConfig().getString(trimName + ".passive.name");

        return new PassiveInfo(enabled, name, desc);
    }

    public static AbilityInfo abilityInfoFromConfig(String trimName) {
        trimName = trimName.toLowerCase();
        YamlConfiguration config = getConfig();

        boolean enabled = getConfig().getBoolean(trimName + ".ability.enabled");

        String name = getConfig().getString(trimName + ".ability.name");
        String desc = getConfig().getString(trimName + ".ability.name");

        int cooldown = getConfig().getInt(trimName + ".ability.cooldown");
        int activationTime = getConfig().getInt(trimName + ".ability.activation-time");;
        int useTime = getConfig().getInt(trimName + ".ability.use-time");;

        return new AbilityInfo(enabled ,name, desc, cooldown, activationTime, useTime);
    }

    public record TrimKey(String name, String title, NamespacedKey itemKey, PassiveInfo passiveInfo, AbilityInfo abilityInfo) {}

    private static final YamlConfiguration TRIM_CONFIG = YamlConfiguration.loadConfiguration(new File(TrimAbilities.getInstance().getDataFolder(), "trims.yml"));

    private static final Map<TrimPattern, TrimKey> TRIM_KEYS = new HashMap<>(); // the trim keys are stored when the plugin is loaded so they can be accessed faster later on

    public static void generateTrimKeys() {
        List<TrimPattern> patterns = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN).stream().toList();

        for (TrimPattern pattern : patterns) {
            String patternName = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN).getKey(pattern).asString().substring(10);

            if (!TRIM_CONFIG.contains(patternName)) {
                continue;
            }

            TRIM_KEYS.put(pattern, new TrimKey(
                    TRIM_CONFIG.getString(patternName + ".name"),
                    TRIM_CONFIG.getString(patternName + ".title"),
                    NamespacedKey.minecraft(patternName + "_armor_trim_smithing_template"),
                    passiveInfoFromConfig(patternName),
                    abilityInfoFromConfig(patternName)
            ));
        }

        TrimAbilities.getInstance().getLogger().info(TRIM_KEYS.toString());
    }

    public static @Nullable TrimKey getTrimKey(TrimPattern pattern) {
        return TRIM_KEYS.getOrDefault(pattern, null);
    }

    public static @NotNull YamlConfiguration getConfig() {
        return TRIM_CONFIG;
    }
}
