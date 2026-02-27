package org.gatu.client.util

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.util.math.Box
import org.joml.Matrix4f

object Render3DUtil {

    fun drawBox(matrix: Matrix4f, box: Box, color: Int) {
        val mc = MinecraftClient.getInstance()
        val camera = mc.gameRenderer.camera
        val camPos = camera.pos

        val renderBox = box.offset(-camPos.x, -camPos.y, -camPos.z)

        val r = ((color shr 16) and 255) / 255f
        val g = ((color shr 8) and 255) / 255f
        val b = (color and 255) / 255f
        val a = ((color ushr 24) and 255) / 255f

        val consumers = mc.bufferBuilders.entityVertexConsumers
        val fillBuffer = consumers.getBuffer(RenderLayer.getDebugFilledBox())
        val lineBuffer = consumers.getBuffer(RenderLayer.getLines())

        drawFilledBox(fillBuffer, matrix, renderBox, r, g, b, a)
        drawOutlinedBox(lineBuffer, matrix, renderBox, r, g, b, a)

        consumers.draw()
    }

    private fun drawFilledBox(
        buffer: VertexConsumer,
        matrix: Matrix4f,
        box: Box,
        r: Float, g: Float, b: Float, a: Float
    ) {
        val minX = box.minX.toFloat()
        val minY = box.minY.toFloat()
        val minZ = box.minZ.toFloat()
        val maxX = box.maxX.toFloat()
        val maxY = box.maxY.toFloat()
        val maxZ = box.maxZ.toFloat()

        quad(buffer, matrix, r, g, b, a, 0f, 0f, -1f,
            minX, minY, minZ,
            minX, maxY, minZ,
            maxX, maxY, minZ,
            maxX, minY, minZ
        )
        quad(buffer, matrix, r, g, b, a, 0f, 0f, 1f,
            minX, minY, maxZ,
            maxX, minY, maxZ,
            maxX, maxY, maxZ,
            minX, maxY, maxZ
        )
        quad(buffer, matrix, r, g, b, a, -1f, 0f, 0f,
            minX, minY, minZ,
            minX, minY, maxZ,
            minX, maxY, maxZ,
            minX, maxY, minZ
        )
        quad(buffer, matrix, r, g, b, a, 1f, 0f, 0f,
            maxX, minY, minZ,
            maxX, maxY, minZ,
            maxX, maxY, maxZ,
            maxX, minY, maxZ
        )
        quad(buffer, matrix, r, g, b, a, 0f, -1f, 0f,
            minX, minY, minZ,
            maxX, minY, minZ,
            maxX, minY, maxZ,
            minX, minY, maxZ
        )
        // Top (+Y)
        quad(buffer, matrix, r, g, b, a, 0f, 1f, 0f,
            minX, maxY, minZ,
            minX, maxY, maxZ,
            maxX, maxY, maxZ,
            maxX, maxY, minZ
        )
    }

    private fun drawOutlinedBox(
        buffer: VertexConsumer,
        matrix: Matrix4f,
        box: Box,
        r: Float, g: Float, b: Float, a: Float
    ) {
        val minX = box.minX.toFloat()
        val minY = box.minY.toFloat()
        val minZ = box.minZ.toFloat()
        val maxX = box.maxX.toFloat()
        val maxY = box.maxY.toFloat()
        val maxZ = box.maxZ.toFloat()

        line(buffer, matrix, minX, minY, minZ, maxX, minY, minZ, r, g, b, a)
        line(buffer, matrix, maxX, minY, minZ, maxX, minY, maxZ, r, g, b, a)
        line(buffer, matrix, maxX, minY, maxZ, minX, minY, maxZ, r, g, b, a)
        line(buffer, matrix, minX, minY, maxZ, minX, minY, minZ, r, g, b, a)

        line(buffer, matrix, minX, maxY, minZ, maxX, maxY, minZ, r, g, b, a)
        line(buffer, matrix, maxX, maxY, minZ, maxX, maxY, maxZ, r, g, b, a)
        line(buffer, matrix, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, a)
        line(buffer, matrix, minX, maxY, maxZ, minX, maxY, minZ, r, g, b, a)

        line(buffer, matrix, minX, minY, minZ, minX, maxY, minZ, r, g, b, a)
        line(buffer, matrix, maxX, minY, minZ, maxX, maxY, minZ, r, g, b, a)
        line(buffer, matrix, maxX, minY, maxZ, maxX, maxY, maxZ, r, g, b, a)
        line(buffer, matrix, minX, minY, maxZ, minX, maxY, maxZ, r, g, b, a)
    }

    private fun line(
        buffer: VertexConsumer,
        matrix: Matrix4f,
        x1: Float, y1: Float, z1: Float,
        x2: Float, y2: Float, z2: Float,
        r: Float, g: Float, b: Float, a: Float
    ) {
        val dx = x2 - x1
        val dy = y2 - y1
        val dz = z2 - z1
        val length = kotlin.math.sqrt((dx * dx + dy * dy + dz * dz).toDouble()).toFloat()

        val nx: Float
        val ny: Float
        val nz: Float
        if (length > 1.0e-6f) {
            nx = dx / length
            ny = dy / length
            nz = dz / length
        } else {
            nx = 0.0f
            ny = 1.0f
            nz = 0.0f
        }

        buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a).normal(nx, ny, nz)
        buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a).normal(nx, ny, nz)
    }

    private fun quad(
        buffer: VertexConsumer,
        matrix: Matrix4f,
        r: Float, g: Float, b: Float, a: Float,
        nx: Float, ny: Float, nz: Float,
        x1: Float, y1: Float, z1: Float,
        x2: Float, y2: Float, z2: Float,
        x3: Float, y3: Float, z3: Float,
        x4: Float, y4: Float, z4: Float
    ) {
        buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a).normal(nx, ny, nz)
        buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a).normal(nx, ny, nz)
        buffer.vertex(matrix, x3, y3, z3).color(r, g, b, a).normal(nx, ny, nz)
        buffer.vertex(matrix, x4, y4, z4).color(r, g, b, a).normal(nx, ny, nz)
    }
}
