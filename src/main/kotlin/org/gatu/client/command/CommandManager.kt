package org.gatu.client.command

import net.minecraft.client.MinecraftClient

object CommandManager {

    private const val PREFIX = "-"
    private val commands = mutableListOf<Command>()

    fun register(command: Command) {
        commands.add(command)
    }

    fun handleMessage(message: String): Boolean {

        if (!message.startsWith(PREFIX)) return false

        val split = message.substring(1).split(" ")
        val name = split[0]
        val args = split.drop(1)

        val command = commands.firstOrNull { it.name.equals(name, true) }

        if (command != null) {
            command.execute(args)
        } else {
            send("§cUnknown command.")
        }

        return true
    }

    fun send(msg: String) {
        val player = MinecraftClient.getInstance().player ?: return
        player.sendMessage(net.minecraft.text.Text.literal("§6[Sunny] §f$msg"), false)
    }
}
