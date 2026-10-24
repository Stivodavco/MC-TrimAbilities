package me.stivodavco.trimAbilities.abstractClasses

import me.stivodavco.trimAbilities.utilities.TrimRegistry

interface TimeAbility : Ability {
    val activationTime: Int
        get() = disableWhenNull(TrimRegistry.getTrimKey(pattern)?.abilityInfo()?.activationTime(), 0)
    var activeTimeLeft: Int
    var active: Boolean

    override fun reset() {
        internalReset()
        super.reset()
    }

    private fun internalReset() {
        activeTimeLeft = activationTime
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