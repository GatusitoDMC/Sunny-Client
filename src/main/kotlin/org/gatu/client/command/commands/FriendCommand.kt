package org.gatu.client.command.commands

import org.gatu.client.command.Command
import org.gatu.client.command.CommandManager
import org.gatu.client.config.ConfigManager
import org.gatu.client.friend.FriendManager

class FriendCommand : Command(
    "friend",
    "Manage friends"
) {

    override fun execute(args: List<String>) {

        if (args.isEmpty()) {
            CommandManager.send("§cUsage: -friend <add/remove/list> <name>")
            return
        }

        when (args[0].lowercase()) {

            "add" -> {
                if (args.size < 2) {
                    CommandManager.send("§cUsage: -friend add <name>")
                    return
                }

                FriendManager.add(args[1])
                ConfigManager.save()
                CommandManager.send("§aAdded ${args[1]} to friends.")
            }

            "remove" -> {
                if (args.size < 2) {
                    CommandManager.send("§cUsage: -friend remove <name>")
                    return
                }

                FriendManager.remove(args[1])
                ConfigManager.save()
                CommandManager.send("§aRemoved ${args[1]} from friends.")
            }

            "list" -> {
                val list = FriendManager.getAll()

                if (list.isEmpty()) {
                    CommandManager.send("§7No friends added.")
                } else {
                    CommandManager.send("§eFriends: §f${list.joinToString(", ")}")
                }
            }

            else -> {
                CommandManager.send("§cInvalid option.")
            }
        }
    }
}