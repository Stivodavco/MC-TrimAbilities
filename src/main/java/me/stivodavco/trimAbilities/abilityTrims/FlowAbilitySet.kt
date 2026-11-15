package me.stivodavco.trimAbilities.abilityTrims

import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.Ability
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.Passive
import org.bukkit.*
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
    var offGround = false
    var jumpInput = false

    override fun internalAbility() {
        val lookVector = owner.location.getDirection()
        owner.velocity = owner.velocity.add(lookVector.multiply(DASH_POWER))
    }

    override fun internalDestroy() {}

    override fun internalPassive() {}

    /* EVENTS */
    @EventHandler
    fun onPlayerMoveEvent(e: PlayerMoveEvent) {
        if (e.player != owner) return

        val belowBlock = e.to.clone().subtract(Location(e.to.world, 0.0, 1.0, 0.0)).block

        if (e.hasChangedBlock() && belowBlock.type != Material.AIR) {
            offGround = false
            availableJumps = MAX_EXTRA_JUMPS
        } else if (e.hasChangedBlock()) {
            offGround = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInputEvent(e: PlayerInputEvent) {
        if (e.player != owner || owner.gameMode == GameMode.CREATIVE) return

        if (!e.input.isJump) jumpInput = false

        if (e.input.isJump && !jumpInput  && offGround && availableJumps > 0 ) {
            jumpInput = true
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
    }

}