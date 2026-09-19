package me.stivodavco.trimAbilities.utilities;

import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class TrimActionbar {
    private final Player player;
    private final BukkitTask task;

    public TrimActionbar(Player plr, AbilityTrim trim) {
        TrimAbilities.getInstance().getLogger().info("Set actionbar for player " + plr.getName() + trim.toString());
        player = plr;
        task = new BukkitRunnable() {
            int ticksPassed = 0;

            @Override
            public void run() {
                ticksPassed += 5;

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
                } else if (trim.getType() == AbilityTrim.AbilityType.NONE) {
                    actionbarText = Component.text("(Passive)").color(NamedTextColor.GRAY);
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

                Component text = Component.text(trim.getName()).color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)
                        .append(Component.text(" | ").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false));

                if (trim.getType() == AbilityTrim.AbilityType.NONE) {
                    text = text.append(Component.text(trim.getAbilityName()).color(NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false).decorate(TextDecoration.ITALIC));

                } else {
                    text = text.append(Component.text(trim.getAbilityName() + ": ").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, false))
                            .append(actionbarText.decoration(TextDecoration.BOLD, false));
                }

                player.sendActionBar(text);
            }
        }.runTaskTimer(TrimAbilities.getInstance(), 0, 5);
    }

    public void stopDisplaying() {
        task.cancel();
    }
}
