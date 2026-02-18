package org.gatu.client.util

import net.minecraft.client.MinecraftClient
import net.minecraft.item.Item
import net.minecraft.screen.slot.SlotActionType

object InventoryUtils {

    private var previousSlot = -1

    fun findHotbar(item: Item): Int? {
        val inv = MinecraftClient.getInstance().player?.inventory ?: return null

        for (i in 0..8) {
            if (inv.getStack(i).item == item) return i
        }

        return null
    }

    fun find(item: Item): Int? {
        val inv = MinecraftClient.getInstance().player?.inventory ?: return null

        for (i in 0 until inv.size()) {
            if (inv.getStack(i).item == item) return i
        }

        return null
    }

    fun swap(slot: Int) {
        val player = MinecraftClient.getInstance().player ?: return
        previousSlot = player.inventory.selectedSlot
        player.inventory.selectedSlot = slot
    }

    fun swapBack() {
        val player = MinecraftClient.getInstance().player ?: return
        if (previousSlot != -1) {
            player.inventory.selectedSlot = previousSlot
            previousSlot = -1
        }
    }

    fun moveToHotbar(from: Int, to: Int) {
        val mc = MinecraftClient.getInstance()
        val player = mc.player ?: return

        mc.interactionManager?.clickSlot(
            player.currentScreenHandler.syncId,
            from,
            to,
            SlotActionType.SWAP,
            player
        )
    }

    fun getEmptyHotbarSlot(): Int? {
        val inv = MinecraftClient.getInstance().player?.inventory ?: return null

        for (i in 0..8) {
            if (inv.getStack(i).isEmpty) return i
        }

        return null
    }
}
