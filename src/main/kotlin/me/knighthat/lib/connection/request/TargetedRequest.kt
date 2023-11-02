package me.knighthat.lib.connection.request

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.knighthat.lib.exception.RequestException
import org.jetbrains.annotations.Contract
import java.util.*

open class TargetedRequest(
    type: RequestType,
    val target: Target,
    payload: JsonElement
) : Request(type, payload), RequireConnection {

    companion object {
        /**
         * Parses request from [JsonObject].
         * Request must have [target] in serialized string,
         * and it is non-null variables.
         *
         * @param json [JsonObject] represents a valid request
         *
         * @return a subclass of [TargetedRequest] matches provided **type**
         *
         * @throws RequestException if [target] is not present in json
         */
        @JvmStatic
        @Throws(RequestException::class)
        @Contract(pure = true)
        fun fromJson(json: JsonObject): TargetedRequest {
            verify(json, "type", "content", "target")

            val target = Target.valueOf(json["target"].asString)
            val payload = json["content"]

            return when (RequestType.valueOf(json["type"].asString)) {

                RequestType.ADD    -> AddRequest(target, payload.asJsonArray)
                RequestType.REMOVE -> RemoveRequest(target, payload.asJsonArray)
                else               -> throw RequestException("not targeted request!")

            }
        }
    }

    override fun serialize(): JsonObject {
        val json = super.serialize()
        json.addProperty("target", target.name)

        return json
    }

    enum class Target { BUTTON, PROFILE }
}