package org.gatu.client.util

import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import kotlin.math.*

object RotationManager {
    private var serverYaw = 0f
    private var serverPitch = 0f
    private var isRotating = false

    fun setRotation(yaw: Float, pitch: Float) {
        serverYaw = yaw
        serverPitch = pitch
        isRotating = true
    }

    fun reset() {
        isRotating = false
    }

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

    fun updateServerRotations() {
        if (!isRotating) return

        val mc = MinecraftClient.getInstance()
        val player = mc.player ?: return

        player.headYaw = serverYaw
        player.bodyYaw = serverYaw
    }

    fun getServerYaw(): Float = serverYaw
    fun getServerPitch(): Float = serverPitch
    fun isActive(): Boolean = isRotating
}