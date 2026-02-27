package org.gatu.client.mixin

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import org.gatu.client.module.misc.CustomPoPSound
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(ClientPlayNetworkHandler::class)
class MixinClientPlayNetworkHandler {

    @Inject(method = ["onEntityStatus"], at = [At("HEAD")], cancellable = true)
    private fun onEntityStatus(packet: EntityStatusS2CPacket, ci: CallbackInfo) {
        if (packet.status.toInt() != 35) return

        val module = CustomPoPSound.INSTANCE ?: return
        if (!module.enabled) return

        val client = MinecraftClient.getInstance()
        val world = client.world ?: return
        val entity = packet.getEntity(world) ?: return

        val soundEvent = when (module.sound.value) {
            CustomPoPSound.sounds.tnt -> SoundEvents.ENTITY_TNT_PRIMED
            CustomPoPSound.sounds.anvil -> SoundEvents.BLOCK_ANVIL_LAND
            CustomPoPSound.sounds.totem -> SoundEvents.ITEM_TOTEM_USE
        }

        ci.cancel()

        world.playSound(
            client.player,
            entity.blockPos,
            soundEvent,
            SoundCategory.PLAYERS,
            100.0f,
            1.0f
        )
    }
}