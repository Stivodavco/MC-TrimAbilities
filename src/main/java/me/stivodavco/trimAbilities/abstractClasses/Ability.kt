package me.stivodavco.trimAbilities.abstractClasses

import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.extensions.keyName
import org.bukkit.inventory.meta.trim.TrimPattern

interface Ability {
    val abilityEnabled: Boolean
        get() = TrimAbilities.getTrimsConfig().getBoolean("${pattern.keyName}.ability.enabled")

    val cooldown: Int
        get() = TrimAbilities.getTrimsConfig().getInt("${pattern.keyName}.ability.cooldown")

    val pattern: TrimPattern
    var cooldownLeft: Int

    fun reset() {
        cooldownLeft = cooldown
    }

    fun ability(): Boolean {
        if (!abilityEnabled) { return false }

        if (cooldownLeft <= 0 && this is AbilitySet && internalAbility()) {
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

    fun internalAbility() : Boolean
}