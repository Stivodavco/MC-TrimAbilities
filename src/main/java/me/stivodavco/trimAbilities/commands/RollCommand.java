package me.stivodavco.trimAbilities.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.stivodavco.trimAbilities.utilities.AnimationBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

import static java.util.Map.entry;

public class RollCommand {
    private static Map<String, String> trimTitles = Map.ofEntries(
        entry("Sentry", "Archery"),
        entry("Dune", "Sand"),
        entry("Snout", "Nether Alliance"),
        entry("Ward", "Senses"),
        entry("Eye", "Teleportation"),
        entry("Bolt", "Thunder"),
        entry("Vex", "Illusions"),
        entry("Silence", "Invisibility"),
        entry("Spire", "Levitation"),
        entry("Rib", "Unbreaking"),
        entry("Wild", "Nature"),
        entry("Flow", "Mobility"),
        entry("Tide", "Water"),
        entry("Raiser", "Impact"),
        entry("Host", "Reaping"),
        entry("Wayfinder", "Warping"),
        entry("Coast", "Reaping"),
        entry("Shaper", "Earthbending")
    );

    private static NamespacedKey getRandomTrim() {
        List<NamespacedKey> keys = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN).keyStream().toList();

        int random = new Random().nextInt(keys.size());

        return keys.get(random);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> createRollTrimCommand() {
        return Commands.literal("rolltrim").then(Commands.argument("targets", ArgumentTypes.players())
                .executes(context -> {
                    List<Player> players = context.getArgument("targets", PlayerSelectorArgumentResolver.class).resolve(context.getSource());

                    for (Player player : players) {
                        Consumer<AnimationBuilder> randomTrim = anim -> {
                            NamespacedKey randKey = getRandomTrim();
                            String randTrim = StringUtils.capitalize(randKey.value());

                            Title title = Title.title(Component.text(randTrim), Component.empty());
                            player.showTitle(title);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);
                        };

                        Consumer<AnimationBuilder> end = anim -> {
                            NamespacedKey randKey = getRandomTrim();
                            String randTrim = StringUtils.capitalize(randKey.value());

                            Title title = Title.title(
                                    Component.text(randTrim).color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD),
                                    Component.text("The trim of " + trimTitles.get(randTrim)).color(NamedTextColor.GRAY)
                            , 10, 80, 10);
                            player.showTitle(title);
                            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);

//                            Material template = Registry.MATERIAL.get();
//                            TrimAbilities.getInstance().getLogger().info("Template: " + template);
//                            if (template != null) {
//                                TrimAbilities.getInstance().getLogger().info("Valid !");
//                                player.give(ItemStack.of(template));
//                            }
                        };

                        AnimationBuilder animation = new AnimationBuilder();
                        animation
                                .atRepeated(0, 2, 60, randomTrim)
                                .at(122, end)
                                .play();
                    }

                    return Command.SINGLE_SUCCESS;
                })
        );
    }
}
