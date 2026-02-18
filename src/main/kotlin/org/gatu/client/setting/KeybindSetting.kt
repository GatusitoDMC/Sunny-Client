package org.gatu.client.setting

class KeybindSetting(
    name: String,
    description: String,
    default: Int
) : Setting<Int>(name, description, default)
