package me.knighthat.lib.connection.request

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import java.util.*

open class TargetedRequest(
    type: RequestType,
    val uuid: UUID,
    val target: Target,
    payload: JsonElement
) : Request(type, payload), RequireConnection {

    override fun serialize(): JsonObject {
        val json = super.serialize()
        json.addProperty("target", target.name)
        json.add("uuid", JsonPrimitive(this.uuid.toString()))

        return json
    }

    enum class Target { BUTTON, PROFILE }
}