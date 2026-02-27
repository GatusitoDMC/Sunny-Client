package org.gatu.client.mixin

import com.mojang.blaze3d.buffers.GpuBufferSlice
import net.minecraft.client.render.Camera
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.util.ObjectAllocator
import org.gatu.client.SunnyClient
import org.gatu.client.event.Render3DEvent
import org.joml.Matrix4f
import org.joml.Vector4f
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(WorldRenderer::class)
class MixinWorldRenderer {

    @Inject(method = ["render"], at = [At("TAIL")])
    private fun onRender(
        allocator: ObjectAllocator,
        tickCounter: RenderTickCounter,
        renderBlockOutline: Boolean,
        camera: Camera,
        positionMatrix: Matrix4f,
        projectionMatrix: Matrix4f,
        frustumMatrix: Matrix4f,
        gpuSlice: GpuBufferSlice,
        fogColor: Vector4f,
        renderHand: Boolean,
        ci: CallbackInfo
    ) {
        SunnyClient.EVENT_BUS.post(Render3DEvent(positionMatrix))
    }
}