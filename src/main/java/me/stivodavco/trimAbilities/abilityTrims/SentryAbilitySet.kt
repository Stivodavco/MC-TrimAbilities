package me.stivodavco.trimAbilities.abilityTrims

import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.TimeAbility
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class SentryAbilitySet(owner: Player) : AbilitySet(owner), TimeAbility {
    override var activeTimeLeft = 0
    override var active = false
    override val pattern = TrimPattern.SENTRY
    override var cooldownLeft = 0

    var runnable: BukkitTask? = null

    val range = 5.0
    val arrowDelayTicks = 20

    override var enabled = true

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
}