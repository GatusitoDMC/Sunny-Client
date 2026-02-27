package org.gatu.client.module.combat

import net.minecraft.client.MinecraftClient
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Hand
import org.gatu.client.SunnyClient
import org.gatu.client.event.events.TickEvent
import org.gatu.client.friend.FriendManager
import org.gatu.client.module.combat.CombatModule
import org.gatu.client.setting.BooleanSetting
import org.gatu.client.setting.NumberSetting
import org.gatu.client.util.RotateUtils
import org.gatu.client.util.RotationManager

class KillAura : CombatModule(
    "KillAura",
    "Attacks nearest entity automatically"
) {

    private val range = NumberSetting {
        name("Range")
        description("Attack range")
        default(4.0)
        min(1.0)
        max(6.0)
        increment(0.1)
    }

    private val rotate = BooleanSetting {
        name("Rotate")
        description("Rotate to target")
        default(true)
    }

    private val rotateSpeed = NumberSetting {
        name("RotateSpeed")
        description("Rotation speed")
        default(8.0)
        min(1.0)
        max(20.0)
        increment(1.0)
        visibility { rotate.value }
    }

    private val useDelay = BooleanSetting {
        name("Delay")
        description("Use attack delay")
        default(true)
    }

    private val delayTime = NumberSetting {
        name("DelayTime")
        description("Delay in ticks")
        default(10.0)
        min(1.0)
        max(20.0)
        increment(1.0)
        visibility { useDelay.value }
    }

    private var tickCounter = 0

    init {
        settings.add(range)
        settings.add(rotate)
        settings.add(rotateSpeed)
        settings.add(useDelay)
        settings.add(delayTime)

        SunnyClient.EVENT_BUS.subscribe(TickEvent::class.java) {
            if (!enabled) return@subscribe
            onTick()
        }
    }

    fun isLookingAt(targetYaw: Float, targetPitch: Float, threshold: Float = 5f): Boolean {
        val mc = MinecraftClient.getInstance()
        val player = mc.player ?: return false

        val yawDiff = RotateUtils.wrapAngle(player.yaw - targetYaw)
        val pitchDiff = player.pitch - targetPitch

        return Math.abs(yawDiff) <= threshold && Math.abs(pitchDiff) <= threshold
    }

    override fun onTick() {
        val mc = MinecraftClient.getInstance()
        val player = mc.player ?: return
        val world = mc.world ?: return

        tickCounter++

        val target = world.entities
            .filterIsInstance<LivingEntity>()
            .filter { it != player }
            .filter { it.isAlive }
            .filter { !FriendManager.isFriend(it.name.string) }
            .filter { player.distanceTo(it) <= range.value }
            .minByOrNull { player.distanceTo(it) }

        if (target == null) {
            RotationManager.reset()
            return
        }

        if (useDelay.value && tickCounter < delayTime.value.toInt()) return
        mc.interactionManager?.attackEntity(player, target)
        player.swingHand(Hand.MAIN_HAND)
        tickCounter = 0

        if (rotate.value) {
            val (targetYaw, targetPitch) = RotationManager.getRotations(target.eyePos)

            RotationManager.setRotation(targetYaw, targetPitch)

            mc.interactionManager?.attackEntity(player, target)
            player.swingHand(Hand.MAIN_HAND)
            tickCounter = 0

            if (!isLookingAt(targetYaw, targetPitch)) return
        }


    }


}
