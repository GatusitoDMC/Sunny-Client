package org.gatu.client.setting

import kotlin.reflect.KClass

class ModeSetting<T : Enum<T>> private constructor(
    name: String,
    description: String,
    val modes: Array<T>,
    default: T
) : Setting<T>(name, description, default) {

    class Builder<T : Enum<T>> {
        private var _name: String? = null
        private var _description: String? = null
        private var _enumKClass: KClass<T>? = null
        private var _default: T? = null

        fun name(value: String) { _name = value }
        fun description(value: String) { _description = value }
        fun enum(value: KClass<T>) { _enumKClass = value }
        fun default(value: T) { _default = value }

        fun build(): ModeSetting<T> {
            val name = requireNotNull(_name) { "ModeSetting: name is required." }
            val desc = requireNotNull(_description) { "ModeSetting: description is required." }
            val kClass = requireNotNull(_enumKClass) { "ModeSetting: enum() is required." }
            val def = requireNotNull(_default) { "ModeSetting: default() is required." }

            val modes = kClass.java.enumConstants
            require(modes.contains(def)) { "Default value is not in enum constants." }

            return ModeSetting(name, desc, modes, def)
        }
    }

    fun setMode(mode: T) { value = mode }

    fun setModeByName(name: String) {
        modes.firstOrNull { it.name.equals(name, true) }?.let { value = it }
    }

    companion object {
        inline operator fun <reified T : Enum<T>> invoke(block: Builder<T>.() -> Unit): ModeSetting<T> {
            return Builder<T>().apply(block).build()
        }
    }
}