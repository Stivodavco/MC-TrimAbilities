package me.stivodavco.trimAbilities.abilityTrims

import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.Passive
import me.stivodavco.trimAbilities.utilities.ParticleUtils
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask


class SilenceAbilitySet(val owner: Player) : AbilitySet(owner, TrimPattern.SILENCE), Passive {
    private var hidden = false
    private var runnable: BukkitTask? = null

    init {
        passive()
    }

    override fun internalPassive() {
        setHidden(true)

        owner.addPotionEffect(INVIS)

        runnable = object : BukkitRunnable() {
            override fun run() {
                var found = false
                for (entity in owner.getNearbyEntities(APPEAR_RANGE, APPEAR_RANGE, APPEAR_RANGE)) {
                    if (entity is Player) {
                        found = true
                        break
                    }
                }

                if (found) {
                    setHidden(false)
                    owner.addPotionEffect(GLOWING)
                } else {
                    setHidden(true)
                    owner.removePotionEffect(PotionEffectType.GLOWING)
                }
            }
        }.runTaskTimer(TrimAbilities.getInstance(), 1, 5)
    }

    override fun internalDestroy() {
        runnable?.cancel()
        setHidden(false);
        owner.removePotionEffect(PotionEffectType.GLOWING);
        owner.removePotionEffect(PotionEffectType.INVISIBILITY);
    }

    /* HELPER FUNCTIONS */
    private fun setHidden(hide: Boolean) {
        var pitch = if (hide) 2f else 0.5f
        val plugin = TrimAbilities.getInstance()

        for (player in Bukkit.getServer().onlinePlayers) {
            if (player == owner) continue

            if (hide) {
                player.hidePlayer(plugin, owner)
            } else {
                player.showPlayer(plugin, owner)
            }
        }

        if (hide != hidden) {
            owner.world.playSound(owner.location, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, pitch)

            val particle = Particle.POOF.builder().extra(0.0)
            ParticleUtils.cylinder(owner.location, 0.5, 2.0, particle)
        }
        hidden = hide
    }

    companion object {
        val INVIS = PotionEffect(PotionEffectType.INVISIBILITY, -1, 255, true, false, false)
        val GLOWING = PotionEffect(PotionEffectType.GLOWING, -1, 255, true, false, true)

        val APPEAR_RANGE = TrimAbilities.getTrimsConfig().getDouble("silence.passive.appear-range")
    }
}