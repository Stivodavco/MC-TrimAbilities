package me.stivodavco.trimAbilities;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.stivodavco.trimAbilities.commands.DebugCommands;
import me.stivodavco.trimAbilities.listeners.AbilityListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class TrimAbilities extends JavaPlugin implements Listener {
    private static JavaPlugin instance;
    private static BukkitRunnable actionbarRunnable;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new AbilityListener(), this);

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(DebugCommands.createSetTrimCommand().build());
            commands.registrar().register(DebugCommands.createReadyTrimCommand().build());
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static JavaPlugin getInstance() { return instance; }
    public static BukkitRunnable getActionbarRunnable() { return actionbarRunnable; }
}