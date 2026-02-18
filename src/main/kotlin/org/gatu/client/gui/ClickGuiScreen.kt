package org.gatu.client.gui

import net.minecraft.client.gui.Click
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.input.KeyInput
import net.minecraft.text.Text
import org.gatu.client.config.ConfigManager
import org.gatu.client.module.Category
import org.gatu.client.module.ModuleManager
import org.gatu.client.setting.*
import org.lwjgl.glfw.GLFW

class ClickGuiScreen : Screen(Text.literal("Sunny Client")) {

    private var listeningKeybind: KeybindSetting? = null

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {

        context.fill(0, 0, width, height, 0x88000000.toInt())

        var xOffset = 4

        for (category in Category.values()) {
            drawCategory(context, category, xOffset, 4, mouseX, mouseY)
            xOffset += 90
        }

        super.render(context, mouseX, mouseY, delta)
    }

    private fun drawCategory(
        context: DrawContext,
        category: Category,
        x: Int,
        y: Int,
        mouseX: Int,
        mouseY: Int
    ) {

        val headerHeight = 10
        val moduleHeight = 10
        val settingHeight = 8
        val width = 85

        context.fill(x, y, x + width, y + headerHeight, 0xFFFFC107.toInt())
        context.drawText(textRenderer, category.name, x + 3, y + 1, 0xFF000000.toInt(), false)

        var currentY = y + headerHeight + 2

        for (module in ModuleManager.getByCategory(category)) {

            val color = if (module.enabled) 0xFF2ECC71.toInt() else 0xFF2C2C2C.toInt()
            context.fill(x, currentY, x + width, currentY + moduleHeight, color)
            context.drawText(textRenderer, module.name, x + 3, currentY + 1, 0xFFFFFFFF.toInt(), false)

            if (module.extended) {

                var settingY = currentY + moduleHeight + 1

                for (setting in module.settings.getAll()) {

                    context.fill(x + 2, settingY, x + width - 2, settingY + settingHeight, 0xFF1A1A1A.toInt())

                    when (setting) {

                        is BooleanSetting -> {
                            val boolColor = if (setting.value) 0xFF2ECC71.toInt() else 0xFFE74C3C.toInt()
                            context.fill(x + width - 14, settingY + 1, x + width - 6, settingY + 6, boolColor)
                            context.drawText(textRenderer, setting.name, x + 4, settingY, 0xFFAAAAAA.toInt(), false)
                        }

                        is ModeSetting -> {
                            context.drawText(
                                textRenderer,
                                "${setting.name}: ${setting.value}",
                                x + 4,
                                settingY,
                                0xFFAAAAAA.toInt(),
                                false
                            )
                        }

                        is NumberSetting -> {
                            val percent = (setting.value - setting.min) / (setting.max - setting.min)
                            val barWidth = ((width - 6) * percent).toInt()
                            context.fill(x + 2, settingY, x + 2 + barWidth, settingY + settingHeight, 0x88FFC107.toInt())
                            context.drawText(
                                textRenderer,
                                "%.1f".format(setting.value),
                                x + 4,
                                settingY,
                                0xFFFFFFFF.toInt(),
                                false
                            )
                        }

                        is KeybindSetting -> {
                            val keyName =
                                if (listeningKeybind == setting) "..."
                                else if (setting.value <= 0) "NONE"
                                else GLFW.glfwGetKeyName(setting.value, 0)?.uppercase()
                                    ?: "K${setting.value}"

                            context.drawText(
                                textRenderer,
                                "Bind: $keyName",
                                x + 4,
                                settingY,
                                0xFFAAAAAA.toInt(),
                                false
                            )
                        }
                    }

                    settingY += settingHeight + 1
                }

                currentY = settingY
            }

            currentY += moduleHeight + 2
        }
    }

    override fun mouseDragged(click: Click, deltaX: Double, deltaY: Double): Boolean {

        val mouseX = click.x()
        val mouseY = click.y()

        var xOffset = 4

        for (category in Category.values()) {

            var currentY = 4 + 12
            val width = 85
            val moduleHeight = 10
            val settingHeight = 8

            for (module in ModuleManager.getByCategory(category)) {

                if (module.extended) {

                    var settingY = currentY + moduleHeight + 1

                    for (setting in module.settings.getAll()) {

                        if (setting is NumberSetting) {

                            if (mouseX >= xOffset + 2 && mouseX <= xOffset + width - 2 &&
                                mouseY >= settingY && mouseY <= settingY + settingHeight
                            ) {

                                val percent =
                                    ((mouseX - (xOffset + 2)) / (width - 4).toDouble())
                                        .coerceIn(0.0, 1.0)

                                val newValue =
                                    setting.min + (setting.max - setting.min) * percent
                                setting.value = newValue
                                ConfigManager.save()
                                return true
                            }
                        }

                        settingY += settingHeight + 1
                    }
                }

                currentY += moduleHeight + 2
            }

            xOffset += 90
        }

        return super.mouseDragged(click, deltaX, deltaY)
    }

    override fun mouseClicked(click: Click, doubled: Boolean): Boolean {

        val mouseX = click.x()
        val mouseY = click.y()
        val button = click.button()

        var xOffset = 4

        for (category in Category.values()) {

            var currentY = 4 + 12
            val width = 85
            val moduleHeight = 10
            val settingHeight = 8

            for (module in ModuleManager.getByCategory(category)) {

                if (mouseX >= xOffset && mouseX <= xOffset + width &&
                    mouseY >= currentY && mouseY <= currentY + moduleHeight
                ) {
                    if (button == 0) module.toggle()
                    if (button == 1) module.extended = !module.extended
                    return true
                }

                if (module.extended) {

                    var settingY = currentY + moduleHeight + 1

                    for (setting in module.settings.getAll()) {

                        if (mouseX >= xOffset + 2 && mouseX <= xOffset + width - 2 &&
                            mouseY >= settingY && mouseY <= settingY + settingHeight
                        ) {

                            when (setting) {

                                is BooleanSetting -> {
                                    setting.toggle()
                                    ConfigManager.save()
                                }

                                is ModeSetting -> {
                                    val next =
                                        (setting.modes.indexOf(setting.value) + 1) % setting.modes.size
                                    setting.setMode(setting.modes[next])
                                    ConfigManager.save()
                                }

                                is KeybindSetting -> listeningKeybind = setting
                            }

                            return true
                        }

                        settingY += settingHeight + 1
                    }

                    currentY = settingY
                }

                currentY += moduleHeight + 2
            }

            xOffset += 90
        }

        return super.mouseClicked(click, doubled)
    }

    override fun keyPressed(input: KeyInput): Boolean {

        val keyCode = input.key()

        listeningKeybind?.let {
            it.value = if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_DELETE) -1 else keyCode
            ConfigManager.save()
            listeningKeybind = null
            return true
        }

        return super.keyPressed(input)
    }

    override fun shouldPause(): Boolean = false
}
