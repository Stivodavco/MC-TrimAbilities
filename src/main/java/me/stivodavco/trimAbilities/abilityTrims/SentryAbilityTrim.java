package me.stivodavco.trimAbilities.abilityTrims;

import me.stivodavco.trimAbilities.enums.AbilityActivationType;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.TrimAbilities;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;

public class SentryAbilityTrim extends AbilityTrim {
    // specific info
    private static final int ARROW_DELAY_TICKS = 30;
    private BukkitTask runnable = null;

    public SentryAbilityTrim(Player newOwner) {
        super(newOwner, 60, AbilityActivationType.LIMITED_TIME, 30, "Sentry", "Turret");
    }

    @Override
    public void ability() {
        if (getOwner() == null) {
            throw new Error("Attempt to use the .ability() method on an AbilityTrim without an owner!");
        }
        resetActivatedTime();
        resetCooldown();

        active = true; // since the ability is one that is activated for a set time, only setting active to true is needed.

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (active) {
                    List<Entity> nearbyEntities = getOwner().getNearbyEntities(5,5,5);

                    for (Entity entity : nearbyEntities) {
                        if (!entity.equals(getOwner()) && entity instanceof LivingEntity) {
                            // launch an arrow
                            Vector direction = entity.getLocation().toVector().subtract(getOwner().getLocation().add(0, 1, 0).toVector());
                            AbstractArrow arrow = getOwner().launchProjectile(AbstractArrow.class, direction);

                            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);

                            return;
                        }
                    }
                } else {
                    runnable = null;
                    cancel();
                }
            }
        }.runTaskTimer(TrimAbilities.getInstance(), 0, ARROW_DELAY_TICKS);
    }

    @Override
    public void disable() {
        if (runnable != null) {
            runnable.cancel();
        }
    }
}
