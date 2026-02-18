package org.gatu.client.module.combat

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRendering.render
import net.minecraft.block.Blocks
import net.minecraft.block.RespawnAnchorBlock
import net.minecraft.client.MinecraftClient
import net.minecraft.item.Items
import net.minecraft.util.math.BlockPos
import org.gatu.client.SunnyClient
import org.gatu.client.event.events.TickEvent
import org.gatu.client.module.Category
import org.gatu.client.module.ToggleableModule
import org.gatu.client.setting.BooleanSetting
import org.gatu.client.setting.NumberSetting
import org.gatu.client.util.*

class AnchorAura : ToggleableModule(
    "AnchorAura",
    "Places and explodes respawn anchors",
    Category.COMBAT
) {

    private val range = NumberSetting("Range", "Place range", 4.5, 1.0, 6.0, 0.1)
    private val delay = NumberSetting("Delay", "Tick delay", 5.0, 0.0, 20.0, 1.0)
    private val rotate = BooleanSetting("Rotate", "Silent rotate", true)
    private val pauseneat = BooleanSetting("Pause On eat", "Pause on eating", true)
    private val pauseonsneaking = BooleanSetting("Pause on Sneaking", "Pause on sneaking", true)

    private val onlyOne = BooleanSetting("OnlyOne", "Lock one position", true)
    private val aboveHead = BooleanSetting("AboveHead", "Place above head", true)
    private val aroundFeet = BooleanSetting("AroundFeet", "Place around feet", true)
    private val aroundHead = BooleanSetting("AroundHead", "Place around head", true)
    private val belowFeet = BooleanSetting("BelowFeet", "Place below feet", true)

    private var timer = 0
    private var lockedPos: BlockPos? = null
    private var renderPos: BlockPos? = null

    init {
        settings.add(range)
        settings.add(delay)
        settings.add(rotate)
        settings.add(pauseneat)
        settings.add(pauseonsneaking)
        settings.add(onlyOne)
        settings.add(aboveHead)
        settings.add(aroundFeet)
        settings.add(aroundHead)
        settings.add(belowFeet)

        SunnyClient.EVENT_BUS.subscribe<TickEvent>(TickEvent::class.java) {
            if (!enabled) return@subscribe
            onTick()
        }

    }

    override fun onTick() {


        val mc = MinecraftClient.getInstance()
        val player = mc.player ?: return
        val world = mc.world ?: return


        if (timer > 0) {
            timer--
            return
        }





        if (pauseneat.value && player.isUsingItem) return
        if (pauseonsneaking.value && player.isSneaking) return

        val target = TargetUtils.getClosestPlayer(range.value) ?: return

        val bestPos = resolvePosition(target.blockPos) ?: return
        renderPos = bestPos

        val state = world.getBlockState(bestPos)

        when {
            state.block != Blocks.RESPAWN_ANCHOR -> placeAnchor(bestPos)
            state.get(RespawnAnchorBlock.CHARGES) == 0 -> chargeAnchor(bestPos)
            else -> explodeAnchor(bestPos)
        }

        timer = delay.value.toInt()
    }




    private fun resolvePosition(base: BlockPos): BlockPos? {

        if (onlyOne.value && lockedPos != null) {
            return lockedPos
        }

        val positions = mutableListOf<BlockPos>()

        if (aboveHead.value) positions.add(base.up(2))

        if (aroundFeet.value) {
            positions.add(base.north())
            positions.add(base.south())
            positions.add(base.east())
            positions.add(base.west())
        }

        if (aroundHead.value) {
            positions.add(base.up().north())
            positions.add(base.up().south())
            positions.add(base.up().east())
            positions.add(base.up().west())
        }

        if (belowFeet.value) positions.add(base.down())

        for (pos in positions) {
            if (BlockUtils.canPlace(pos, range.value) ||
                MinecraftClient.getInstance().world?.getBlockState(pos)?.block == Blocks.RESPAWN_ANCHOR
            ) {
                if (onlyOne.value) lockedPos = pos
                return pos
            }
        }

        return null
    }

    private fun placeAnchor(pos: BlockPos) {

        val slot = InventoryUtils.findHotbar(Items.RESPAWN_ANCHOR) ?: return

        InventoryUtils.swap(slot)
        BlockUtils.interact(pos, rotate.value)
        InventoryUtils.swapBack()
    }

    private fun chargeAnchor(pos: BlockPos) {

        val slot = InventoryUtils.findHotbar(Items.GLOWSTONE) ?: return

        InventoryUtils.swap(slot)
        BlockUtils.interact(pos, rotate.value)
        InventoryUtils.swapBack()
    }

    private fun explodeAnchor(pos: BlockPos) {
        BlockUtils.interact(pos, rotate.value)
        if (onlyOne.value) lockedPos = null
    }

    private fun render() {

        val pos = renderPos ?: return

        RenderUtils.drawBox(pos, 255, 0, 0, 80)
    }



}
