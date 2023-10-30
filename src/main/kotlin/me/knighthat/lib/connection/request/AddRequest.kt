package me.knighthat.lib.connection.request

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.knighthat.lib.json.Json
import java.util.*
import java.util.function.Consumer

class AddRequest private constructor(
    uuid: UUID,
    target: Target,
) : TargetedRequest(RequestType.ADD, uuid, target, JsonArray()) {

    override val payload: JsonArray
        get() {
            val array = super.payload.asJsonArray
            return if (Json.isGzip(array))
                Json.gzipDecompress(array).asJsonArray
            else
                array
        }

    constructor(uuid: UUID, target: Target, payload: JsonArray) : this(uuid, target) {
        this.payload.addAll(payload)
    }

    constructor(uuid: UUID, target: Target, payload: Consumer<JsonArray>) : this(uuid, target) {
        payload.accept(this.payload)
    }

    override fun serialize(): JsonObject {
        val json = super.serialize()
        json.add("content", Json.gzipCompress(this.payload))

        return json
    }
}

