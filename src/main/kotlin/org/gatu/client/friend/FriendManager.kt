package org.gatu.client.friend

object FriendManager {

    private val friends = mutableSetOf<String>()

    fun add(name: String) {
        friends.add(name.lowercase())
    }

    fun remove(name: String) {
        friends.remove(name.lowercase())
    }

    fun isFriend(name: String): Boolean {
        return friends.contains(name.lowercase())
    }

    fun getAll(): Set<String> = friends

    fun clear() {
        friends.clear()
    }
}
