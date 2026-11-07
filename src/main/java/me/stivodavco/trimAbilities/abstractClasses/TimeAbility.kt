package me.stivodavco.trimAbilities.abstractClasses

import me.stivodavco.trimAbilities.utilities.TrimRegistry

interface TimeAbility : Ability {
    val activationTime: Int
        get() = TrimRegistry.getTrimKey(pattern)?.abilityInfo()?.activationTime() ?: 0
    var activeTimeLeft: Int
    var active: Boolean

    private fun internalReset() {
        activeTimeLeft = activationTime
        active = false
    }

    override fun tick() {
        if (active && activeTimeLeft > 0) {
            activeTimeLeft--

            if (activeTimeLeft == 0) {
                active = false
            }
        } else {
            super.tick()
        }
    }

    override fun ability(): Boolean {
        if (active) {
            return false
        } else {
            active = true
            activeTimeLeft = activationTime
            return super.ability()
        }
    }
}