package org.gatu.client.event


import net.minecraft.client.sound.SoundInstance

class PlaySoundEvent : Event() {
    var sound: SoundInstance? = null

    companion object {
        private val INSTANCE = PlaySoundEvent()

        fun get(sound: SoundInstance?): PlaySoundEvent {
            INSTANCE.sound = sound
            return INSTANCE
        }
    }
}