package me.stivodavco.trimAbilities.abilityTrims;

import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.utilities.RandomnessUtils;
import me.stivodavco.trimAbilities.utilities.Storage;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;

public class SentryAbilityTrim extends AbilityTrim {
    // specific info
    private static final int ARROW_DELAY_TICKS = 30;
    private static final int RANGE = 5;
    private BukkitTask runnable;

    public SentryAbilityTrim(Player newOwner) {
        super(newOwner, 60, AbilityType.LIMITED_TIME, 30, 0, "Sentry", "Turret");
    }

    @Override
    public void enable() {}

    @Override
    public void customAbility() {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
            if (!active) {
                runnable = null;
                cancel();
            }

            List<Entity> nearbyEntities = getOwner().getNearbyEntities(RANGE,RANGE,RANGE);

            for (Entity entity : nearbyEntities) {
                if (!entity.equals(getOwner()) && entity instanceof LivingEntity) {
                    // launch an arrow
                    Vector direction = entity.getLocation().toVector().subtract(getOwner().getLocation().add(0, 1, 0).toVector());
                    AbstractArrow arrow = getOwner().launchProjectile(AbstractArrow.class, direction);

                    arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);

                    return;
                }
            }
            }
        }.runTaskTimer(TrimAbilities.getInstance(), 0, ARROW_DELAY_TICKS);
    }

    @Override
    public void internalDisable() {
        if (runnable != null) {
            runnable.cancel();
        }
    }

    @Override
    public void internalUsed() {}

    /* EVENTS */
    @EventHandler
    public void onDamage(EntityDamageEvent e) { // SENTRY
        if (e.getEntity() instanceof Player player) {
            if (Storage.getAbilityTrim(player) instanceof SentryAbilityTrim) {
                if (e.getDamageSource().getDirectEntity() instanceof Arrow && RandomnessUtils.chance(80)) {
                    player.getWorld().playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1, 1);
                    e.setCancelled(true);
                }
            }
        }
    }
}
