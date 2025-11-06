package me.stivodavco.trimAbilities.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import me.stivodavco.trimAbilities.abilityTrims.SentryAbilityTrim;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.utilities.ArmorUtils;
import me.stivodavco.trimAbilities.utilities.RandomnessUtils;
import me.stivodavco.trimAbilities.utilities.Storage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class AbilityListener implements Listener {
    // GENERAL (FOR EVERY TRIM)
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (( e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK) ) && e.getPlayer().isSneaking()) {
            AbilityTrim trim = Storage.getAbilityTrim(e.getPlayer());
            if (trim != null && trim.isAbilityReady()) {
                trim.ability();

                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onArmorChange (PlayerArmorChangeEvent e) {
        Player player = e.getPlayer();

        AbilityTrim trim = ArmorUtils.getActiveTrim(player);

        if (trim != null) {
            if (Storage.getAbilityTrim(player) == null) {
                // Send message & play sound that the trim was equipped if it was not already equipped
                player.sendMessage(Component.text("Trim Equipped ").color(NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD)
                        .append(Component.text("You gained the abilities of the " + trim.getName() + " armor trim.").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, false))
                );
                player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 1);

                // register the trim
                trim.setOwner(player);
                Storage.setActiveTrim(player, trim);
            }
        } else {
            AbilityTrim earlierTrim = Storage.getAbilityTrim(player);
            if (earlierTrim != null) {
                // Send message & play sound that the trim was unequipped if it was already equipped
                player.sendMessage(Component.text("Trim Unequipped ").color(NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD)
                        .append(Component.text("You lost the abilities of the " + earlierTrim.getName() + " armor trim.").color(NamedTextColor.RED).decoration(TextDecoration.BOLD, false))
                );
                player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, 1);

                earlierTrim.disable();
                Storage.setActiveTrim(player, null);
                player.sendActionBar(Component.empty());
            }
        }
    }


    // SPECIFIC
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player) {
            if (Storage.getAbilityTrim(player) instanceof SentryAbilityTrim) {
                if (e.getDamageSource().getDirectEntity() instanceof Arrow && RandomnessUtils.chance(50)) {
                    player.getWorld().playSound(player.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1, 1);
                    e.setCancelled(true);
                }
            }
        }
    }
}
