package me.knighthat.lib.connection.request

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.knighthat.lib.json.Json
import java.util.*
import java.util.function.Consumer

class AddRequest(
    payload: JsonElement,
    uuid: UUID?,
    target: Target
) : TargetedRequest(RequestType.ADD, JsonArray(), uuid, target) {

    init {
        this.payload
            .asJsonArray
            .addAll(
                if (Json.isGzip(payload)) {
                    val array = payload.asJsonArray
                    Json.gzipDecompress(array).asJsonArray
                } else
                    payload.asJsonArray
            )
    }

    constructor(uuid: UUID, payload: JsonArray) : this(payload, uuid, Target.BUTTON)

    constructor(payload: Consumer<JsonArray>) : this(JsonArray(), null, Target.PROFILE) {
        payload.accept(this.payload.asJsonArray)
    }

    override fun serialize(): JsonObject {
        val json = super.serialize()
        json.add("content", Json.gzipCompress(this.payload))

        return json
    }
}

