package org.gatu.client.command.commands

import org.gatu.client.command.Command
import org.gatu.client.command.CommandManager
import org.gatu.client.config.ConfigManager
import org.gatu.client.module.Category
import org.gatu.client.module.ModuleManager

class ResetCommand : Command(
    "reset",
    "Reset module or category"
) {

    override fun execute(args: List<String>) {

        if (args.size < 2) {
            CommandManager.send("§cUsage: -reset <module/category> <name>")
            return
        }

        when (args[0].lowercase()) {

            "module" -> {

                val moduleName = args[1]

                val module = ModuleManager.getModules()
                    .firstOrNull { it.name.equals(moduleName, true) }

                if (module == null) {
                    CommandManager.send("§cModule not found.")
                    return
                }

                if (module.enabled) module.toggle()
                module.settings.resetAll()

                CommandManager.send("§aModule ${module.name} reset.")
            }

            "category" -> {

                val category = Category.values()
                    .firstOrNull { it.name.equals(args[1], true) }

                if (category == null) {
                    CommandManager.send("§cCategory not found.")
                    return
                }

                ModuleManager.getByCategory(category).forEach { module ->
                    if (module.enabled) module.toggle()
                    module.settings.resetAll()
                }

                CommandManager.send("§aCategory ${category.name} reset.")
            }

            else -> {
                CommandManager.send("§cInvalid type. Use module or category.")
            }
        }

        ConfigManager.save()
    }
}
