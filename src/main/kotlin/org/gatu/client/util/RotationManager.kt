package org.gatu.client.util

object RotationManager {

    var yaw: Float? = null
    var pitch: Float? = null

    fun setRotation(newYaw: Float, newPitch: Float) {
        yaw = newYaw
        pitch = newPitch
    }

    fun clear() {
        yaw = null
        pitch = null
    }
}
