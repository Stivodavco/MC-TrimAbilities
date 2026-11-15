package me.stivodavco.trimAbilities.abilityTrims

import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.Passive
import me.stivodavco.trimAbilities.abstractClasses.TimeAbility
import me.stivodavco.trimAbilities.utilities.ParticleUtils
import me.stivodavco.trimAbilities.utilities.RandomnessUtils
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask


class WildAbilitySet(val owner: Player) : AbilitySet(owner, TrimPattern.WILD), TimeAbility, Passive {
    override var activeTimeLeft = 0
    override var active = false
    override var cooldownLeft = cooldown

    private val vinedPlrs = mutableListOf<Player>()

    private var runnable: BukkitTask? = null

    override fun internalAbility() {
        val particle = Particle.DUST.builder().data(DustOptions(Color.GREEN, 2f))
        ParticleUtils.cylinder(owner.location, RANGE, 2.0, particle)

        vinedPlrs.clear()
        for (entity in owner.getNearbyEntities(RANGE, RANGE, RANGE)) {
            if (entity is Player) {
                vinedPlrs.add(entity)

                runnable = object : BukkitRunnable() {
                    override fun run() {
                        if (!active) {
                            runnable = null
                            cancel()
                        }

                        ParticleUtils.cylinder(entity.location, 0.5, 1.0, particle)
                    }
                }.runTaskTimer(TrimAbilities.getInstance(), 1, 10)
            }
        }
    }

    override fun internalDestroy() {
        runnable?.cancel()
    }

    override fun internalPassive() {}

    /* EVENTS */
    @EventHandler(ignoreCancelled = true)
    fun onPlayerMove(e: PlayerMoveEvent) {
        if (active && vinedPlrs.contains(e.getPlayer())) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        val entity = e.entity
        if (e.damager == owner && RandomnessUtils.chance(POISON_CHANCE) && entity is LivingEntity && !entity.hasPotionEffect(PotionEffectType.POISON)) {
            entity.addPotionEffect(POISON)
        }
    }

    companion object {
        val RANGE = TrimAbilities.getTrimsConfig().getDouble("wild.ability.range")

        val POISON_CHANCE = TrimAbilities.getTrimsConfig().getInt("wild.passive.poison-chance")

        val POISON = PotionEffect(PotionEffectType.POISON,
            TrimAbilities.getTrimsConfig().getInt("wild.passive.poison-duration-ticks"),
            TrimAbilities.getTrimsConfig().getInt("wild.passive.poison-amplifier"),
            false, true)
    }

}