package me.stivodavco.trimAbilities.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.stivodavco.trimAbilities.utilities.AnimationBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.List;

public class RollCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> createRollTrimCommand() {
        return Commands.literal("rolltrim").then(Commands.argument("targets", ArgumentTypes.players())
                .executes(context -> {
                    List<Player> players = context.getArgument("targets", PlayerSelectorArgumentResolver.class).resolve(context.getSource());

                    return Command.SINGLE_SUCCESS;
                })
        );
    }
}
