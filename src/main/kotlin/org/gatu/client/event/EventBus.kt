package org.gatu.client.event

class EventBus {

    private val listeners =
        mutableMapOf<Class<*>, MutableList<Listener<*>>>()

    fun <T : Event> subscribe(eventClass: Class<T>, listener: Listener<T>) {
        listeners.computeIfAbsent(eventClass) { mutableListOf() }
            .add(listener)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Event> post(event: T) {

        val list = listeners[event::class.java]?.toList() ?: return

        for (listener in list) {
            (listener as Listener<T>).call(event)
        }
    }
}
