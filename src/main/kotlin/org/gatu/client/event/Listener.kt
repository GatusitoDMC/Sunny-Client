package org.gatu.client.event

fun interface Listener<T : Event> {
    fun call(event: T)
}
