package org.gatu.client.command

abstract class Command(
    val name: String,
    val description: String
) {
    abstract fun execute(args: List<String>)
}
