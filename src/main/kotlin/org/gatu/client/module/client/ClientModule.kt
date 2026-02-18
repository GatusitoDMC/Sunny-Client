package org.gatu.client.module.client

import org.gatu.client.module.Category
import org.gatu.client.module.Module

abstract class ClientModule(
    name: String,
    description: String
) : Module(name, description, Category.CLIENT)
