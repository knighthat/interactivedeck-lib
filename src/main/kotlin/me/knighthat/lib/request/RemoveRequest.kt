package me.knighthat.lib.request

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import java.util.*
import java.util.function.Consumer

class RemoveRequest(
        payload: JsonElement,
        uuid: UUID?,
        target: Target
) : TargetedRequest(RequestType.REMOVE, payload, uuid, target) {

    constructor(uuid: UUID, payload: JsonArray) : this(payload, uuid, Target.BUTTON)

    constructor(payload: Consumer<JsonArray>) : this(JsonArray(), null, Target.PROFILE) {
        payload.accept(this.payload.asJsonArray)
    }
}