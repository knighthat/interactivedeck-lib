package me.knighthat.lib.connection.request

import com.google.gson.JsonArray
import java.util.*
import java.util.function.Consumer

class RemoveRequest(
    uuid: UUID,
    target: Target,
    override val payload: JsonArray
) : TargetedRequest(RequestType.REMOVE, uuid, target, payload) {

    constructor(uuid: UUID, target: Target, payload: Consumer<JsonArray>) : this(uuid, target, JsonArray()) {
        payload.accept(this.payload)
    }
}