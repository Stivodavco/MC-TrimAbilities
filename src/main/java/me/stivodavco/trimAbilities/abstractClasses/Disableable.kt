package me.stivodavco.trimAbilities.abstractClasses

interface Disableable {
    var enabled: Boolean

    fun enable() {
        enabled = true
    }

    fun disable() {
        enabled = false
    }

    fun <T> disableWhenNull(value: Any?, returnValue: T): T {
        if (value == null) {
            disable()
        }

        return returnValue
    }

}