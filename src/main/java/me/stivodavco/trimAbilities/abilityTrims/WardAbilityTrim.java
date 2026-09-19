package me.stivodavco.trimAbilities.abilityTrims;

import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WardAbilityTrim extends AbilityTrim {
    private static final int RADIUS = 25;
    private static final int EFFECT_DURATION = 10;

    private static final PotionEffect BLINDNESS = new PotionEffect(PotionEffectType.BLINDNESS, EFFECT_DURATION * 20, 255);
    private static final PotionEffect SLOWNESS = new PotionEffect(PotionEffectType.SLOWNESS, EFFECT_DURATION * 20, 3);
    private static final PotionEffect SPEED = new PotionEffect(PotionEffectType.SPEED, EFFECT_DURATION * 20, 3);
    private static final PotionEffect GLOWING = new PotionEffect(PotionEffectType.GLOWING, EFFECT_DURATION * 20, 255);

    private static final NamespacedKey MODIFIER_KEY = new NamespacedKey(TrimAbilities.getInstance(), "ward_trim_health_modifier");

    private @Nullable BukkitTask runnable = null;
    private Map<Player, Location> playerLocations = new HashMap<>();

    public WardAbilityTrim(Player newOwner) {
        super(newOwner, 180, AbilityType.LIMITED_ACTIVATED_AFTER_USED, EFFECT_DURATION, 10, "Ward", "Sensitive Shriek");
    }

    @Override
    public void enable() {
        AttributeInstance maxHealth =  getOwner().getAttribute(Attribute.MAX_HEALTH);

        if (maxHealth != null && maxHealth.getModifier(MODIFIER_KEY) == null) {
            AttributeModifier modifier = new AttributeModifier(MODIFIER_KEY, 10, AttributeModifier.Operation.ADD_NUMBER);

            maxHealth.addModifier(modifier);
        }
    }

    private void showTitleToEntities(List<Entity> entities, Title title, @Nullable Sound sound) {
        for (Entity entity : entities) {
            if (entity instanceof Player player) {
                player.showTitle(title);

                if (sound != null) {
                    player.playSound(player.getLocation(), sound, 1, 1);
                }
            }
        }
    }

    @Override
    public void customAbility() {
        getOwner().addPotionEffects(List.of(BLINDNESS, SLOWNESS));

        List<Entity> nearbyEntities = getOwner().getNearbyEntities(RADIUS, RADIUS, RADIUS);

        Component title = Component.text("DON'T MOVE!").decorate(TextDecoration.BOLD).color(NamedTextColor.RED);
        Component subtitle = Component.text("A warden senses you...").color(NamedTextColor.DARK_RED);

        showTitleToEntities(nearbyEntities, Title.title(title, subtitle, 0, 20, 2), Sound.ENTITY_WARDEN_LISTENING);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity entity : nearbyEntities) {
                    if (entity instanceof Player player) { // && player != getOwner()
                        playerLocations.put(player, player.getLocation());
                    }
                }
            }
        }.runTaskLater(TrimAbilities.getInstance(), 20);

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!using) {
                    runnable = null;
                    cancel();
                }

                for (Player player : playerLocations.keySet()) {
                    player.showTitle(Title.title(Component.text(""), subtitle, 0, 6, 0));

                    Location startingLoc = playerLocations.get(player);
                    Location loc = player.getLocation();

                    boolean xDiff = Math.abs(startingLoc.getX() - loc.getX()) > 0.3;
                    boolean yDiff = Math.abs(startingLoc.getY() - loc.getY()) > 0.3;
                    boolean zDiff = Math.abs(startingLoc.getZ() - loc.getZ()) > 0.3;

                    if (xDiff || yDiff || zDiff) {
                        Component movedSubtitle = Component.text("!").color(NamedTextColor.RED).decorate(TextDecoration.BOLD);
                        player.showTitle(Title.title(Component.text(""), movedSubtitle, 0, 80, 10));
                        player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 1, 1);

                        getOwner().removePotionEffect(PotionEffectType.BLINDNESS);
                        getOwner().removePotionEffect(PotionEffectType.SLOWNESS);
                        getOwner().addPotionEffect(SPEED);

                        player.addPotionEffects(List.of(BLINDNESS, SLOWNESS, GLOWING));

                        used();

                        runnable = null;
                        cancel();
                    }
                }
            }
        }.runTaskTimer(TrimAbilities.getInstance(), 20, 5);
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

    @Override
    public void internalUsed() {

    }
}
