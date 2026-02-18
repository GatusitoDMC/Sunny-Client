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

class KillAura : CombatModule(
    "KillAura",
    "Attacks nearest entity automatically"
) {

    private val range = NumberSetting(
        "Range",
        "Attack range",
        4.0,
        1.0,
        6.0,
        0.1
    )
    private val Rotate = BooleanSetting(
        "Rotate",
        "Rotate to target",
        true
    )

    private val rotateSpeed = NumberSetting(
        "RotateSpeed",
        "Rotation speed",
        8.0,
        1.0,
        20.0,
        1.0
    )


    private val useDelay = BooleanSetting(
        "Delay",
        "Use attack delay",
        true
    )

    private val delayTime = NumberSetting(
        "DelayTime",
        "Delay in ticks",
        10.0,
        1.0,
        20.0,
        1.0
    )

    private var tickCounter = 0

    init {
        settings.add(range)
        settings.add(Rotate)
        settings.add(rotateSpeed)
        settings.add(useDelay)
        settings.add(delayTime)

        SunnyClient.EVENT_BUS.subscribe(TickEvent::class.java) {
            if (!enabled) return@subscribe
            onTick()
        }
    }

    override fun onTick() {

        val mc = MinecraftClient.getInstance()
        val player = mc.player ?: return
        val world = mc.world ?: return

        tickCounter++

        if (useDelay.value) {
            if (tickCounter < delayTime.value.toInt()) return
        }

        val target = world.entities
            .filterIsInstance<LivingEntity>()
            .filter { it != player }
            .filter { it.isAlive }
            .filter { !FriendManager.isFriend(it.name.string) }
            .minByOrNull { player.distanceTo(it) }

        if (target != null && player.distanceTo(target) <= range.value) {

            if (Rotate.value) {

                val (targetYaw, targetPitch) =
                    org.gatu.client.util.RotateUtils.getRotations(target.eyePos)


                org.gatu.client.util.RotationManager.setRotation(
                    targetYaw,
                    targetPitch
                )
            }

            mc.interactionManager?.attackEntity(player, target)
            player.swingHand(Hand.MAIN_HAND)

            tickCounter = 0
        }
    }
}
