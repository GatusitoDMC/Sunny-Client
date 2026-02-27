package org.gatu.client.setting

abstract class Setting<T>(
    val name: String,
    val description: String,
    value: T,
    private val visibility: () -> Boolean = { true }
) {

    var value: T = value
        set(newValue) {
            field = newValue
            org.gatu.client.config.ConfigManager.save()
        }

    protected val defaultValue: T = value

    open fun reset() {
        value = defaultValue
    }

    fun isVisible(): Boolean = visibility()
}