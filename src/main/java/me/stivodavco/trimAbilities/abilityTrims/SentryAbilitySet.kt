package me.stivodavco.trimAbilities.abilityTrims

import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.Passive
import me.stivodavco.trimAbilities.abstractClasses.TimeAbility
import me.stivodavco.trimAbilities.utilities.RandomnessUtils
import me.stivodavco.trimAbilities.utilities.Storage
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

    var runnable: BukkitTask? = null

    val blockChance = 80
    val range = 5.0
    val arrowDelayTicks = 20

    init {
        passive()
    }

    override fun internalAbility() {
        runnable = object : BukkitRunnable() {
            override fun run() {
                if (!active) {
                    runnable = null
                    cancel()
                }

                val nearbyEntities: MutableList<Entity> = owner.getNearbyEntities(
                    range,
                    range,
                    range
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
        }.runTaskTimer(TrimAbilities.getInstance(), 0, arrowDelayTicks.toLong())
    }

    override fun internalDestroy() {
        runnable?.cancel()
    }

    /* EVENTS */
    @EventHandler
    fun onDamage(e: EntityDamageEvent) {
        if (!passiveEnabled) {
            return
        }

        val entity = e.entity
        if (entity is Player) {
            if (Storage.getAbilitySet(entity) is SentryAbilitySet) {
                if (e.damageSource.directEntity is Arrow && RandomnessUtils.chance(blockChance)) {
                    entity.world.playSound(entity.location, Sound.ITEM_SHIELD_BLOCK, 1f, 1f)
                    e.isCancelled = true
                }
            }
        }
    }

    override fun internalPassive() {}
}