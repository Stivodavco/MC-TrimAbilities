package me.stivodavco.trimAbilities.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.registry.RegistryKey;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.utilities.ArmorUtils;
import me.stivodavco.trimAbilities.utilities.Storage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

public class DebugCommands {
    public static LiteralArgumentBuilder<CommandSourceStack> createSetTrimCommand() {
        return Commands.literal("equiptrim").then(Commands.argument("trimPattern", ArgumentTypes.resource(RegistryKey.TRIM_PATTERN))
                .executes(context -> {
                    TrimPattern pattern = context.getArgument("trimPattern", TrimPattern.class);

                    if (context.getSource().getExecutor() instanceof Player executor) {
                        for (int slotNum = 36; slotNum <= 39; slotNum += 1) {
                            ItemStack item = executor.getInventory().getItem(slotNum);
                            if (item != null) {
                                ArmorMeta meta = (ArmorMeta) item.getItemMeta();
                                meta.setTrim(new ArmorTrim(TrimMaterial.DIAMOND, pattern));
                                item.setItemMeta(meta);
                            }
                        }
                        ArmorUtils.equipTrimIfPresent(executor);
                        AbilityTrim abilityTrim = Storage.getAbilityTrim(executor);
                        if (abilityTrim != null) {
                            abilityTrim.cooldownLeft = 0;
                        }
                    }
                    return Command.SINGLE_SUCCESS;
        }))
                ;
    }

    public static LiteralArgumentBuilder<CommandSourceStack> createReadyTrimCommand() {
        return Commands.literal("readytrim").then(Commands.argument("player", ArgumentTypes.player())
                .executes(context -> {
                    Player player = context.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(context.getSource()).getFirst();

                    AbilityTrim abilityTrim = Storage.getAbilityTrim(player);
                    if (abilityTrim != null) {
                        abilityTrim.cooldownLeft = 0;
                        player.sendMessage(Component.text(String.format("Successfully cleared the cooldown for player %s's equipped armor trim. ", player.getName())).color(NamedTextColor.GREEN));
                    } else {
                        player.sendMessage(Component.text(String.format("Failed for %s; Nonexistent player or that player has no equipped armor trim.", player.getName())).color(NamedTextColor.RED));
                    }

                    return Command.SINGLE_SUCCESS;
                }))
                ;
    }
}
