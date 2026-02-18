package org.gatu.client.util

import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.Vec3d
import kotlin.math.atan2
import kotlin.math.sqrt

object RotateUtils {

    fun getRotations(target: Vec3d): Pair<Float, Float> {

        val player = MinecraftClient.getInstance().player ?: return 0f to 0f

        val diffX = target.x - player.x
        val diffY = target.y - (player.y + player.eyeY)
        val diffZ = target.z - player.z

        val dist = sqrt(diffX * diffX + diffZ * diffZ)

        val yaw = Math.toDegrees(atan2(diffZ, diffX)) - 90.0
        val pitch = -Math.toDegrees(atan2(diffY, dist))

        return yaw.toFloat() to pitch.toFloat()
    }
}
