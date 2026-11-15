package me.stivodavco.trimAbilities.abilityTrims

import com.destroystokyo.paper.ParticleBuilder
import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.Ability
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.Passive
import me.stivodavco.trimAbilities.utilities.ParticleUtils
import me.stivodavco.trimAbilities.utilities.RandomnessUtils
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.EvokerFangs
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.meta.trim.TrimPattern


class VexAbilitySet(val owner: Player) : AbilitySet(owner, TrimPattern.VEX), Ability, Passive {
    override var cooldownLeft = cooldown

    override fun internalAbility() {
        for (entity in owner.getNearbyEntities(RANGE, RANGE, RANGE)) {
            if (entity is LivingEntity) {
                spawnFangsAtLocation(entity.location)
                entity.damage(EXTRA_DMG.toDouble(), DamageSource.builder(DamageType.MAGIC).withDirectEntity(owner).build())
            }
        }

        val particle = ParticleBuilder(Particle.END_ROD).extra(0.0)

        ParticleUtils.circle(owner.location, RANGE, particle)
    }

    override fun internalDestroy() {
    }

    override fun internalPassive() {}

    /* HELPER FUNCTIONS */
    private fun spawnFangsAtLocation(location: Location) {
        val world = location.world
        val locOffsets = listOf(
            Location(world, 1.0, 0.0, 0.0),
            Location(world, 1.0, 0.0, -1.0),
            Location(world, 1.0, 0.0, 1.0),
            Location(world, 0.0, 0.0, 1.0),
            Location(world, 0.0, 0.0, -1.0),
            Location(world, 0.0, 0.0, 0.0),
            Location(world, -1.0, 0.0, 0.0),
            Location(world, -1.0, 0.0, 1.0),
            Location(world, -1.0, 0.0, -1.0)
        )

        for (offset in locOffsets) {
            val targetLoc: Location = location.clone().add(offset)
            world.spawn(targetLoc, EvokerFangs::class.java)
        }
    }

    /* EVENTS */
    @EventHandler(ignoreCancelled = true)
    fun onPlayerDeath(e: PlayerDeathEvent) {
        if (e.entity == owner && RandomnessUtils.chance(REVIVE_CHANCE)) {
            e.isCancelled = true

            val maxHealth = owner.getAttribute(Attribute.MAX_HEALTH)!!.baseValue
            e.setReviveHealth(Math.clamp(HEAL_HEALTH.toDouble(), 1.0, maxHealth))

            val particle = ParticleBuilder(Particle.TOTEM_OF_UNDYING)
            ParticleUtils.cylinder(owner.location, 0.5, 2.0, particle)

            owner.world.playSound(owner.location, Sound.ITEM_TOTEM_USE, 1.0f, 1.0f)
        }
    }

    companion object {
        val REVIVE_CHANCE = TrimAbilities.getTrimsConfig().getInt("vex.passive.revive-chance")
        val HEAL_HEALTH = TrimAbilities.getTrimsConfig().getInt("vex.passive.heal-health")

        val RANGE = TrimAbilities.getTrimsConfig().getDouble("vex.ability.range")
        val EXTRA_DMG = TrimAbilities.getTrimsConfig().getInt("vex.ability.extra-damage")
    }
}