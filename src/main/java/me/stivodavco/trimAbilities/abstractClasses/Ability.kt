package me.stivodavco.trimAbilities.abstractClasses

import me.stivodavco.trimAbilities.utilities.TrimRegistry
import org.bukkit.inventory.meta.trim.TrimPattern

interface Ability {
    val abilityEnabled: Boolean
        get() = TrimRegistry.getTrimKey(pattern)?.abilityInfo?.enabled ?: false

    val cooldown: Int
        get() = TrimRegistry.getTrimKey(pattern)?.abilityInfo?.cooldown ?: 0

    val pattern: TrimPattern
    var cooldownLeft: Int

    fun reset() {
        cooldownLeft = cooldown
    }

    fun ability(): Boolean {
        if (!abilityEnabled) { return false }

        if (cooldownLeft <= 0) {
            internalAbility()
            reset()
            return true
        }
        return false
    }

    fun tick() {
        if (cooldownLeft > 0) {
            cooldownLeft--
        }
    }

    fun internalAbility()
}