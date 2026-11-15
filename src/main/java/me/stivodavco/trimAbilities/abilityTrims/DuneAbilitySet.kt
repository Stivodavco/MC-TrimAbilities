package me.stivodavco.trimAbilities.abilityTrims

import com.destroystokyo.paper.ParticleBuilder
import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.Passive
import me.stivodavco.trimAbilities.abstractClasses.TimeAbility
import me.stivodavco.trimAbilities.utilities.ParticleUtils
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class DuneAbilitySet(val owner: Player) : AbilitySet(owner, TrimPattern.DUNE), TimeAbility, Passive {
    override var activeTimeLeft = 0
    override var active = false
    override var cooldownLeft = cooldown

    init {
        passive()
    }

    override fun internalAbility() {
        val nearby = owner.getNearbyEntities(RANGE, RANGE, RANGE)

        for (entity in nearby) {
            if (entity is Player) {
                entity.addPotionEffects(listOf(BLINDNESS, SLOWNESS))
            }
        }

        val particle = ParticleBuilder(Particle.DUST_COLOR_TRANSITION)
            .colorTransition(Color.fromRGB(189, 169, 57), Color.fromRGB(239, 245, 76), 4f)
            .count(3)

        ParticleUtils.sphere(owner.location, RANGE, particle)

        owner.addPotionEffect(SPEED)
    }

    override fun internalDestroy() {
        owner.removePotionEffect(PotionEffectType.SATURATION)
    }

    override fun internalPassive() {
        owner.addPotionEffect(SATURATION)
    }

    companion object {
        val RANGE = TrimAbilities.getTrimsConfig().getDouble("dune.ability.range")

        val SATURATION = PotionEffect(PotionEffectType.SATURATION, -1, 255, false, false)

        val BLINDNESS = PotionEffect(PotionEffectType.BLINDNESS,
            TrimAbilities.getTrimsConfig().getInt("dune.ability.activation-time"),
            TrimAbilities.getTrimsConfig().getInt("dune.ability.blindness-amplifier"), false, false)

        val SLOWNESS = PotionEffect(PotionEffectType.BLINDNESS,
            TrimAbilities.getTrimsConfig().getInt("dune.ability.activation-time"),
            TrimAbilities.getTrimsConfig().getInt("dune.ability.slowness-amplifier"), false, false)

        val SPEED = PotionEffect(PotionEffectType.BLINDNESS,
            TrimAbilities.getTrimsConfig().getInt("dune.ability.activation-time"),
            TrimAbilities.getTrimsConfig().getInt("dune.ability.speed-amplifier"), false, false)
    }

}