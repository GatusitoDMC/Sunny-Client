package org.gatu.client.module.movement

import net.minecraft.client.MinecraftClient
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import org.gatu.client.SunnyClient
import org.gatu.client.event.events.TickEvent
import org.gatu.client.module.movement.MovementModule
import org.gatu.client.setting.NumberSetting

class NoFall : MovementModule(
    "NoFall",
    "Prevents fall damage using packets"
) {

    private val distance = NumberSetting(
        "Distance",
        "Fall distance before spoof",
        2.5,
        1.0,
        5.0,
        0.5
    )

    init {
        settings.add(distance)

        SunnyClient.EVENT_BUS.subscribe(TickEvent::class.java) {
            if (!enabled) return@subscribe
            onTick()
        }
    }

    override fun onTick() {

        val mc = MinecraftClient.getInstance()
        val player = mc.player ?: return

        if (player.isGliding) return
        if (player.fallDistance > distance.value.toFloat()) {

            mc.networkHandler?.sendPacket(
                PlayerMoveC2SPacket.PositionAndOnGround(
                    player.x,
                    player.y,
                    player.z,
                    true,
                    player.horizontalCollision
                )
            )

            if (player.fallDistance > distance.value.toFloat()) {

            }
        }
    }


}
