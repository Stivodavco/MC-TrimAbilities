package me.stivodavco.trimAbilities.abilityTrims

import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.Passive
import me.stivodavco.trimAbilities.abstractClasses.TimeAbility
import me.stivodavco.trimAbilities.extensions.abilitySet
import me.stivodavco.trimAbilities.utilities.RandomnessUtils
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class SentryAbilitySet(val owner: Player) : AbilitySet(owner, TrimPattern.SENTRY), TimeAbility, Passive {
    override var activeTimeLeft = 0
    override var active = false
    override var cooldownLeft = cooldown

    private var runnable: BukkitTask? = null

    override fun internalAbility() {
        runnable = object : BukkitRunnable() {
            override fun run() {
                if (!active) {
                    runnable = null
                    cancel()
                }

                val nearbyEntities: MutableList<Entity> = owner.getNearbyEntities(
                    RANGE,
                    RANGE,
                    RANGE
                )

                for (entity in nearbyEntities) {
                    if (entity != owner && entity is LivingEntity) {
                        // launch an arrow
                        val direction = entity.location.toVector()
                            .subtract(owner.location.add(0.0, 1.0, 0.0).toVector())
                        val arrow: AbstractArrow =
                            owner.launchProjectile(AbstractArrow::class.java, direction)

                        arrow.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED

                        return
                    }
                }
            }
        }.runTaskTimer(TrimAbilities.getInstance(), 0, ARROW_DELAY_TICKS.toLong())
    }

    override fun internalDestroy() {
        runnable?.cancel()
    }

    override fun internalPassive() {}

    /* EVENTS */
    @EventHandler
    fun onDamage(e: EntityDamageEvent) {
        val entity = e.entity
        if (entity is Player) {
            if (entity.abilitySet is SentryAbilitySet) {
                if (e.damageSource.directEntity is Arrow && RandomnessUtils.chance(BLOCK_CHANCE)) {
                    entity.world.playSound(entity.location, Sound.ITEM_SHIELD_BLOCK, 1f, 1f)
                    e.isCancelled = true
                }
            }
        }
    }

    companion object {
        val BLOCK_CHANCE = TrimAbilities.getTrimsConfig().getInt("sentry.passive.block-chance-percent")
        val RANGE = TrimAbilities.getTrimsConfig().getDouble("sentry.ability.range")
        val ARROW_DELAY_TICKS = TrimAbilities.getTrimsConfig().getInt("sentry.ability.arrow-delay-ticks")
    }
}