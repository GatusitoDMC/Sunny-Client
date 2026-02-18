package org.gatu.client.module.client

import net.minecraft.client.MinecraftClient
import org.gatu.client.SunnyClient
import org.gatu.client.event.events.Render2DEvent
import org.gatu.client.module.Category
import org.gatu.client.module.Module
import org.gatu.client.module.ModuleManager
import org.gatu.client.setting.BooleanSetting

class HUD : Module(
    "HUD",
    "Client visual settings",
    Category.CLIENT
) {

    private val arrayList = BooleanSetting(
        "ArrayList",
        "Show active modules",
        true
    )

    init {
        settings.add(arrayList)

        SunnyClient.EVENT_BUS.subscribe(Render2DEvent::class.java) { event ->
            if (!enabled) return@subscribe
            render(event)
        }
    }

    private fun render(event: Render2DEvent) {

        if (!arrayList.value) return

        val mc = MinecraftClient.getInstance()
        val context = event.context
        val textRenderer = mc.textRenderer

        val activeModules = ModuleManager.getModules()
            .filter { it.enabled && it.category != Category.CLIENT }
            .sortedByDescending { textRenderer.getWidth(it.name) }

        var y = mc.window.scaledHeight - 6

        for (module in activeModules.reversed()) {

            val text = module.name
            val width = textRenderer.getWidth(text)

            val x = mc.window.scaledWidth - width - 4
            y -= 10

            context.drawText(
                textRenderer,
                text,
                x,
                y,
                0xFFFFFFFF.toInt(),
                false
            )
        }
    }
}
