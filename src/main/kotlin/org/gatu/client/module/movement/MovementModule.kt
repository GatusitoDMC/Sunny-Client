package org.gatu.client.module.movement

import org.gatu.client.module.Category
import org.gatu.client.module.ToggleableModule

abstract class MovementModule(
    name: String,
    description: String,
    defaultKey: Int = -1
) : ToggleableModule(name, description, Category.MOVEMENT, defaultKey)
