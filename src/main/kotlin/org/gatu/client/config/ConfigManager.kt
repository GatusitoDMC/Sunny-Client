package org.gatu.client.config

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import net.minecraft.client.MinecraftClient
import org.gatu.client.friend.FriendManager
import org.gatu.client.module.ModuleManager
import org.gatu.client.module.ToggleableModule
import org.gatu.client.setting.*
import java.io.File

object ConfigManager {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    private val file: File
        get() {
            val dir = File(MinecraftClient.getInstance().runDirectory, "config/sunny")
            if (!dir.exists()) dir.mkdirs()
            return File(dir, "config.json")
        }

    fun save() {

        val root = JsonObject()

        ModuleManager.getModules().forEach { module ->

            val moduleObject = JsonObject()

            moduleObject.addProperty("enabled", module.enabled)

            if (module is ToggleableModule) {
                moduleObject.addProperty("keybind", module.keybind.value)
            }

            val settingsObject = JsonObject()

            module.settings.getAll().forEach { setting ->

                when (setting) {
                    is BooleanSetting -> settingsObject.addProperty(setting.name, setting.value)
                    is NumberSetting -> settingsObject.addProperty(setting.name, setting.value)
                    is ModeSetting -> settingsObject.addProperty(setting.name, setting.value)
                    is KeybindSetting -> settingsObject.addProperty(setting.name, setting.value)
                }
            }

            moduleObject.add("settings", settingsObject)
            root.add(module.name, moduleObject)
        }

        root.add("friends", gson.toJsonTree(FriendManager.getAll()))

        file.writeText(gson.toJson(root))
    }
    fun load() {

        if (!file.exists()) return

        val root = gson.fromJson(file.readText(), JsonObject::class.java)

        if (root.has("friends")) {
            FriendManager.clear()
            val array = root.getAsJsonArray("friends")
            array.forEach {
                FriendManager.add(it.asString)
            }
        }

        ModuleManager.getModules().forEach { module ->
            val moduleObject = root.getAsJsonObject(module.name) ?: return@forEach

            if (moduleObject.has("enabled")) {
                val enabled = moduleObject.get("enabled").asBoolean
                if (enabled != module.enabled) module.toggle()
            }

            if (module is ToggleableModule && moduleObject.has("keybind")) {
                module.keybind.value = moduleObject.get("keybind").asInt
            }

            val settingsObject = moduleObject.getAsJsonObject("settings") ?: return@forEach

            module.settings.getAll().forEach { setting ->

                if (!settingsObject.has(setting.name)) return@forEach

                when (setting) {
                    is BooleanSetting -> setting.value = settingsObject.get(setting.name).asBoolean
                    is NumberSetting -> setting.value = settingsObject.get(setting.name).asDouble
                    is ModeSetting -> setting.setMode(settingsObject.get(setting.name).asString)
                    is KeybindSetting -> setting.value = settingsObject.get(setting.name).asInt
                }
            }
        }
    }
}
