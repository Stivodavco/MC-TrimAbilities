package me.stivodavco.trimAbilities.abilityTrims;

import com.destroystokyo.paper.ParticleBuilder;
import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.utilities.ParticleUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class CoastAbilityTrim extends AbilityTrim {
    public static final int LASER_RANGE = 10;
    public static final int LASER_DMG = 3;
    public static final PotionEffect WATER_BREATHING = new PotionEffect(PotionEffectType.WATER_BREATHING, -1, 255, true, false);
    public static final PotionEffect DOLPHINS_GRACE = new PotionEffect(PotionEffectType.DOLPHINS_GRACE, -1, 2, true, false);

    public BukkitTask runnable;

    public CoastAbilityTrim(Player newOwner) {
        super(newOwner, TrimPattern.COAST, AbilityType.LIMITED_TIME);
    }

    @Override
    public void enable() {
        getOwner().addPotionEffects(List.of(WATER_BREATHING, DOLPHINS_GRACE));
    }

    @Override
    public void customAbility() {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!active) {
                    cancel();
                    runnable = null;
                }

                Entity entity = getOwner().getTargetEntity(LASER_RANGE);



                if (entity instanceof LivingEntity livingEntity) {
                    if (livingEntity.getActiveItem().getType() != Material.SHIELD) {
                        livingEntity.damage(LASER_DMG);
                    }
                    Location targetLoc = livingEntity.getEyeLocation();

                    ParticleBuilder particle = Particle.GLOW_SQUID_INK.builder().extra(0);
                    ParticleUtils.beam(getOwner().getEyeLocation(), targetLoc, particle);

                    getOwner().getWorld().playSound(livingEntity.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_HURT, 1, 2);
                }
                getOwner().getWorld().playSound(getOwner().getLocation(), Sound.ENTITY_GUARDIAN_ATTACK, 1, 2);
            }
        }.runTaskTimer(TrimAbilities.getInstance(), 0, 5);
    }

    @Override
    public void internalDisable() {
        getOwner().removePotionEffect(PotionEffectType.WATER_BREATHING);
        getOwner().removePotionEffect(PotionEffectType.DOLPHINS_GRACE);

        if (runnable != null) {
            runnable.cancel();
        }
    }

    @Override
    public void internalUsed() {

    }
}
