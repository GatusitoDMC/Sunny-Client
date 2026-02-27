package org.gatu.client.mixin

import net.minecraft.client.network.ClientPlayerEntity
import org.gatu.client.util.RotationManager.isActive
import org.gatu.client.util.RotationManager.updateServerRotations
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(ClientPlayerEntity::class)
class MixinClientPlayerEntity {
    @Inject(method = ["sendMovementPackets"], at = [At("HEAD")])
    private fun onSendMovementPackets(ci: CallbackInfo?) {
        if (isActive()) {
            updateServerRotations()
        }
    }
}