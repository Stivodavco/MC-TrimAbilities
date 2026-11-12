package me.stivodavco.trimAbilities.utilities

import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.Ability
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.Passive
import me.stivodavco.trimAbilities.abstractClasses.TimeAbility
import me.stivodavco.trimAbilities.extensions.keyName
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class AbilityActionbar(val owner: Player, val abilitySet: AbilitySet) {
    val runnable = object : BukkitRunnable() {
        override fun run() {
            display()
        }
    }.runTaskTimer(TrimAbilities.getInstance(), 0, 5)

    fun disable() {
        runnable.cancel()
        owner.sendActionBar(Component.empty())
    }

    fun getPassiveComponent() : Component {
        val name = TrimAbilities.getTrimsConfig().getString("${abilitySet.pattern.keyName}.passive.name")

        return Component.text(" [$name]").
            color(TextColor.color(135, 135, 135)).decorate(TextDecoration.ITALIC)
    }

    fun formatTime(total: Int) : String {
        val min = total / 60
        val sec = total - min * 60

        return "${min}m ${sec}s"
    }

    fun getAbilityComponent() : Component {
        val name = TrimAbilities.getTrimsConfig().getString("${abilitySet.pattern.keyName}.ability.name") ?: "N/A"
        val base = Component.text(" $name ").color(TextColor.color(253, 131, 255)).decorate(TextDecoration.BOLD)

        var cooldown: Component

        if (abilitySet is TimeAbility && abilitySet.active) {
            cooldown = Component.text("⏱ ${formatTime(abilitySet.activeTimeLeft)}").color(TextColor.color(84, 153, 224))
        } else if (abilitySet is Ability) {
            if (abilitySet.cooldownLeft == 0) {
                cooldown = Component.text("♦ READY").color(TextColor.color(40, 242, 63))
            } else {
                cooldown = Component.text("⏳ ${formatTime(abilitySet.cooldownLeft)}").color(TextColor.color(224, 69, 24))
            }
        } else {
            cooldown = Component.empty()
        }

        cooldown = cooldown.decoration(TextDecoration.BOLD, false)

        return base.append(cooldown)
    }

    fun display() {
        var message: Component? = null

        if (abilitySet is Ability && abilitySet.abilityEnabled) {
            message = getAbilityComponent()
        } else if (abilitySet is Passive && abilitySet.passiveEnabled) {
            message = getPassiveComponent()
        }

        if (message != null) {
            owner.sendActionBar(message)
        }
    }
}