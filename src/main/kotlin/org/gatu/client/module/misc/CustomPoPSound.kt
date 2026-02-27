package org.gatu.client.module.misc

import org.gatu.client.module.Category
import org.gatu.client.module.Module
import org.gatu.client.setting.ModeSetting

class CustomPoPSound : Module(
    "PoP Sounds",
    "PoP Sound",
    Category.MISC
) {
    enum class sounds { tnt, anvil, totem }

    val sound: ModeSetting<sounds> = ModeSetting {
        name("Sound")
        description("Select pop sound")
        enum(sounds::class)
        default(sounds.totem)
    }


    init {
        settings.add(sound)
        INSTANCE = this
    }

    companion object {
        @JvmStatic var INSTANCE: CustomPoPSound? = null
    }
}