package me.stivodavco.trimAbilities.extensions

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import org.apache.commons.lang3.StringUtils
import org.bukkit.inventory.meta.trim.TrimPattern

val TrimPattern.clazz : Class<out AbilitySet>?
    get() {
        val key = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN).getKey(this)
        val name = key!!.asString().substring(10)

        var clazz: Class<out AbilitySet>? = null

        try {
            clazz = Class.forName("me.stivodavco.trimAbilities.abilityTrims.${StringUtils.capitalize(name)}AbilitySet").asSubclass(AbilitySet::class.java)
        } catch (e: Exception) {
            TrimAbilities.getInstance().logger.warning("Failed to get AbilitySet class from TrimPattern $this; ${e.stackTrace}")
        }

        return clazz
    }