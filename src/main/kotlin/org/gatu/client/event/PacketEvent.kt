package org.gatu.client.event.events

import net.minecraft.network.packet.Packet
import org.gatu.client.event.Event

class PacketEvent<T : Packet<*>>(val packet: T) : Event()
