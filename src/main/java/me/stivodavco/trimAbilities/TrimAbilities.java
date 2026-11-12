package me.stivodavco.trimAbilities;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.stivodavco.trimAbilities.commands.DebugCommands;
import me.stivodavco.trimAbilities.commands.RollCommand;
import me.stivodavco.trimAbilities.listeners.TrimListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class TrimAbilities extends JavaPlugin implements Listener {
    private static JavaPlugin instance;
    private static YamlConfiguration trimsConfig;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new TrimListener(), this);

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(DebugCommands.createSetTrimCommand().build());
            commands.registrar().register(DebugCommands.createReadyTrimCommand().build());
            commands.registrar().register(RollCommand.createRollTrimCommand().build());
        });

        saveResource("trims.yml", false);
        trimsConfig = YamlConfiguration.loadConfiguration(new File(TrimAbilities.getInstance().getDataFolder(), "trims.yml"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static JavaPlugin getInstance() { return instance; }
    public static YamlConfiguration getTrimsConfig() { return trimsConfig; }
}