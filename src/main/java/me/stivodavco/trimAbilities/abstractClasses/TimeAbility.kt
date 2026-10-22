package me.stivodavco.trimAbilities.abstractClasses

import me.stivodavco.trimAbilities.utilities.TrimRegistry

interface TimeAbility : Ability {
    val ACTIVATION_TIME: Int
        get() = disableWhenNull(TrimRegistry.getTrimKey(pattern)?.abilityInfo()?.activationTime(), 0)
    var activeTimeLeft: Int
    var active: Boolean

    override fun reset() {
        internalReset()
        super.reset()
    }

    private fun internalReset() {
        activeTimeLeft = ACTIVATION_TIME
        active = false
    }

    override fun tick() {
        if (active && activeTimeLeft > 0) {
            activeTimeLeft--
        } else {
            super.tick()
        }
    }

    override fun ability(): Boolean {
        if (active) {
            return false
        } else {
            return super.ability()
        }
    }
}