package me.stivodavco.trimAbilities.utilities;

import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.abilityTrims.BoltAbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.CoastAbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.DuneAbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.EyeAbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.FlowAbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.HostAbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.RaiserAbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.RibAbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.SentryAbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.ShaperAbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.SilenceAbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.SnoutAbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.SpireAbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.TideAbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.VexAbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.WardAbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.WayfinderAbilityTrim;
import me.stivodavco.trimAbilities.abilityTrims.WildAbilityTrim;
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

    private static final Map<TrimPattern, TrimKey> TRIM_KEYS = Map.ofEntries(
        entry(TrimPattern.BOLT, new TrimKey(
            TRIM_CONFIG.getString("bolt.name"),
            TRIM_CONFIG.getString("bolt.title"),
            BoltAbilityTrim.class,
            NamespacedKey.minecraft("bolt_armor_trim_smithing_template"),
            passiveInfoFromConfig("bolt"), abilityInfoFromConfig("bolt")
            )),
        entry(TrimPattern.COAST, new TrimKey(
            TRIM_CONFIG.getString("coast.name"),
            TRIM_CONFIG.getString("coast.title"),
            CoastAbilityTrim.class,
            NamespacedKey.minecraft("coast_armor_trim_smithing_template"),
            passiveInfoFromConfig("coast"), abilityInfoFromConfig("coast")
            )),
        entry(TrimPattern.DUNE, new TrimKey(
            TRIM_CONFIG.getString("dune.name"),
            TRIM_CONFIG.getString("dune.title"),
            DuneAbilityTrim.class,
            NamespacedKey.minecraft("dune_armor_trim_smithing_template"),
            passiveInfoFromConfig("dune"), abilityInfoFromConfig("dune")
            )),
        entry(TrimPattern.EYE, new TrimKey(
            TRIM_CONFIG.getString("eye.name"),
            TRIM_CONFIG.getString("eye.title"),
            EyeAbilityTrim.class,
            NamespacedKey.minecraft("eye_armor_trim_smithing_template"),
            passiveInfoFromConfig("eye"), abilityInfoFromConfig("eye")
            )),
        entry(TrimPattern.FLOW, new TrimKey(
            TRIM_CONFIG.getString("flow.name"),
            TRIM_CONFIG.getString("flow.title"),
            FlowAbilityTrim.class,
            NamespacedKey.minecraft("flow_armor_trim_smithing_template"),
            passiveInfoFromConfig("flow"), abilityInfoFromConfig("flow")
            )),
        entry(TrimPattern.HOST, new TrimKey(
            TRIM_CONFIG.getString("host.name"),
            TRIM_CONFIG.getString("host.title"),
            HostAbilityTrim.class,
            NamespacedKey.minecraft("host_armor_trim_smithing_template"),
            passiveInfoFromConfig("host"), abilityInfoFromConfig("host")
            )),
        entry(TrimPattern.RAISER, new TrimKey(
            TRIM_CONFIG.getString("raiser.name"),
            TRIM_CONFIG.getString("raiser.title"),
            RaiserAbilityTrim.class,
            NamespacedKey.minecraft("raiser_armor_trim_smithing_template"),
            passiveInfoFromConfig("raiser"), abilityInfoFromConfig("raiser")
            )),
        entry(TrimPattern.RIB, new TrimKey(
            TRIM_CONFIG.getString("rib.name"),
            TRIM_CONFIG.getString("rib.title"),
            RibAbilityTrim.class,
            NamespacedKey.minecraft("rib_armor_trim_smithing_template"),
            passiveInfoFromConfig("rib"), abilityInfoFromConfig("rib")
            )),
        entry(TrimPattern.SENTRY, new TrimKey(
            TRIM_CONFIG.getString("sentry.name"),
            TRIM_CONFIG.getString("sentry.title"),
            SentryAbilityTrim.class,
            NamespacedKey.minecraft("sentry_armor_trim_smithing_template"),
            passiveInfoFromConfig("sentry"), abilityInfoFromConfig("sentry")
            )),
        entry(TrimPattern.SHAPER, new TrimKey(
            TRIM_CONFIG.getString("shaper.name"),
            TRIM_CONFIG.getString("shaper.title"),
            ShaperAbilityTrim.class,
            NamespacedKey.minecraft("shaper_armor_trim_smithing_template"),
            passiveInfoFromConfig("shaper"), abilityInfoFromConfig("shaper")
            )),
        entry(TrimPattern.SILENCE, new TrimKey(
            TRIM_CONFIG.getString("silence.name"),
            TRIM_CONFIG.getString("silence.title"),
            SilenceAbilityTrim.class,
            NamespacedKey.minecraft("silence_armor_trim_smithing_template"),
            passiveInfoFromConfig("silence"), abilityInfoFromConfig("silence")
            )),
        entry(TrimPattern.SNOUT, new TrimKey(
            TRIM_CONFIG.getString("snout.name"),
            TRIM_CONFIG.getString("snout.title"),
            SnoutAbilityTrim.class,
            NamespacedKey.minecraft("snout_armor_trim_smithing_template"),
            passiveInfoFromConfig("snout"), abilityInfoFromConfig("snout")
            )),
        entry(TrimPattern.SPIRE, new TrimKey(
            TRIM_CONFIG.getString("spire.name"),
            TRIM_CONFIG.getString("spire.title"),
            SpireAbilityTrim.class,
            NamespacedKey.minecraft("spire_armor_trim_smithing_template"),
            passiveInfoFromConfig("spire"), abilityInfoFromConfig("spire")
            )),
        entry(TrimPattern.TIDE, new TrimKey(
            TRIM_CONFIG.getString("tide.name"),
            TRIM_CONFIG.getString("tide.title"),
            TideAbilityTrim.class,
            NamespacedKey.minecraft("tide_armor_trim_smithing_template"),
            passiveInfoFromConfig("tide"), abilityInfoFromConfig("tide")
            )),
        entry(TrimPattern.VEX, new TrimKey(
            TRIM_CONFIG.getString("vex.name"),
            TRIM_CONFIG.getString("vex.title"),
            VexAbilityTrim.class,
            NamespacedKey.minecraft("vex_armor_trim_smithing_template"),
            passiveInfoFromConfig("vex"), abilityInfoFromConfig("vex")
            )),
        entry(TrimPattern.WARD, new TrimKey(
            TRIM_CONFIG.getString("ward.name"),
            TRIM_CONFIG.getString("ward.title"),
            WardAbilityTrim.class,
            NamespacedKey.minecraft("ward_armor_trim_smithing_template"),
            passiveInfoFromConfig("ward"), abilityInfoFromConfig("ward")
            )),
        entry(TrimPattern.WAYFINDER, new TrimKey(
            TRIM_CONFIG.getString("wayfinder.name"),
            TRIM_CONFIG.getString("wayfinder.title"),
            WayfinderAbilityTrim.class,
            NamespacedKey.minecraft("wayfinder_armor_trim_smithing_template"),
            passiveInfoFromConfig("wayfinder"), abilityInfoFromConfig("wayfinder")
            )),
        entry(TrimPattern.WILD, new TrimKey(
            TRIM_CONFIG.getString("wild.name"),
            TRIM_CONFIG.getString("wild.title"),
            WildAbilityTrim.class,
            NamespacedKey.minecraft("wild_armor_trim_smithing_template"),
            passiveInfoFromConfig("wild"), abilityInfoFromConfig("wild")
            ))
    );

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
