package me.stivodavco.trimAbilities.listeners

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import me.stivodavco.trimAbilities.abstractClasses.Ability
import me.stivodavco.trimAbilities.extensions.abilitySet
import me.stivodavco.trimAbilities.extensions.equipSetIfPresent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class TrimListener : Listener {
    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        if ((e.action == Action.RIGHT_CLICK_AIR || e.action == Action.RIGHT_CLICK_BLOCK) && e.player.isSneaking) {
            val set = e.player.abilitySet

            if (set is Ability && set.cooldownLeft == 0 && set.abilityEnabled) {
                set.ability()
                e.setCancelled(true)
            }
        }
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        e.player.equipSetIfPresent()
    }

    @EventHandler
    fun onLeave(e: PlayerQuitEvent) {
        e.player.abilitySet = null
    }

    @EventHandler
    fun onArmorChange(e: PlayerArmorChangeEvent) {
        e.player.equipSetIfPresent()
    }
}