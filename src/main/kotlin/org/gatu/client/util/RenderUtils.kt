package org.gatu.client.util

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box

object RenderUtils {

    fun drawBox(pos: BlockPos, r: Int, g: Int, b: Int, a: Int) {

        val mc = MinecraftClient.getInstance()
        val camera = mc.gameRenderer.camera.pos

        val matrices = MatrixStack()
        val vertexConsumers = mc.bufferBuilders.entityVertexConsumers
        val buffer = vertexConsumers.getBuffer(RenderLayer.getLines())

        val box = Box(pos).offset(
            -camera.x,
            -camera.y,
            -camera.z
        )

        val entry = matrices.peek()

        VertexRendering.drawBox(
            entry,
            buffer,
            box,
            r / 255f,
            g / 255f,
            b / 255f,
            a / 255f
        )

        vertexConsumers.draw()
    }
}
