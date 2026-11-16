package me.stivodavco.trimAbilities.abilityTrims

import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.Ability
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.Passive
import me.stivodavco.trimAbilities.utilities.ParticleUtils
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class EyeAbilitySet(val owner: Player) : AbilitySet(owner, TrimPattern.EYE), Ability, Passive {
    override var cooldownLeft = cooldown

    override fun internalAbility() : Boolean {
        val startLoc = owner.location

        val targetBlock = owner.getTargetBlockExact(MAX_TELEPORT_DIST)
        if (targetBlock != null) {
            val teleportLoc = targetBlock.location
            teleportLoc.rotation = owner.location.rotation
            owner.teleport(teleportLoc)

            val particle = Particle.DUST.builder()
                .data(DustOptions(Color.fromRGB(255, 0, 255), 4f))
                .extra(0.0)

            ParticleUtils.beam(startLoc, owner.location, particle)

            for (loc in listOf(startLoc, owner.location)) {
                owner.world.playSound(loc, Sound.ENTITY_PLAYER_TELEPORT, 1f, 0f)
            }

            owner.addPotionEffect(GLOWING)
            return true
        } else {
            owner.playSound(owner.location, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f)
            return false
        }
    }

    override fun internalDestroy() {}

    override fun internalPassive() {}

    companion object {
        val MAX_TELEPORT_DIST = TrimAbilities.getTrimsConfig().getInt("eye.ability.max-teleport-distance")

        val GLOWING = PotionEffect(PotionEffectType.GLOWING,
            TrimAbilities.getTrimsConfig().getInt("eye.ability.glow-duration-ticks"), 255, true, false)
    }

}