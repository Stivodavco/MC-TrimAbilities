package me.stivodavco.trimAbilities.abilityTrims

import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.Ability
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.Passive
import me.stivodavco.trimAbilities.utilities.ParticleUtils
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.trim.TrimPattern


class WardAbilitySet(val owner: Player) : AbilitySet(owner, TrimPattern.WARD), Ability, Passive {
    override var cooldownLeft = cooldown

    init {
        passive()
    }

    override fun internalPassive() {
        val maxHealth = owner.getAttribute(Attribute.MAX_HEALTH)

        if (maxHealth != null && maxHealth.getModifier(MODIFIER_KEY) == null) {
            val modifier = AttributeModifier(MODIFIER_KEY, ADD_HEALTH, AttributeModifier.Operation.ADD_NUMBER)

            maxHealth.addModifier(modifier)
        }
    }

    override fun internalAbility() {
        val direction = owner.location.direction
        val endLoc = owner.location.add(direction.clone().multiply(RANGE))

        val ownerLoc = owner.location.clone()
        for (i in 1..RANGE) {
            val loc = ownerLoc.add(direction)

            TrimAbilities.getInstance().logger.info(loc.toString())

            for (entity in owner.world.getNearbyLivingEntities(loc, 1.0)) {
                if (entity != owner) {
                    entity.damage(DAMAGE)
                }
            }
        }

        val particle = Particle.SONIC_BOOM.builder().extra(0.0)
        ParticleUtils.beam(owner.location, endLoc, particle)

        owner.location.world.playSound(owner.location, Sound.ENTITY_WARDEN_SONIC_BOOM, 1f, 1f)
    }

    override fun internalDestroy() {
        val maxHealth= owner.getAttribute(Attribute.MAX_HEALTH)

        if (maxHealth?.getModifier(MODIFIER_KEY) != null) {
            maxHealth.removeModifier(MODIFIER_KEY)
        }
    }

    companion object {
        val RANGE = TrimAbilities.getTrimsConfig().getInt("ward.ability.range")

        val DAMAGE = TrimAbilities.getTrimsConfig().getDouble("ward.ability.damage")

        val MODIFIER_KEY = NamespacedKey(TrimAbilities.getInstance(), "ward_abilityset_health_modif")
        val ADD_HEALTH = TrimAbilities.getTrimsConfig().getDouble("ward.passive.add-health")
    }
}