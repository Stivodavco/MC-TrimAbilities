package me.stivodavco.trimAbilities.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.registry.RegistryKey;
import me.stivodavco.trimAbilities.TrimAbilities;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

public class DebugCommands {
    public static LiteralArgumentBuilder<CommandSourceStack> createSetTrimCommand() {
        return Commands.literal("equiptrim").then(Commands.argument("trimPattern", ArgumentTypes.resource(RegistryKey.TRIM_PATTERN)))
                .executes(context -> {
                    TrimPattern pattern = context.getArgument("trimPattern", TrimPattern.class);

                    TrimAbilities.getInstance().getLogger().info(String.valueOf(pattern));

                    if (pattern != null && context.getSource().getExecutor() instanceof Player executor) {
                        for (int slotNum = 36; slotNum <= 39; slotNum += 1) {
                            ItemStack item = executor.getInventory().getItem(slotNum);
                            if (item != null) {
                                ArmorMeta meta = (ArmorMeta) item.getItemMeta();
                                meta.setTrim(new ArmorTrim(TrimMaterial.DIAMOND, pattern));
                            }
                        }
                    }
                    return Command.SINGLE_SUCCESS;
        });
    }
}
