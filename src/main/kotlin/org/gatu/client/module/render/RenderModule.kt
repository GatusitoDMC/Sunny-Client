package org.gatu.client.module.render

import org.gatu.client.module.Category
import org.gatu.client.module.ToggleableModule

abstract class RenderModule(
    name: String,
    description: String,
    defaultKey: Int = -1
) : ToggleableModule(name, description, Category.RENDER, defaultKey)
