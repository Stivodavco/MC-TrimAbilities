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
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType


class HostAbilitySet(val owner: Player) : AbilitySet(owner, TrimPattern.HOST), TimeAbility, Passive {
    override var activeTimeLeft = 0
    override var active = false
    override var cooldownLeft = cooldown

    override fun internalAbility() : Boolean {
        var amplifier = -1

        val particle = Particle.SCRAPE.builder().count(2).extra(0.0)

        for (entity in owner.getNearbyEntities(RANGE, RANGE, RANGE)) {
            if (entity is Player) {
                amplifier++
                entity.addPotionEffect(WEAKNESS)

                ParticleUtils.beam(
                    owner.location.add(0.0, 1.0, 0.0),
                    entity.location.add(0.0, 1.0, 0.0),
                    particle
                )
            }
        }

        if (amplifier >= 0) {
            val strength = PotionEffect(PotionEffectType.STRENGTH, activationTime, amplifier)
            owner.addPotionEffect(strength)
        }

        ParticleUtils.sphere(owner.location, RANGE, particle)
        return true
    }

    override fun internalDestroy() {}

    override fun internalPassive() {}

    /* EVENTS */
    @EventHandler
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        val attacker = e.damager
        val victim = e.entity

        if (attacker == owner && victim is Player) {
            owner.heal(e.getFinalDamage() * (HEAL_PERCENT / 100))

            val particle = Particle.ELECTRIC_SPARK.builder().extra(0.0)
            ParticleUtils.beam(owner.location.clone().add(0.0, 1.0, 0.0), victim.location.clone().add(0.0, 1.0, 0.0), particle)

            owner.world.playSound(owner.location, Sound.ENTITY_GUARDIAN_HURT, 1.0f, 2.0f)
        }
    }

    companion object {
        val RANGE = TrimAbilities.getTrimsConfig().getDouble("host.ability.range")

        val WEAKNESS = PotionEffect(PotionEffectType.WEAKNESS,
            TrimAbilities.getTrimsConfig().getInt("host.ability.activation-time"),
            TrimAbilities.getTrimsConfig().getInt("host.ability.weakness-amplifier"))

        val HEAL_PERCENT = TrimAbilities.getTrimsConfig().getInt("host.passive.heal-percent")
    }
}