package me.knighthat.lib.connection.request

import com.google.gson.JsonObject
import java.util.*
import java.util.function.Consumer

class UpdateRequest(
    uuid: UUID,
    target: Target,
    override val payload: JsonObject
) : TargetedRequest(RequestType.UPDATE, uuid, target, payload) {

    constructor(uuid: UUID, target: Target, payload: Consumer<JsonObject>) : this(uuid, target, JsonObject()) {
        payload.accept(this.payload)
    }
}