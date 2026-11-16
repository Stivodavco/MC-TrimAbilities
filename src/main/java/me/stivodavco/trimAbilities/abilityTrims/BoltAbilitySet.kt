package me.stivodavco.trimAbilities.abilityTrims

import com.destroystokyo.paper.ParticleBuilder
import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.Ability
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.Passive
import me.stivodavco.trimAbilities.utilities.ParticleUtils
import me.stivodavco.trimAbilities.utilities.RandomnessUtils
import org.bukkit.Particle
import org.bukkit.entity.LightningStrike
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.meta.trim.TrimPattern


class BoltAbilitySet(val owner: Player) : AbilitySet(owner, TrimPattern.BOLT), Ability, Passive {
    override var cooldownLeft = cooldown

    override fun internalAbility() : Boolean {
        for (entity in owner.getNearbyEntities(RANGE, RANGE, RANGE)) {
            if (entity is LivingEntity) {
                entity.world.spawn(entity.location, LightningStrike::class.java)
            }
        }

        val particle = ParticleBuilder(Particle.GLOW_SQUID_INK).extra(0.0)
        ParticleUtils.circle(owner.location, RANGE, particle)
        return true
    }

    override fun internalDestroy() {
    }

    override fun internalPassive() {}

    /* EVENTS */
    @EventHandler
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        if (e.damager == owner && RandomnessUtils.chance(LIGHTNING_CHANCE)) {
            e.entity.world.spawn(e.entity.location, LightningStrike::class.java)
        }
    }

    companion object {
        val RANGE = TrimAbilities.getTrimsConfig().getDouble("bolt.ability.range")
        val LIGHTNING_CHANCE = TrimAbilities.getTrimsConfig().getInt("bolt.passive.lightning-chance")
    }
}