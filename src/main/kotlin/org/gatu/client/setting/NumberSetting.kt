package org.gatu.client.setting

class NumberSetting private constructor(
    name: String,
    description: String,
    default: Double,
    val min: Double,
    val max: Double,
    val increment: Double,
    visibility: () -> Boolean
) : Setting<Double>(name, description, default, visibility) {

    fun setValue(newValue: Double) {
        value = newValue.coerceIn(min, max)
        org.gatu.client.config.ConfigManager.save()
    }

    class Builder {
        private var _name: String? = null
        private var _description: String? = null
        private var _default: Double? = null
        private var _min: Double? = null
        private var _max: Double? = null
        private var _increment: Double? = null
        private var _visibility: () -> Boolean = { true }

        fun name(value: String) { _name = value }
        fun description(value: String) { _description = value }
        fun default(value: Double) { _default = value }
        fun min(value: Double) { _min = value }
        fun max(value: Double) { _max = value }
        fun increment(value: Double) { _increment = value }
        fun visibility(block: () -> Boolean) { _visibility = block }

        fun build(): NumberSetting {
            return NumberSetting(
                name = requireNotNull(_name) { "NumberSetting: name is required." },
                description = requireNotNull(_description) { "NumberSetting: description is required." },
                default = requireNotNull(_default) { "NumberSetting: default is required." },
                min = requireNotNull(_min) { "NumberSetting: min is required." },
                max = requireNotNull(_max) { "NumberSetting: max is required." },
                increment = requireNotNull(_increment) { "NumberSetting: increment is required." },
                visibility = _visibility
            )
        }
    }

    companion object {
        operator fun invoke(block: Builder.() -> Unit): NumberSetting {
            return Builder().apply(block).build()
        }
    }
}