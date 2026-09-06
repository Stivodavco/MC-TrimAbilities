package me.stivodavco.trimAbilities.abstractClasses;

import me.stivodavco.trimAbilities.TrimAbilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;

public abstract class AbilityTrim implements Listener {
    public enum AbilityType {
        LIMITED_TIME,
        UNTIL_USED,
        LIMITED_UNTIL_USED,
        LIMITED_ACTIVATED_AFTER_USED,
        INSTANT
    }

    // General info (same for every instance of an AbilityTrim)
    public static int COOLDOWN = 0;
    public static AbilityType ABILITY_TYPE = null;
    public static int ACTIVATION_TIME = 0;
    public static String NAME = "Null";
    public static String ABILITY_NAME = "Null";
    public static TrimPattern PATTERN = null;

    // Active info (different for every instance of an AbilityTrim when they are already active and used by someone)
    public int cooldownLeft = 0; // cooldown left
    public int activeTimeLeft = 0; // how long the ability is going to be activated for
    public boolean active = false;
    private Player owner;

    // returns the name of the trim - eg. "Sentry"
    public String getName() {
        return NAME;
    }

    // returns the name of the ability - eg. "Turret"
    public String getAbilityName() {
        return ABILITY_NAME;
    }

    // returns the AbilityActivationType of the ability - eg. AbilityActivationType.LIMITED_TIME
    public AbilityType getActivationType() {
        return ABILITY_TYPE;
    }

    // returns the ActivationTime in seconds
    public int getActivationTime() { return ACTIVATION_TIME; }

    // returns the TrimPattern of this AbilityTrim
    public static @NotNull TrimPattern getPattern() {
        return PATTERN;
    }

    // returns the remaining cooldown to be able to use the ability
    public int getCooldown() {
        return cooldownLeft;
    }

    // returns how long the ability is going to be active for
    public int getActiveTimeLeft() {
        return activeTimeLeft;
    }

    // returns true if the ability is active, otherwise false
    public boolean isActive() {
        return active;
    }

    // subtracts 1 from the cooldown (seconds, if possible)
    public void subtractCooldown() {
        if (cooldownLeft > 0 && !active) {
            cooldownLeft--;
        }
    }

    // subtracts 1 from the active time (seconds, if possible)
    public void subtractActiveTime() {
        if (activeTimeLeft > 0 && ABILITY_TYPE.equals(AbilityType.LIMITED_TIME)) {
            activeTimeLeft--;
            if (activeTimeLeft <= 0) {
                active = false;
            }
        }
    }

    // resets the cooldown
    public void resetCooldown() {
        cooldownLeft = COOLDOWN;
    }

    // resets the activated time
    public void resetActivatedTime() {
        active = false;
        activeTimeLeft = ACTIVATION_TIME;
    }

    // returns true if the ability is ready, otherwise false
    public boolean isAbilityReady() {
        return cooldownLeft == 0;
    }

    public abstract void enable();

    // returns the owner
    public @NotNull Player getOwner() {
        return owner;
    }

    public void ability() {
        resetActivatedTime();
        resetCooldown();

        if (ABILITY_TYPE == AbilityType.LIMITED_TIME) {
            active = true;
        }

        customAbility();
    } // runs the ability

    public abstract void customAbility();

    public void disable() {
        HandlerList.unregisterAll(this);
        internalDisable();
    } // disables the ability (in case there is a BukkitRunnable running)

    public abstract void internalDisable();

    public AbilityTrim(@NotNull Player newOwner, int cooldown, @NotNull AbilityType activationType, int activationTime, String name, String abilityName) {
        owner = newOwner;
        resetCooldown();
        resetActivatedTime();

        COOLDOWN = cooldown;
        ABILITY_TYPE = activationType;
        ACTIVATION_TIME = activationTime;
        NAME = name;
        ABILITY_NAME = abilityName;

        Bukkit.getPluginManager().registerEvents(this, TrimAbilities.getInstance());

        enable();
    }
}
