package org.gatu.client.module

object ModuleManager {

    private val modules = mutableListOf<Module>()

    fun register(module: Module) {
        modules.add(module)
        modules.sortBy { it.name.lowercase() }
    }

    fun getModules(): List<Module> = modules

    fun getByCategory(category: Category): List<Module> =
        modules.filter { it.category == category }

    fun getByName(name: String): Module? =
        modules.firstOrNull { it.name.equals(name, true) }
}
