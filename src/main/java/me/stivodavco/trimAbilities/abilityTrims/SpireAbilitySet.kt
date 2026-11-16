package me.stivodavco.trimAbilities.abilityTrims

import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.Passive
import me.stivodavco.trimAbilities.abstractClasses.TimeAbility
import me.stivodavco.trimAbilities.utilities.ParticleUtils
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector


class SpireAbilitySet(val owner: Player) : AbilitySet(owner, TrimPattern.SPIRE), TimeAbility, Passive {
    override var activeTimeLeft = 0
    override var active = false
    override var cooldownLeft = cooldown

    override fun internalAbility() : Boolean {
        for (entity in owner.getNearbyEntities(RANGE, RANGE, RANGE)) {
            if (entity is Player) {
                entity.addPotionEffect(LEVITATION)
            }
        }

        val particle = Particle.END_ROD.builder().extra(0.0)

        ParticleUtils.cylinder(owner.location, RANGE, 3.0, particle)
        return true
    }

    override fun internalDestroy() {}

    override fun internalPassive() {}

    /* EVENTS */
    @EventHandler
    fun onEntityDamage(e: EntityDamageEvent) {
        if (e.getEntity() == owner && e.cause == EntityDamageEvent.DamageCause.FALL) {
            e.isCancelled = true

            val particle = Particle.POOF.builder().extra(0.2).count(15).location(owner.location).receivers(50)
            particle.spawn()

            owner.location.world.playSound(owner.location, Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1.0f, 2.0f)
            owner.velocity = Vector(0.0, 0.6, 0.0)
        }
    }

    companion object {
        val RANGE = TrimAbilities.getTrimsConfig().getDouble("spire.ability.range")

        val LEVITATION = PotionEffect(PotionEffectType.LEVITATION, TrimAbilities.getTrimsConfig().getInt("spire.ability.activation-time") * 20, 1, false, true)
    }

}