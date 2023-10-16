package me.knighthat.lib.connection.request

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.util.*
import java.util.function.Consumer

class UpdateRequest(
    payload: JsonElement,
    uuid: UUID?,
    target: Target
) : TargetedRequest(RequestType.UPDATE, payload, uuid, target) {

    constructor(uuid: UUID, payload: JsonObject) : this(payload, uuid, Target.BUTTON)

    constructor(payload: Consumer<JsonObject>) : this(JsonObject(), null, Target.PROFILE) {
        payload.accept(this.payload.asJsonObject)
    }
}