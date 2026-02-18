package org.gatu.client.module.movement

import net.minecraft.client.MinecraftClient
import org.gatu.client.SunnyClient
import org.gatu.client.event.events.TickEvent
import org.gatu.client.module.movement.MovementModule
import org.gatu.client.setting.NumberSetting

class Fly : MovementModule(
    "Fly",
    "Allows you to fly"
) {
    private val Speed = NumberSetting(
        "Speed",
        "Speed of Flying",
        1.0,
        0.1,
        10.0,
        0.1
    )

    init {
        settings.add(Speed)
        SunnyClient.EVENT_BUS.subscribe(TickEvent::class.java) {
            if (!enabled) return@subscribe
            onTick()
        }
    }

    override fun onEnable() {
        val mc = MinecraftClient.getInstance()
        mc.player?.abilities?.allowFlying = true
    }

    override fun onDisable() {
        val mc = MinecraftClient.getInstance()
        mc.player?.let { player ->
            player.abilities.allowFlying = false
            player.abilities.flying = false
        }
    }

    override fun onTick() {
        val mc = MinecraftClient.getInstance()
        val player = mc.player ?: return


        player.abilities.allowFlying = true


        val speedDouble = try {
            Speed.value as? Double ?: 1.0
        } catch (e: Exception) {
            1.0
        }
        val baseSpeed = speedDouble * 0.1
        val yawRad = Math.toRadians(player.yaw.toDouble())

        var dx = 0.0
        var dz = 0.0

        val options = mc.options

        if (options.forwardKey.isPressed) {
            dx += -Math.sin(yawRad)
            dz += Math.cos(yawRad)
        }
        if (options.backKey.isPressed) {
            dx += Math.sin(yawRad)
            dz += -Math.cos(yawRad)
        }
        if (options.leftKey.isPressed) {
            dx += -Math.cos(yawRad)
            dz += -Math.sin(yawRad)
        }
        if (options.rightKey.isPressed) {
            dx += Math.cos(yawRad)
            dz += Math.sin(yawRad)
        }

        val horizLen = Math.hypot(dx, dz)
        if (horizLen > 0.0) {
            dx = dx / horizLen * baseSpeed
            dz = dz / horizLen * baseSpeed
        } else {
            dx = 0.0
            dz = 0.0
        }

        val dy = when {
            options.jumpKey.isPressed -> baseSpeed
            options.sneakKey.isPressed -> -baseSpeed
            else -> 0.0
        }

        player.setVelocity(dx, dy, dz)
    }
}