package org.gatu.client.module.combat

import net.minecraft.client.MinecraftClient
import net.minecraft.item.Items
import net.minecraft.screen.slot.SlotActionType
import org.gatu.client.SunnyClient
import org.gatu.client.event.events.TickEvent

import org.gatu.client.setting.NumberSetting
import org.gatu.client.util.InventoryUtils
import kotlin.jvm.java

class Offhand : CombatModule(
    "AutoTotem",
    "Totem in OffHand"
) {
    private val minLife = NumberSetting {
        name("Min Life")
        description("Min Life")
        default(12.0)
        min(1.0)
        max(20.0)
        increment(1.0)
    }



    private var tickCounter = 0

    init {
        settings.add(minLife)
        SunnyClient.EVENT_BUS.subscribe(TickEvent::class.java) {
            if (enabled) onTick()
        }
    }

    override fun onTick() {
        val mc = MinecraftClient.getInstance()
        val player = mc.player ?: return

        val currentHealth = player.health + player.absorptionAmount

        if (currentHealth > minLife.value) return

        if (player.offHandStack.item == Items.TOTEM_OF_UNDYING) return

        val slot = InventoryUtils.find(Items.TOTEM_OF_UNDYING) ?: return

        InventoryUtils.moveToOffhand(slot)
    }
}