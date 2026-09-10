package me.stivodavco.trimAbilities.abstractClasses;

import com.destroystokyo.paper.ParticleBuilder;
import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.utilities.ParticleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
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
        INSTANT,
        NONE
    }

    // General info (same for every instance of an AbilityTrim)
    public static int COOLDOWN = 0;
    public static AbilityType ABILITY_TYPE = null;
    public static int ACTIVATION_TIME = 0;
    public static int USE_TIME = 0;
    public static String NAME = "Null";
    public static String ABILITY_NAME = "Null";
    public static TrimPattern PATTERN = null;

    // Active info (different for every instance of an AbilityTrim when they are already active and used by someone)
    public int cooldownLeft = 0; // cooldown left
    public int activeTimeLeft = 0; // how long the ability is going to be activated for
    public int usingTimeLeft = 0;
    public boolean active = false;
    public boolean using = false;
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
    public AbilityType getType() {
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

    // returns how long the ability is going to be used
    public int getUsingTimeLeft() { return usingTimeLeft; }

    // returns true if the ability is active, otherwise false
    public boolean isActive() {
        return active;
    }

    // returns true if the ability is being used, otherwise false (in case of UNTIL_USED)
    public boolean isUsing() { return using; }

    // subtracts 1 from the cooldown (seconds, if possible)
    public void subtractCooldown() {
        if (cooldownLeft > 0 && !active) {
            cooldownLeft--;
            if (cooldownLeft == 0) {
                getOwner().playSound(getOwner().getLocation(), Sound.BLOCK_VAULT_OPEN_SHUTTER, SoundCategory.UI, 0.2f, 2);
            }
        }
    }

    // subtracts 1 from the active time (seconds, if possible)
    public void subtractTime() {
        if (active && activeTimeLeft > 0) {
            activeTimeLeft--;
            if (activeTimeLeft <= 0) {
                getOwner().playSound(getOwner().getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, SoundCategory.UI, 0.2f, 2);
                active = false;
            }
        } else if (using && usingTimeLeft > 0) {
            usingTimeLeft--;
            if (usingTimeLeft <= 0) {
                getOwner().playSound(getOwner().getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, SoundCategory.UI, 0.2f, 2);
                using = false;
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

    // resets the until used time
    public void resetUntilUsedTime() {
        using = false;
        usingTimeLeft = USE_TIME;
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
        resetUntilUsedTime();

        switch (getType()) {
            case LIMITED_TIME:
                active = true;
                break;
            case LIMITED_UNTIL_USED, LIMITED_ACTIVATED_AFTER_USED:
                using = true;
                break;
        }

        getOwner().getWorld().playSound(getOwner().getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.PLAYERS, 1, 2);
        ParticleBuilder particle = new ParticleBuilder(Particle.GLOW).extra(0);
        ParticleUtils.cylinder(getOwner().getLocation(), 0.5, 2, particle);

        customAbility();
    } // runs the ability

    public abstract void customAbility();

    public void disable() {
        HandlerList.unregisterAll(this);
        internalDisable();
    } // disables the ability (in case there is a BukkitRunnable running)

    public abstract void internalDisable();

    public void used() {
        if (using || getType() == AbilityType.UNTIL_USED) {
            getOwner().playSound(getOwner().getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.UI, 0.2f, 2);
            using = false;
            if (getType() == AbilityType.LIMITED_ACTIVATED_AFTER_USED) {
                resetActivatedTime();
                active = true;
            }
            internalUsed();
        }
    }

    public abstract void internalUsed();

    public AbilityTrim(@NotNull Player newOwner, int cooldown, @NotNull AbilityType activationType, int activationTime, int useTime, String name, String abilityName) {
        owner = newOwner;

        COOLDOWN = cooldown;
        ABILITY_TYPE = activationType;
        ACTIVATION_TIME = activationTime;
        USE_TIME = useTime;
        NAME = name;
        ABILITY_NAME = abilityName;

        resetCooldown();
        resetActivatedTime();
        resetUntilUsedTime();

        Bukkit.getPluginManager().registerEvents(this, TrimAbilities.getInstance());

        enable();
    }
}
