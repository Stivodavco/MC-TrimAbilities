package me.stivodavco.trimAbilities.abilityTrims;

import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WardAbilityTrim extends AbilityTrim {
    private static final int RADIUS = 25;
    private static final int EFFECT_DURATION = 10;

    private static final NamespacedKey MODIFIER_KEY = new NamespacedKey(TrimAbilities.getInstance(), "ward_trim_health_modifier");

    private @Nullable BukkitTask runnable = null;
    private Map<Player, Location> playerLocations = new HashMap<>();

    public WardAbilityTrim(Player newOwner) {
        super(newOwner, 180, AbilityType.LIMITED_UNTIL_USED, 10, "Wild", "Sensitive Shriek");
    }

    @Override
    public void enable() {
        AttributeInstance maxHealth =  getOwner().getAttribute(Attribute.MAX_HEALTH);

        if (maxHealth != null && maxHealth.getModifier(MODIFIER_KEY) == null) {
            AttributeModifier modifier = new AttributeModifier(MODIFIER_KEY, 10, AttributeModifier.Operation.ADD_NUMBER);

            maxHealth.addModifier(modifier);
        }
    }

    @Override
    public void customAbility() {
        List<Entity> nearbyEntities = getOwner().getNearbyEntities(RADIUS, RADIUS, RADIUS);

        nearbyEntities.add(getOwner());

        TrimAbilities.getInstance().getLogger().info(nearbyEntities.toString());

        for (Entity entity : nearbyEntities) {
            if (entity instanceof Player player) { // && player != getOwner()
                playerLocations.put(player, player.getLocation());
            }
        }

        TrimAbilities.getInstance().getLogger().info(playerLocations.toString());

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!active) {
                    runnable = null;
                    cancel();
                }

                for (Player player : playerLocations.keySet()) {
                    Location startingLoc = playerLocations.get(player);
                    Location loc = player.getLocation();

                    boolean xDiff = Math.abs(startingLoc.getX() - loc.getX()) > 0.3;
                    boolean yDiff = Math.abs(startingLoc.getY() - loc.getY()) > 0.3;
                    boolean zDiff = Math.abs(startingLoc.getZ() - loc.getZ()) > 0.3;

                    if (xDiff || yDiff || zDiff) {
                        player.sendMessage("YOU MOVED!!!");

                        runnable = null;
                        cancel();

                        activeTimeLeft = EFFECT_DURATION;
                    }
                }
            }
        }.runTaskTimer(TrimAbilities.getInstance(), 10, 10);
    }

    @Override
    public void internalDisable() {
        AttributeInstance maxHealth =  getOwner().getAttribute(Attribute.MAX_HEALTH);

        if (maxHealth != null && maxHealth.getModifier(MODIFIER_KEY) != null) {
            maxHealth.removeModifier(MODIFIER_KEY);
        }

        if (runnable != null) {
            runnable.cancel();
            runnable = null;
        }
    }

    /* EVENTS */
    @EventHandler()
    public void onPlayerMove(PlayerMoveEvent e) {


        boolean xDiff = Math.abs(e.getFrom().getX() - e.getTo().getX()) > 0.3;
        boolean yDiff = Math.abs(e.getFrom().getY() - e.getTo().getY()) > 0.3;
        boolean zDiff = Math.abs(e.getFrom().getZ() - e.getTo().getZ()) > 0.3;

        if (xDiff || yDiff || zDiff) {

        }
    }
}
