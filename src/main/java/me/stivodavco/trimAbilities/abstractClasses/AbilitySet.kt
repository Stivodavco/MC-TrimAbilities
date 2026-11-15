package me.stivodavco.trimAbilities.abstractClasses

import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.utilities.AbilityActionbar
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.inventory.meta.trim.TrimPattern

abstract class AbilitySet(owner: Player, val pattern: TrimPattern) : Listener {
    val actionbar = AbilityActionbar(owner, this)
    val ticker = AbilityTicker(20, this)

    init {
        if (this is Passive && passiveEnabled) {
            Bukkit.getPluginManager().registerEvents(this, TrimAbilities.getInstance())
        }
    }

    fun destroy() {
        ticker.disable()
        internalDestroy()
        HandlerList.unregisterAll(this)
    }

    abstract fun internalDestroy()
}