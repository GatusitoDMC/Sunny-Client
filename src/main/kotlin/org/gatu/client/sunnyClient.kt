package org.gatu.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.gatu.client.command.CommandManager
import org.gatu.client.command.commands.FriendCommand
import org.gatu.client.command.commands.ResetCommand
import org.gatu.client.config.ConfigManager
import org.gatu.client.event.EventBus
import org.gatu.client.event.events.Render2DEvent
import org.gatu.client.event.events.TickEvent
import org.gatu.client.gui.ClickGuiScreen
import org.gatu.client.input.KeybindManager
import org.gatu.client.module.client.HUD
import org.gatu.client.module.combat.*
import org.gatu.client.module.movement.NoFall
import org.lwjgl.glfw.GLFW


class SunnyClient : ClientModInitializer {

    companion object {
        val EVENT_BUS = EventBus()
    }

    private lateinit var guiKey: KeyBinding

    override fun onInitializeClient() {

        // Registros
        KillAura()
        NoFall()
        AnchorAura()

        HUD()


        ConfigManager.load()

        CommandManager.register(ResetCommand())

        CommandManager.register(FriendCommand())


        //etc

        ClientSendMessageEvents.ALLOW_CHAT.register { message ->
            !CommandManager.handleMessage(message)
        }

        HudRenderCallback.EVENT.register { context, _ ->
            EVENT_BUS.post(Render2DEvent(context))
        }

        ClientTickEvents.END_CLIENT_TICK.register {
            EVENT_BUS.post(TickEvent())
        }

        guiKey = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.sunny.gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyBinding.Category.MISC
            )
        )

        ClientTickEvents.END_CLIENT_TICK.register { client ->

            while (guiKey.wasPressed()) {
                client.setScreen(ClickGuiScreen())
            }

            val window = client.window.handle

            KeybindManager.handleKey(client.window.handle)

        }
    }
}

