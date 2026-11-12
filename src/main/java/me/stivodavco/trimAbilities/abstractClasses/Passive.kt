package me.stivodavco.trimAbilities.abstractClasses

import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.extensions.keyName
import org.bukkit.inventory.meta.trim.TrimPattern

interface Passive {
    val passiveEnabled: Boolean
        get() = TrimAbilities.getTrimsConfig().getBoolean("${pattern.keyName}.passive.enabled")

    val pattern: TrimPattern

    fun passive() {
        if (passiveEnabled) {
            internalPassive()
        }
    }

    fun internalPassive()
}