package me.stivodavco.trimAbilities.abilityTrims;

import com.destroystokyo.paper.ParticleBuilder;
import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.utilities.ParticleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SilenceAbilityTrim extends AbilityTrim {
    public static final PotionEffect INVIS = new PotionEffect(PotionEffectType.INVISIBILITY, -1, 255, true, false);
    public static final PotionEffect GLOWING = new PotionEffect(PotionEffectType.GLOWING, -1, 255, true, false);
    public static final int APPEAR_RANGE = 10;

    private boolean equipped = true;
    private boolean hidden = false;
    private BukkitTask runnable;

    public SilenceAbilityTrim(Player newOwner) {
        super(newOwner, TrimPattern.SILENCE, AbilityType.NONE);
    }

    private void setHidden(boolean hide) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player == getOwner()) {
                continue;
            }

            JavaPlugin plugin = TrimAbilities.getInstance();

            float pitch = 1;

            if (hide) {
                player.hidePlayer(plugin, getOwner());
                pitch = 2;
            } else {
                player.showPlayer(plugin, getOwner());
                pitch = 0.5f;
            }

            if (hide != hidden) {
                getOwner().getWorld().playSound(getOwner().getLocation(), Sound.ENTITY_PLAYER_TELEPORT, 1, pitch);

                ParticleBuilder particle = Particle.POOF.builder().extra(0);
                ParticleUtils.cylinder(getOwner().getLocation(), 0.5, 2, particle);
            }
            hidden = hide;
        }
    }

    @Override
    public void enable() {
        setHidden(true);

        getOwner().addPotionEffect(INVIS);

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                boolean found = false;
                for (Entity entity : getOwner().getNearbyEntities(APPEAR_RANGE, APPEAR_RANGE, APPEAR_RANGE)) {
                    if (entity instanceof Player) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    setHidden(false);
                    getOwner().addPotionEffect(GLOWING);
                } else {
                    setHidden(true);
                    getOwner().removePotionEffect(PotionEffectType.GLOWING);
                }
            }
        }.runTaskTimer(TrimAbilities.getInstance(), 1, 5);
    }

    @Override
    public void customAbility() {}

    @Override
    public void internalDisable() {
        if (runnable != null) {
            runnable.cancel();
        }
        equipped = false;
        setHidden(false);
        getOwner().removePotionEffect(PotionEffectType.GLOWING);
        getOwner().removePotionEffect(PotionEffectType.INVISIBILITY);
    }

    @Override
    public void internalUsed() {

    }

//    /* EVENTS */
//    @EventHandler(priority = EventPriority.LOWEST)
//    public void onPlayerDeath(PlayerDeathEvent e) {
//        if (e.getDamageSource().getCausingEntity() == getOwner()) {
//            if (e.deathMessage() != null) {
//                Component censored = e.deathMessage().replaceText(TextReplacementConfig.builder()
//                        .matchLiteral(e.getPlayer().getName())
//                        .replacement(Component.text("XXXXXXXX").decorate(TextDecoration.OBFUSCATED))
//                        .build()
//                );
//
//                e.deathMessage(censored);
//            }
//        }
//    }
}
