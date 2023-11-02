package me.knighthat.lib.connection.request

import com.google.gson.JsonObject
import me.knighthat.lib.exception.RequestException
import org.jetbrains.annotations.Contract
import java.util.*
import java.util.function.Consumer

class UpdateRequest(
    val uuid: UUID,
    target: Target,
    override val payload: JsonObject
) : TargetedRequest(RequestType.UPDATE, target, payload) {

    companion object {
        /**
         * Parses request from [JsonObject].
         * Request must have [uuid] and [target] in serialized string,
         * and they are non-null variables.
         *
         * @param json [JsonObject] represents a valid request
         *
         * @return [UpdateRequest] that contains values from input json
         *
         * @throws RequestException if [type], [payload], [target], or [uuid] is missing
         * or when [type] isn't [Request.RequestType.UPDATE]
         */
        @JvmStatic
        @Throws(RequestException::class)
        @Contract(pure = true)
        fun fromJson(json: JsonObject): UpdateRequest {
            verify(json, "type", "content", "target", "uuid")

            if (RequestType.valueOf(json["type"].asString) != RequestType.UPDATE)
                throw RequestException("not update request!")

            val uuid = UUID.fromString(json["uuid"].asString)
            val target = Target.valueOf(json["target"].asString)
            val payload = json["content"].asJsonObject
            return UpdateRequest(uuid, target, payload)
        }
    }

    constructor(uuid: UUID, target: Target, payload: Consumer<JsonObject>) : this(uuid, target, JsonObject()) {
        payload.accept(this.payload)
    }

    override fun serialize(): JsonObject {
        val json = super.serialize()
        json.addProperty("uuid", uuid.toString())

        return json
    }
}