package org.gatu.client.setting

class ModeSetting(
    name: String,
    description: String,
    val modes: List<String>,
    default: String
) : Setting<String>(name, description, default) {

    init {
        require(modes.contains(default))
    }

    fun setMode(mode: String) {
        if (modes.contains(mode)) {
            value = mode
        }
    }
}
