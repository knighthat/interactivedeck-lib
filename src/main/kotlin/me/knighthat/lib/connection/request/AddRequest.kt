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
) : TargetedRequest(RequestType.ADD, payload, uuid, target) {

    constructor(uuid: UUID, payload: JsonArray) : this(JsonArray(), uuid, Target.BUTTON) {
        this.payload
            .asJsonArray
            .addAll(
                if (Json.isGzip(payload)) {
                    val array = payload.asJsonArray
                    Json.gzipDecompress(array).asJsonArray
                } else
                    payload
            )
    }

    constructor(payload: Consumer<JsonArray>) : this(JsonArray(), null, Target.PROFILE) {
        var content = JsonArray()
        payload.accept(content)

        if (Json.isGzip(content))
            content = Json.gzipDecompress(content).asJsonArray

        this.payload.asJsonArray.addAll(content)
    }

    override fun serialize(): JsonObject {
        val json = super.serialize()
        json.add("content", Json.gzipCompress(this.payload))

        return json
    }
}

