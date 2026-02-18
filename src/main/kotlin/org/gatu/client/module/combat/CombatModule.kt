package org.gatu.client.module.combat

import org.gatu.client.module.Category
import org.gatu.client.module.ToggleableModule

abstract class CombatModule(
    name: String,
    description: String,
    defaultKey: Int = -1
) : ToggleableModule(name, description, Category.COMBAT, defaultKey)
