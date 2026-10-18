package me.stivodavco.trimAbilities.abilityTrims;

import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ShaperAbilityTrim extends AbilityTrim {
    public static final int FLIGHT_DURATION  = 3;
    public static final PotionEffect HASTE = new PotionEffect(PotionEffectType.HASTE, -1, 255, true, false);

    public BukkitTask runnable;

    public ShaperAbilityTrim(Player newOwner) {
        super(newOwner, TrimPattern.SHAPER, AbilityType.LIMITED_TIME);
    }

    @Override
    public void enable() {
        getOwner().addPotionEffect(HASTE);
    }

    @Override
    public void customAbility() {
        getOwner().setAllowFlight(true);

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                getOwner().setAllowFlight(false);
            }
        }.runTaskLater(TrimAbilities.getInstance(), FLIGHT_DURATION * 20);
    }

    @Override
    public void internalDisable() {
        getOwner().removePotionEffect(PotionEffectType.HASTE);

        getOwner().setAllowFlight(false);
        if (runnable != null) {
            runnable.cancel();
        }
    }

    @Override
    public void internalUsed() {

    }
}
