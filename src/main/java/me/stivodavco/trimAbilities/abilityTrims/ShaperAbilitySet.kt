package me.stivodavco.trimAbilities.abilityTrims

import com.destroystokyo.paper.ParticleBuilder
import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.Passive
import me.stivodavco.trimAbilities.abstractClasses.TimeAbility
import me.stivodavco.trimAbilities.utilities.ParticleUtils
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.block.data.type.Bed
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.BoundingBox
import org.checkerframework.checker.units.qual.radians


class ShaperAbilitySet(val owner: Player) : AbilitySet(owner, TrimPattern.SHAPER), TimeAbility, Passive {
    override var activeTimeLeft = 0
    override var active = false
    override var cooldownLeft = cooldown

    var runnable: BukkitTask? = null

    init {
        passive()
    }

    override fun internalPassive() {
        owner.addPotionEffect(HASTE);
    }

    override fun internalAbility() : Boolean {
        val boundingBox = BoundingBox.of(owner.location, RANGE, RANGE, RANGE)
        owner.allowFlight = true

        runnable = object : BukkitRunnable() {
            override fun run() {
                if (!active || !owner.boundingBox.overlaps(boundingBox)) {
                    active = false
                    owner.allowFlight = false
                    runnable = null
                    cancel()
                }

                val particle = Particle.CLOUD.builder().extra(0.0)

                ParticleUtils.cube(
                    boundingBox.min.toLocation(owner.world),
                    boundingBox.max.toLocation(owner.world),
                    particle
                )
            }
        }.runTaskTimer(TrimAbilities.getInstance(), 0, 5)

        return true
    }

    override fun internalDestroy() {
        owner.removePotionEffect(PotionEffectType.HASTE)
        runnable?.cancel()
        owner.allowFlight = false
    }

    companion object {
        val RANGE = TrimAbilities.getTrimsConfig().getDouble("shaper.ability.range")

        val HASTE = PotionEffect(PotionEffectType.HASTE, -1,
            TrimAbilities.getTrimsConfig().getInt("shaper.passive.haste-amplifier"), true, false, false)
    }
}