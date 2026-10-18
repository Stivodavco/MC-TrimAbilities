package me.stivodavco.trimAbilities.utilities;

import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.abilityTrims.SentryAbilityTrim;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;

import static java.util.Map.entry;

public class TrimRegistry {
    public record PassiveInfo(String name, String description) {}

    public record AbilityInfo(String name, String description, int cooldown, int activationTime, int useTime) {}

    public record TrimKey(String name, String title, Class<? extends AbilityTrim> clazz, NamespacedKey itemKey, PassiveInfo passiveInfo, AbilityInfo abilityInfo) {}

    private static final YamlConfiguration TRIM_CONFIG = YamlConfiguration.loadConfiguration(new File(TrimAbilities.getInstance().getDataFolder(), "trims.yml"));

    private static final Map<TrimPattern, TrimKey> TRIM_DATA = Map.ofEntries(
        entry(TrimPattern.SENTRY, new TrimKey(
            "Sentry",
            getConfig().getString("sentry.title"),
            SentryAbilityTrim.class,
            NamespacedKey.minecraft("sentry_armor_trim_smithing_template"),
            new PassiveInfo(getConfig().getString("sentry.passive.name"), getConfig().getString("sentry.passive.description")),
            new AbilityInfo(getConfig().getString("sentry.passive.name"), "n/a", getConfig().getInt("sentry.cooldown"), getConfig().getInt("sentry.activation-time"), getConfig().getInt("sentry.use-time"))
            ))
    );

    public static @Nullable TrimKey getTrimKey(TrimPattern pattern) {
        return TRIM_DATA.getOrDefault(pattern, null);
    }

    public static @NotNull YamlConfiguration getConfig() {
        return TRIM_CONFIG;
    }
}
