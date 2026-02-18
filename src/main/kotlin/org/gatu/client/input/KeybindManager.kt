package org.gatu.client.input

import org.gatu.client.module.ModuleManager
import org.gatu.client.module.ToggleableModule
import org.lwjgl.glfw.GLFW

object KeybindManager {

    private val pressed = mutableSetOf<Int>()

    fun handleKey(window: Long) {

        for (key in 32..348) {

            val state = GLFW.glfwGetKey(window, key)

            if (state == GLFW.GLFW_PRESS) {

                if (!pressed.contains(key)) {

                    pressed.add(key)

                    ModuleManager.getModules().forEach { module ->
                        if (module is ToggleableModule) {
                            if (module.keybind.value == key) {
                                module.toggle()
                            }
                        }
                    }
                }
            }

            if (state == GLFW.GLFW_RELEASE) {
                pressed.remove(key)
            }
        }
    }
}
