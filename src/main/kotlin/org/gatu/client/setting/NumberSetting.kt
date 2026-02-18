package org.gatu.client.setting

class NumberSetting(
    name: String,
    description: String,
    default: Double,
    val min: Double,
    val max: Double,
    val increment: Double
) : Setting<Double>(name, description, default) {

    fun setValue(newValue: Double) {
        value = newValue.coerceIn(min, max)
    }
}
