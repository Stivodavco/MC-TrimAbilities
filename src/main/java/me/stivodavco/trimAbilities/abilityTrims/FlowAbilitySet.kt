package me.stivodavco.trimAbilities.abilityTrims

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.Ability
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.Passive
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerInputEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.util.Vector

class FlowAbilitySet(val owner: Player) : AbilitySet(owner, TrimPattern.FLOW), Ability, Passive {
    override var cooldownLeft = cooldown

    var availableJumps = MAX_EXTRA_JUMPS
    var airTime = 0
    var inAir = false

    override fun internalAbility() : Boolean {
        val lookVector = owner.location.getDirection()
        owner.velocity = owner.velocity.add(lookVector.multiply(DASH_POWER))
        return true
    }

    override fun internalDestroy() {}

    override fun internalPassive() {}

    /* EVENTS */
    @EventHandler
    fun onPlayerJumpEvent(e: PlayerJumpEvent) {
        if (e.player != owner) return

        inAir = true
        airTime = JUMP_DELAY_TICKS - 1
    }

    @EventHandler
    fun onPlayerMoveEvent(e: PlayerMoveEvent) {
        if (e.player != owner) return

        val belowBlock = e.to.clone().subtract(Location(e.to.world, 0.0, 0.1, 0.0)).block

        if (e.hasChangedBlock() && !belowBlock.isPassable) {
            availableJumps = MAX_EXTRA_JUMPS
            airTime = 0
            inAir = false
        } else if (e.hasChangedBlock()) {
            inAir = true
        }

        if (inAir) {
            airTime++
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInputEvent(e: PlayerInputEvent) {
        if (e.player != owner || owner.gameMode == GameMode.CREATIVE) return

        if (e.input.isJump && availableJumps > 0 && airTime > JUMP_DELAY_TICKS) {
            availableJumps -= 1

            owner.velocity = Vector(owner.velocity.x, JUMP_POWER, owner.velocity.z)

            owner.world.playSound(owner.location, Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1.0f, 2.0f)
            Particle.GUST.builder().location(owner.location).receivers(20).spawn()
        }
    }

    companion object {
        val MAX_EXTRA_JUMPS = TrimAbilities.getTrimsConfig().getInt("flow.passive.max-extra-jumps")

        val JUMP_POWER = TrimAbilities.getTrimsConfig().getDouble("flow.passive.jump-power")

        val DASH_POWER = TrimAbilities.getTrimsConfig().getDouble("flow.ability.dash-power")

        const val JUMP_DELAY_TICKS = 10
    }

}