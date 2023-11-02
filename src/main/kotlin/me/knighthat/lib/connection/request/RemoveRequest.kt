package me.knighthat.lib.connection.request

import com.google.gson.JsonArray
import me.knighthat.lib.component.Identifiable
import java.util.function.Consumer

class RemoveRequest(
    target: Target,
    override val payload: JsonArray
) : TargetedRequest(RequestType.REMOVE, target, payload) {

    constructor(target: Target, payload: Consumer<JsonArray>) : this(target, JsonArray()) {
        payload.accept(this.payload)
    }

    constructor(target: Target, payload: Array<out Identifiable>) : this(target, JsonArray()) {
        payload.forEach { this.payload.add(it.uuid.toString()) }
    }
}