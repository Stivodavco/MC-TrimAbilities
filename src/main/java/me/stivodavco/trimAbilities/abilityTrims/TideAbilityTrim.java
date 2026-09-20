package me.stivodavco.trimAbilities.abilityTrims;

import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class TideAbilityTrim extends AbilityTrim {
    public static final int IMMUNITY_DURATION = 10;
    public static final PotionEffect REGEN = new PotionEffect(PotionEffectType.REGENERATION, 40, 1, true, true);

    private BukkitTask runnable;

    public TideAbilityTrim(Player newOwner) {
        super(newOwner, 120, AbilityType.LIMITED_TIME, 10, 0, "Tide", "Water Tornado");
    }

    @Override
    public void enable() {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                boolean inWater = getOwner().getLocation().getBlock().getType() == Material.WATER;
                boolean inRain = getOwner().getWorld().hasStorm();

                if ( (inWater || inRain) && !getOwner().hasPotionEffect(PotionEffectType.REGENERATION) ) {
                    getOwner().addPotionEffect(REGEN);
                }
            }
        }.runTaskTimer(TrimAbilities.getInstance(), 0, 10);
    }

    @Override
    public void customAbility() {

    }

    @Override
    public void internalDisable() {
        if (runnable != null) {
            runnable.cancel();
        }
    }

    @Override
    public void internalUsed() {

    }
}
