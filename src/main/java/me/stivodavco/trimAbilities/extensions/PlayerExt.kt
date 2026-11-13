package me.stivodavco.trimAbilities.extensions

import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.utilities.Storage
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.ArmorMeta

var Player.abilitySet: AbilitySet?
    get() = Storage.getAbilitySet(this)
    set(new) = Storage.setAbilitySet(this, new)

fun Player.isWearingFullSet() : Boolean {
    val helmetMeta = inventory.helmet.itemMeta as? ArmorMeta
    val targetPattern = helmetMeta?.trim?.pattern

    if (targetPattern != null) {
        var equal = true

        for (armor in inventory.armorContents) { // check if all armor has the same trim, if not, set equal to false
            if ( (armor?.itemMeta as? ArmorMeta)?.trim?.pattern != targetPattern ) {
                equal = false
                break
            }
        }

        return equal
    }

    return false
}

fun Player.equipSetIfPresent() {
    if (isWearingFullSet() && abilitySet == null) {
        val pattern = (inventory.helmet.itemMeta as? ArmorMeta)?.trim!!.pattern

        val clazz = pattern.abilitySetClass

        if (clazz != null) {
            val instance: AbilitySet?

            try {
                instance = clazz.getDeclaredConstructor(Player::class.java).newInstance(this)
            } catch (e: Exception) {
                throw Exception(e)
            }

            abilitySet = instance
        }
    } else if (!isWearingFullSet() && abilitySet != null) {
        abilitySet = null
    }
}
