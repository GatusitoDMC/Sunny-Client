package org.gatu.client.util

import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import org.gatu.client.friend.FriendManager

object TargetUtils {

    fun getClosestPlayer(range: Double): PlayerEntity? {

        val mc = MinecraftClient.getInstance()
        val player = mc.player ?: return null
        val world = mc.world ?: return null

        return world.players
            .filter { it != player }
            .filter { it.isAlive }
            .filter { !FriendManager.isFriend(it.name.string) }
            .filter { player.distanceTo(it) <= range }
            .minByOrNull { player.distanceTo(it) }
    }
}
