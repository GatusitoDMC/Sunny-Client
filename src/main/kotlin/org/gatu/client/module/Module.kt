package org.gatu.client.module

import org.gatu.client.setting.SettingContainer

abstract class Module(
    val name: String,
    val description: String,
    val category: Category
) {

    var enabled = false
        private set

    var extended = false

    val settings = SettingContainer()

    init {
        ModuleManager.register(this)
    }
    private fun sendToggleMessage(state: Boolean) {

        val mc = net.minecraft.client.MinecraftClient.getInstance()
        val player = mc.player ?: return

        val status = if (state) "§aEnabled" else "§cDisabled"
        val message = "§6[Sunny] §f$name §7» $status"

        player.sendMessage(net.minecraft.text.Text.literal(message), false)
    }

    fun toggle() {
        enabled = !enabled

        if (enabled) {
            onEnable()
            sendToggleMessage(true)
        } else {
            onDisable()
            sendToggleMessage(false)
        }
        org.gatu.client.config.ConfigManager.save()

    }

    open fun onEnable() {}
    open fun onDisable() {}
    open fun onTick() {}
}
