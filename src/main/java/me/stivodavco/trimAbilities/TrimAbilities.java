package me.stivodavco.trimAbilities;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.commands.DebugCommands;
import me.stivodavco.trimAbilities.listeners.AbilityListener;
import me.stivodavco.trimAbilities.utilities.Storage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public final class TrimAbilities extends JavaPlugin implements Listener {
    private static JavaPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new AbilityListener(), this);

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(DebugCommands.createSetTrimCommand().build());
            commands.registrar().register(DebugCommands.createReadyTrimCommand().build());
        });

        // PASSIVE RUNNABLES
        new BukkitRunnable() {
            int ticksPassed = 0;

            @Override
            public void run() {
                ticksPassed += 5;

                HashMap<Player, AbilityTrim> activeTrims = Storage.getActiveTrims();
                if (activeTrims == null) {
                    return;
                }
                for (Player player : activeTrims.keySet()) {
                    AbilityTrim trim = activeTrims.get(player);

                    if (ticksPassed >= 20) {
                        trim.subtractCooldown();
                        trim.subtractTime();
                        ticksPassed = 0;
                    }

                    // ACTIONBAR
                    Component actionbarText;

                    if (trim.isActive()) {
                        int total = trim.getActiveTimeLeft();
                        int minutes = Math.floorDiv(total, 60);
                        int seconds = total - minutes * 60;

                        actionbarText = Component.text("[ACTIVE FOR " + minutes + "m " + seconds + "s]").color(NamedTextColor.DARK_AQUA);
                    } else if (trim.isUsing()){
                        if (trim.getType() == AbilityTrim.AbilityType.UNTIL_USED) {
                            actionbarText = Component.text("[WAITING UNTIL USED]").color(NamedTextColor.AQUA);
                        } else {
                            int total = trim.getUsingTimeLeft();
                            int minutes = Math.floorDiv(total, 60);
                            int seconds = total - minutes * 60;

                            actionbarText = Component.text("[WAITING UNTIL USED " + minutes + "m " + seconds + "s]").color(NamedTextColor.AQUA);
                        }
                    } else {
                        if (trim.isAbilityReady()) {
                            actionbarText = Component.text("[READY!]").color(NamedTextColor.GREEN);
                        } else {
                            int total = trim.getCooldown();
                            int minutes = Math.floorDiv(total, 60);
                            int seconds = total - minutes * 60;

                            actionbarText = Component.text("[On Cooldown] " + minutes + "m " + seconds + "s").color(NamedTextColor.RED);
                        }
                    }

                    player.sendActionBar(Component.text(trim.getName()).color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)
                            .append(Component.text(" | ").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false))
                            .append(Component.text(trim.getAbilityName() + ": ").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, false))
                            .append(actionbarText.decoration(TextDecoration.BOLD, false))
                    );
                }
            }
        }.runTaskTimer(this, 0, 5);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static JavaPlugin getInstance() {
        return instance;
    }
}
