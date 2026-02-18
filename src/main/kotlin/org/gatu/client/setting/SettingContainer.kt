package org.gatu.client.setting

class SettingContainer {

    private val settings = mutableListOf<Setting<*>>()

    fun add(setting: Setting<*>) {
        settings.add(setting)
    }

    fun getAll(): List<Setting<*>> = settings

    fun resetAll() {
        settings.forEach { it.reset() }
    }
}
