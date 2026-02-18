package org.gatu.client.setting

class BooleanSetting(
    name: String,
    description: String,
    default: Boolean
) : Setting<Boolean>(name, description, default) {

    fun toggle() {
        value = !value
        org.gatu.client.config.ConfigManager.save()

    }

}
