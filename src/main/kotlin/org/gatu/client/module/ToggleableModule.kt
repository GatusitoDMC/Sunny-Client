package org.gatu.client.module

import org.gatu.client.setting.KeybindSetting

abstract class ToggleableModule(
    name: String,
    description: String,
    category: Category,
    defaultKey: Int = -1
) : Module(name, description, category) {

    val keybind = KeybindSetting(
        "Keybind",
        "Key to toggle the module",
        defaultKey
    )

    init {
        settings.add(keybind)
    }
}
