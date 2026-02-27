package org.gatu.client.setting

class BooleanSetting private constructor(
    name: String,
    description: String,
    default: Boolean,
    visibility: () -> Boolean
) : Setting<Boolean>(name, description, default, visibility) {

    fun toggle() {
        value = !value
        org.gatu.client.config.ConfigManager.save()
    }

    class Builder {
        private var _name: String? = null
        private var _description: String? = null
        private var _default: Boolean? = null
        private var _visibility: () -> Boolean = { true }

        fun name(value: String) { _name = value }
        fun description(value: String) { _description = value }
        fun default(value: Boolean) { _default = value }
        fun visibility(block: () -> Boolean) { _visibility = block }

        fun build(): BooleanSetting {
            return BooleanSetting(
                name = requireNotNull(_name) { "BooleanSetting: name is required." },
                description = requireNotNull(_description) { "BooleanSetting: description is required." },
                default = requireNotNull(_default) { "BooleanSetting: default is required." },
                visibility = _visibility
            )
        }
    }

    companion object {
        operator fun invoke(block: Builder.() -> Unit): BooleanSetting {
            return Builder().apply(block).build()
        }
    }
}