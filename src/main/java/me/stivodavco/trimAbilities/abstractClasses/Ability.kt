package me.stivodavco.trimAbilities.abstractClasses

import me.stivodavco.trimAbilities.utilities.TrimRegistry
import org.bukkit.inventory.meta.trim.TrimPattern

interface Ability : Disableable {
    val cooldown: Int
        get() = disableWhenNull(TrimRegistry.getTrimKey(pattern)?.abilityInfo()?.cooldown(), 0)

    val pattern: TrimPattern
    var cooldownLeft: Int

    fun reset() {
        cooldownLeft = cooldown
    }

    fun ability(): Boolean {
        if (!enabled) { return false }

        if (cooldownLeft <= 0) {
            internalAbility()
            reset()
        }
        return true
    }

    fun tick() {
        if (cooldownLeft > 0) {
            cooldownLeft--
        }
    }

    fun internalAbility()
}