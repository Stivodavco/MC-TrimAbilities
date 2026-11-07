package me.stivodavco.trimAbilities.abstractClasses

import me.stivodavco.trimAbilities.utilities.TrimRegistry
import org.bukkit.inventory.meta.trim.TrimPattern

interface Passive {
    val passiveEnabled: Boolean
        get() = TrimRegistry.getTrimKey(pattern)?.passiveInfo?.enabled ?: false

    val pattern: TrimPattern

    fun passive() {
        if (passiveEnabled) {
            internalPassive()
        }
    }

    fun internalPassive()
}