package me.knighthat.lib.component

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import me.knighthat.lib.connection.request.RequestJson
import me.knighthat.lib.connection.request.TargetedRequest
import me.knighthat.lib.connection.request.UpdateRequest
import me.knighthat.lib.json.JsonSerializable
import me.knighthat.lib.logging.EventLogging

interface LiveComponent : RealtimeProperty, Identifiable, EventLogging {

    val target: TargetedRequest.Target

    override fun sendUpdate(property: String, oldValue: Any?, newValue: Any?) {
        val json = JsonObject()

        if (newValue == null) {
            json.add(property, JsonNull.INSTANCE)
        } else
            when (newValue) {
                is JsonSerializable -> json.add(property, newValue.serialize())
                is RequestJson      -> json.add(property, newValue.toRequest())
                is JsonElement      -> json.add(property, newValue)
                is Number           -> json.addProperty(property, newValue)
                is Boolean          -> json.addProperty(property, newValue)
                is Char             -> json.addProperty(property, newValue)
                is String           -> json.addProperty(property, newValue)
                else                -> throw IllegalArgumentException("Unsupported JSON type " + newValue::class.qualifiedName)
            }

        UpdateRequest(uuid, target, json).send()
    }
}