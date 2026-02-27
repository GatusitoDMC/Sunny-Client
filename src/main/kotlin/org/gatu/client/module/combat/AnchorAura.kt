package org.gatu.client.module.combat

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRendering.render
import net.minecraft.block.Blocks
import net.minecraft.block.RespawnAnchorBlock
import net.minecraft.client.MinecraftClient
import net.minecraft.item.Items
import net.minecraft.util.math.BlockPos
import org.gatu.client.SunnyClient
import org.gatu.client.event.Render3DEvent
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

    private val range = NumberSetting {
        name("Range")
        description("Place range")
        default(4.5)
        min(1.0)
        max(6.0)
        increment(0.1)
    }

    private val delay = NumberSetting {
        name("Delay")
        description("Tick delay")
        default(5.0)
        min(0.0)
        max(20.0)
        increment(1.0)
    }

    private val rotate = BooleanSetting {
        name("Rotate")
        description("Silent rotate")
        default(true)
    }

    private val pauseOnEat = BooleanSetting {
        name("Pause On Eat")
        description("Pause on eating")
        default(true)
    }

    private val pauseOnSneaking = BooleanSetting {
        name("Pause On Sneaking")
        description("Pause on sneaking")
        default(true)
    }

    private val aboveHead = BooleanSetting {
        name("AboveHead")
        description("Place above head")
        default(true)
    }

    private val aroundFeet = BooleanSetting {
        name("AroundFeet")
        description("Place around feet")
        default(true)
    }

    private val aroundHead = BooleanSetting {
        name("AroundHead")
        description("Place around head")
        default(true)
    }

    private val belowFeet = BooleanSetting {
        name("BelowFeet")
        description("Place below feet")
        default(true)
    }

    private val pauseneat = BooleanSetting {
        name("Pause On eat")
        description("Pause on eating")
        default(true)
    }

    private var timer = 0
    private var lockedPos: BlockPos? = null
    private var renderPos: BlockPos? = null

    init {
        settings.add(range)
        settings.add(delay)
        settings.add(rotate)
        settings.add(pauseneat)
        settings.add(aboveHead)
        settings.add(aroundFeet)
        settings.add(aroundHead)
        settings.add(belowFeet)

        SunnyClient.EVENT_BUS.subscribe<TickEvent>(TickEvent::class.java) {
            if (!enabled) return@subscribe
            onTick()
        }
        SunnyClient.EVENT_BUS.subscribe(Render3DEvent::class.java) { event ->
            if (!enabled) return@subscribe
            onRender3D(event)
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
    }

    private fun onRender3D(event: Render3DEvent) {
        val pos = renderPos ?: return
        val box = net.minecraft.util.math.Box(pos)

        Render3DUtil.drawBox(event.matrix, box, 0x80FF0000.toInt())
    }





}
