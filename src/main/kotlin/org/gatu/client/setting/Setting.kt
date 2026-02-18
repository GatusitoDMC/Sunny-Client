package org.gatu.client.setting

abstract class Setting<T>(
    val name: String,
    val description: String,
    var value: T
) {

    protected val defaultValue: T = value

    open fun reset() {
        value = defaultValue
    }
}
