package me.stivodavco.trimAbilities.abilityTrims

import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.Passive
import me.stivodavco.trimAbilities.abstractClasses.TimeAbility
import me.stivodavco.trimAbilities.utilities.ParticleUtils
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask


class CoastAbilitySet(val owner: Player) : AbilitySet(owner, TrimPattern.COAST), TimeAbility, Passive {
    override var activeTimeLeft = 0
    override var active = false
    override var cooldownLeft = cooldown

    private var runnable: BukkitTask? = null

    init {
        passive()
    }

    override fun internalPassive() {
        owner.addPotionEffects(listOf(WATER_BREATHING, DOLPHINS_GRACE))
    }

    override fun internalAbility() : Boolean {
        runnable = object : BukkitRunnable() {
            override fun run() {
                if (!active) {
                    cancel()
                    runnable = null
                }

                val entity = owner.getTargetEntity(RANGE)

                if (entity is LivingEntity) {
                    if (entity.activeItem.type != Material.SHIELD) {
                        entity.damage(DAMAGE)
                    }

                    val targetLoc = entity.eyeLocation

                    val particle = Particle.GLOW_SQUID_INK.builder().extra(0.0)
                    ParticleUtils.beam(owner.eyeLocation, targetLoc, particle)

                    owner.world.playSound(entity.location, Sound.ENTITY_ELDER_GUARDIAN_HURT, 1.0f, 2.0f)
                }
                owner.world.playSound(owner.location, Sound.ENTITY_GUARDIAN_ATTACK, 1.0f, 2.0f)
            }
        }.runTaskTimer(TrimAbilities.getInstance(), 0, 5)
        return true
    }

    override fun internalDestroy() {
        owner.removePotionEffect(PotionEffectType.WATER_BREATHING);
        owner.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);

        runnable?.cancel()
    }

    companion object {
        val RANGE = TrimAbilities.getTrimsConfig().getInt("coast.ability.range")
        val DAMAGE = TrimAbilities.getTrimsConfig().getDouble("coast.ability.damage")

        val WATER_BREATHING = PotionEffect(PotionEffectType.WATER_BREATHING, -1, 255, true, false, false)

        val DOLPHINS_GRACE = PotionEffect(PotionEffectType.DOLPHINS_GRACE, -1,
            TrimAbilities.getTrimsConfig().getInt("coast.passive.dolphins-grace-amplifier"), true, false, false)
    }
}