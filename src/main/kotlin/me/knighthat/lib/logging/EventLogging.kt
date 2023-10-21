package me.knighthat.lib.logging

interface EventLogging {

    fun sendUpdate(property: String, oldValue: Any, newValue: Any)

    fun logUpdate(property: String, oldValue: Any, newValue: Any)

    fun logAndSendUpdate(property: String, oldValue: Any, newValue: Any) {
        logUpdate(property, oldValue, newValue)
        sendUpdate(property, oldValue, newValue)
    }
}