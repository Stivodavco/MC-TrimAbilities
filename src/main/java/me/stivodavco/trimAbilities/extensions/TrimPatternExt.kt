package me.stivodavco.trimAbilities.extensions

import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import org.apache.commons.lang3.StringUtils
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.trim.TrimPattern

val TrimPattern.abilitySetClass : Class<out AbilitySet>?
    get() {
        var clazz: Class<out AbilitySet>? = null

        try {
            clazz = Class.forName("me.stivodavco.trimAbilities.abilityTrims.${StringUtils.capitalize(keyName)}AbilitySet").asSubclass(AbilitySet::class.java)
        } catch (e: Exception) {
            TrimAbilities.getInstance().logger.warning("Failed to get AbilitySet class from TrimPattern $this; ${e.stackTrace}")
        }

        return clazz
    }

val TrimPattern.itemMaterial : Material
    get() {
        val materialKey = NamespacedKey.minecraft("${keyName}_armor_trim_smithing_template")

        return Registry.MATERIAL.get(materialKey)!!
    }

val TrimPattern.keyName : String
    get() {
        val patternKey = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN).getKey(this)
        val name = patternKey!!.asString().substring(10)

        return name
    }