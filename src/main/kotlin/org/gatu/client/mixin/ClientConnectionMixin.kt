package org.gatu.client.mixin

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import org.gatu.client.util.RotationManager
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.Mutable
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(PlayerMoveC2SPacket::class)
abstract class PlayerMoveC2SPacketMixin {

    @Shadow @Mutable protected var yaw: Float = 0f
    @Shadow @Mutable protected var pitch: Float = 0f

    @Inject(method = ["<init>"], at = [At("TAIL")])
    private fun onConstruct(ci: CallbackInfo) {

        if (RotationManager.yaw != null) {
            this.yaw = RotationManager.yaw!!
            this.pitch = RotationManager.pitch!!
            RotationManager.clear()
        }
    }
}
