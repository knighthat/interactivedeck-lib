package me.knighthat.lib.request

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import java.util.*

open class TargetedRequest(
        type: RequestType,
        payload: JsonElement,
        val uuid: UUID?,
        val target: Target
) : Request(type, payload) {

    override fun serialize(): JsonObject {
        val uuid =
                if (this.uuid == null)
                    JsonNull.INSTANCE
                else
                    JsonPrimitive(this.uuid.toString())

        val json = super.serialize()
        json.addProperty("target", target.name)
        json.add("uuid", uuid)

        return json
    }

    enum class Target { BUTTON, PROFILE }
}