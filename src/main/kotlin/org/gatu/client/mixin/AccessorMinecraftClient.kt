package org.gatu.client.mixin

import net.minecraft.client.MinecraftClient
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(MinecraftClient::class)
interface AccessorMinecraftClient {
    @Accessor("itemUseCooldown")
    fun `sunny$setItemUseCooldown`(itemUseCooldown: Int)
}