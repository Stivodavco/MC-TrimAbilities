package me.stivodavco.trimAbilities.abstractClasses

import me.stivodavco.trimAbilities.TrimAbilities
import org.bukkit.scheduler.BukkitRunnable

class AbilityTicker(val periodTicks: Long, val abilitySet: AbilitySet) {
    val ticker = object : BukkitRunnable() {
        override fun run() {
            if (abilitySet is Ability) {
                abilitySet.tick()
            }
            abilitySet.actionbar.display()
        }
    }.runTaskTimer(TrimAbilities.getInstance(), 0, periodTicks)

    fun disable() {
        ticker.cancel()
        abilitySet.actionbar.disable()
    }
}