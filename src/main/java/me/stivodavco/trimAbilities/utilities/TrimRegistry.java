package me.stivodavco.trimAbilities.utilities;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import org.apache.commons.lang3.StringUtils;
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
    public record PassiveInfo(String name, String description) {}
    public record AbilityInfo(String name, String description, int cooldown, int activationTime, int useTime) {}

    public static PassiveInfo passiveInfoFromConfig(String trimName) {
        trimName = trimName.toLowerCase();
        YamlConfiguration config = getConfig();

        String name = getConfig().getString(trimName + ".passive.name");
        String desc = getConfig().getString(trimName + ".passive.name");

        return new PassiveInfo(name, desc);
    }

    public static AbilityInfo abilityInfoFromConfig(String trimName) {
        trimName = trimName.toLowerCase();
        YamlConfiguration config = getConfig();

        String name = getConfig().getString(trimName + ".ability.name");
        String desc = getConfig().getString(trimName + ".ability.name");

        int cooldown = getConfig().getInt(trimName + ".ability.cooldown");
        int activationTime = getConfig().getInt(trimName + ".ability.activation-time");;
        int useTime = getConfig().getInt(trimName + ".ability.use-time");;

        return new AbilityInfo(name, desc, cooldown, activationTime, useTime);
    }

    public record TrimKey(String name, String title, Class<? extends AbilityTrim> clazz, NamespacedKey itemKey, PassiveInfo passiveInfo, AbilityInfo abilityInfo) {}

    private static final YamlConfiguration TRIM_CONFIG = YamlConfiguration.loadConfiguration(new File(TrimAbilities.getInstance().getDataFolder(), "trims.yml"));

    private static final Map<TrimPattern, TrimKey> TRIM_KEYS = new HashMap<>(); // the trim keys are stored when the plugin is loaded so they can be accessed faster later on

    public static void generateTrimKeys() {
        List<TrimPattern> patterns = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN).stream().toList();

        for (TrimPattern pattern : patterns) {
            String patternName = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN).getKey(pattern).asString().substring(10);

            if (!TRIM_CONFIG.contains(patternName)) {
                continue;
            }

            Class<?> raw = null;
            try {
                raw = Class.forName("me.stivodavco.trimAbilities.abilityTrims." + StringUtils.capitalize(patternName) + "AbilityTrim");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            Class<? extends AbilityTrim> clazz = raw.asSubclass(AbilityTrim.class);

            TRIM_KEYS.put(pattern, new TrimKey(
                    TRIM_CONFIG.getString(patternName + ".name"),
                    TRIM_CONFIG.getString(patternName + ".title"),
                    clazz,
                    NamespacedKey.minecraft(patternName + "_armor_trim_smithing_template"),
                    passiveInfoFromConfig(patternName),
                    abilityInfoFromConfig(patternName)
            ));
        }
    }

    public static @Nullable TrimKey getTrimKey(TrimPattern pattern) {
        return TRIM_KEYS.getOrDefault(pattern, null);
    }

    public static @Nullable TrimKey getTrimKey(Class<? extends AbilityTrim> clazz) {
        TrimKey match = null;
        for (TrimKey key : TRIM_KEYS.values()) {
            if (key.clazz == clazz) {
                match = key;
            }
        }
        return match;
    }

    public static @NotNull YamlConfiguration getConfig() {
        return TRIM_CONFIG;
    }
}
