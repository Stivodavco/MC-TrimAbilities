package me.stivodavco.trimAbilities.abilityTrims

import io.papermc.paper.event.entity.EntityKnockbackEvent
import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.Ability
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.Passive
import me.stivodavco.trimAbilities.utilities.ParticleUtils
import org.bukkit.*
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.util.Vector

class RaiserAbilitySet(val owner: Player) : AbilitySet(owner, TrimPattern.RAISER), Ability, Passive {
    override var cooldownLeft = cooldown

    override fun internalAbility() {
        for (entity in owner.getNearbyEntities(RANGE, RANGE, RANGE)) {
            if (entity is LivingEntity) {
                entity.damage(DAMAGE, DamageSource.builder(DamageType.MACE_SMASH).withDirectEntity(owner).build())
                entity.velocity = entity.velocity.add(Vector(0.0, KNOCKBACK_POWER, 0.0))
            }
        }

        val particle = Particle.BLOCK.builder()
            .count(4)
            .data(owner.location.clone().subtract(0.0, 1.0, 0.0).block.type.createBlockData())
            .extra(0.0)

        ParticleUtils.cylinder(owner.location, RANGE, 2.0, particle)

        owner.world.playSound(owner.location, Sound.ITEM_MACE_SMASH_GROUND, 1.0f, 0.0f)
    }

    override fun internalDestroy() {}

    override fun internalPassive() {}

    /* EVENTS */
    @EventHandler(ignoreCancelled = true)
    fun onEntityKnockback(e: EntityKnockbackEvent) {
        if (e.entity == owner) {
            e.isCancelled = true
        }
    }

    companion object {
        val RANGE = TrimAbilities.getTrimsConfig().getDouble("raiser.ability.range")

        val KNOCKBACK_POWER = TrimAbilities.getTrimsConfig().getDouble("raiser.ability.knockback-power")

        val DAMAGE = TrimAbilities.getTrimsConfig().getDouble("raiser.ability.damage")
    }

}