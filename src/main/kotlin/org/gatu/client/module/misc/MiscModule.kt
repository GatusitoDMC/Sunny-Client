package org.gatu.client.module.misc

import org.gatu.client.module.Category
import org.gatu.client.module.ToggleableModule

abstract class MiscModule(
    name: String,
    description: String,
    defaultKey: Int = -1
) : ToggleableModule(name, description, Category.MISC, defaultKey)
