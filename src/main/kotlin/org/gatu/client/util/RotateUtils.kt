package org.gatu.client.util

import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import kotlin.math.*

object RotateUtils {

    fun getRotations(target: Vec3d): Pair<Float, Float> {
        val mc = MinecraftClient.getInstance()
        val player = mc.player ?: return Pair(0f, 0f)

        val diffX = target.x - player.x
        val diffY = target.y - (player.y + player.standingEyeHeight)
        val diffZ = target.z - player.z

        val diffXZ = sqrt(diffX * diffX + diffZ * diffZ)

        val yaw = Math.toDegrees(atan2(diffZ, diffX)).toFloat() - 90f
        val pitch = (-Math.toDegrees(atan2(diffY, diffXZ))).toFloat()

        return Pair(wrapAngle(yaw), wrapAngle(pitch))
    }

    fun wrapAngle(angle: Float): Float {
        var wrapped = angle % 360f
        if (wrapped >= 180f) wrapped -= 360f
        if (wrapped < -180f) wrapped += 360f
        return wrapped
    }

    fun getDistanceToEntity(target: Vec3d): Double {
        val mc = MinecraftClient.getInstance()
        val player = mc.player ?: return 0.0
        return player.eyePos.distanceTo(target)
    }
}