package me.knighthat.lib.connection.request

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import me.knighthat.lib.exception.RequestException
import me.knighthat.lib.json.JsonSerializable
import java.util.*

open class TargetedRequest(
    type: RequestType,
    val uuid: UUID,
    val target: Target,
    payload: JsonElement
) : Request(type, payload), RequireConnection {

    companion object {
        /**
         * Parses request from [JsonObject].
         * Request must have [uuid] and [target] in serialized string,
         * and they are non-null variables.
         *
         * @param json    [JsonObject] represents a valid request
         * @param type    type of request
         * @param payload needed content of the request
         *
         * @return a subclass of [TargetedRequest] matches provided **type**
         *
         * @throws RequestException     if [target] or [uuid] is not present in json
         * @throws NullPointerException if [target] or [uuid] is null
         *
         * @see [Target]
         * @see [JsonSerializable]
         */
        @JvmStatic
        @Throws(RequestException::class)
        fun fromJson(json: JsonObject, type: RequestType, payload: JsonElement): TargetedRequest {
            if (!json.has("target"))
                throw RequestException("missing target!")
            if (!json.has("uuid"))
                throw RequestException("missing uuid!")

            val uuid = UUID.fromString(json["uuid"].asString)
            val target = Target.valueOf(json["target"].asString)

            return when (type) {
                RequestType.ADD    -> AddRequest(uuid, target, payload.asJsonArray)
                RequestType.REMOVE -> RemoveRequest(uuid, target, payload.asJsonArray)
                RequestType.UPDATE -> UpdateRequest(uuid, target, payload.asJsonObject)
                else               -> throw RequestException("unknown request type $type")
            }
        }
    }

    override fun serialize(): JsonObject {
        val json = super.serialize()
        json.addProperty("target", target.name)
        json.add("uuid", JsonPrimitive(this.uuid.toString()))

        return json
    }

    enum class Target { BUTTON, PROFILE }
}