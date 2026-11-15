package me.stivodavco.trimAbilities.abilityTrims

import com.destroystokyo.paper.event.block.AnvilDamagedEvent
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.Passive
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.inventory.meta.trim.TrimPattern

class RibAbilitySet(val owner: Player) : AbilitySet(owner, TrimPattern.RIB), Passive {
    /* EVENTS */
    @EventHandler(ignoreCancelled = true)
    fun onPlayerItemDamage(e: PlayerItemDamageEvent) {
        if (e.player == owner) {
            e.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onAnvilDamagedEvent(e: AnvilDamagedEvent) {
        if (owner in e.viewers) {
            e.isCancelled = true
        }
    }

    override fun internalDestroy() {}

    override fun internalPassive() {}
}