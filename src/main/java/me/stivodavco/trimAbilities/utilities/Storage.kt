package me.stivodavco.trimAbilities.utilities

import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import org.bukkit.entity.Player

object Storage {
    private val activeSet = mutableMapOf<Player, AbilitySet>()

    fun getAbilitySet(player: Player): AbilitySet? {
        return activeSet[player]
    }

    fun setAbilitySet(player: Player, set: AbilitySet?) {
        if (set != null) {
            activeSet[player] = set
        } else {
            val prevSet: AbilitySet? = getAbilitySet(player)

            prevSet?.destroy()

            activeSet.remove(player)
        }
    }
}