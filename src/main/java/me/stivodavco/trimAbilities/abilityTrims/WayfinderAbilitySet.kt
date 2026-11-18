package me.stivodavco.trimAbilities.abilityTrims

import me.stivodavco.trimAbilities.abstractClasses.Ability
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.inventory.meta.trim.TrimPattern

class WayfinderAbilitySet(val owner: Player) : AbilitySet(owner, TrimPattern.WAYFINDER), Ability {
    override var cooldownLeft = cooldown

    override fun internalAbility() : Boolean {
        val heldItem = owner.inventory.itemInMainHand
        if (heldItem.type == Material.COMPASS) {
            val meta = heldItem.itemMeta
            if (meta is CompassMeta) {
                val lodestone = meta.lodestone
                if (lodestone != null) {
                    owner.teleportAsync(lodestone)
                    return true
                }
            }
        }
        owner.playSound(owner.location, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f)
        return false
    }

    override fun internalDestroy() {}
}