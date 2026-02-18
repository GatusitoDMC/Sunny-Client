package org.gatu.client.util

import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.Entity
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

object BlockUtils {

    fun canPlace(pos: BlockPos, range: Double): Boolean {

        val mc = MinecraftClient.getInstance()
        val player = mc.player ?: return false
        val world = mc.world ?: return false

        if (player.eyePos.distanceTo(Vec3d.ofCenter(pos)) > range) return false

        val state: BlockState = world.getBlockState(pos)

        if (!state.isReplaceable) return false

        val box = Box(pos)

        val entities = world.getOtherEntities(null, box) {
            it !is net.minecraft.entity.ItemEntity &&
                    it !is net.minecraft.entity.ExperienceOrbEntity
        }

        return entities.isEmpty()
    }

    fun interact(pos: BlockPos, rotate: Boolean = false) {

        val mc = MinecraftClient.getInstance()
        val player = mc.player ?: return

        if (rotate) {
            val (yaw, pitch) = RotateUtils.getRotations(Vec3d.ofCenter(pos))
            RotationManager.setRotation(yaw, pitch)
        }

        val hit = BlockHitResult(
            Vec3d.ofCenter(pos),
            Direction.UP,
            pos,
            false
        )

        mc.interactionManager?.interactBlock(player, Hand.MAIN_HAND, hit)
    }
}
