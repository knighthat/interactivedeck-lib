package me.knighthat.lib.connection.request

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.knighthat.lib.connection.Connection
import me.knighthat.lib.connection.action.Action
import me.knighthat.lib.connection.wireless.WirelessSender
import me.knighthat.lib.exception.RequestException
import me.knighthat.lib.json.JsonSerializable
import me.knighthat.lib.logging.Log
import org.jetbrains.annotations.Contract
import java.util.*

open class Request(
    val type: RequestType,
    open val payload: JsonElement
) : JsonSerializable {

    companion object {
        /**
         * Parses request from [JsonObject].
         * Request must have [type] and [payload].
         * If request's type is ADD or REMOVE,
         * then the conversion will be re-direct to [TargetedRequest.fromJson].
         * UPDATE request will be sent to [UpdateRequest.fromJson]
         *
         * @param json [JsonObject] represents a valid request
         *
         * @return a subclass of [Request]
         *
         * @throws RequestException if [type] or [payload] is missing from json string
         */
        @JvmStatic
        @Throws(RequestException::class)
        @Contract(pure = true)
        fun fromJson(json: JsonObject): Request {
            verify(json, "type", "content")

            val content = json["content"]

            return when (RequestType.valueOf(json["type"].asString)) {
                RequestType.ADD,
                RequestType.REMOVE -> TargetedRequest.fromJson(json)

                RequestType.UPDATE -> UpdateRequest.fromJson(json)

                RequestType.PAIR   -> PairRequest(content)

                RequestType.ACTION -> ActionRequest(Action.fromJson(content))
            }
        }

        /**
         * Scans through [JsonObject] and checks if it contains provided keys,
         * if not, [RequestException] will be thrown.
         *
         * @throws [RequestException] when a key is not found
         */
        fun verify(json: JsonObject, vararg requirements: String) {

            for (req in requirements)
                if (!json.has(req))
                    throw RequestException("missing $req!")

        }
    }

    init {
        Log.deb("Request to ${type.name} is created!")
    }

    /**
     * Adds itself to sending queue.
     * But only when connection is established and is connected to a client.
     *
     * @see [Connection]
     */
    fun send() {
        if (Connection.isConnected())
            WirelessSender.send(this)
    }

    override fun serialize(): JsonObject {
        val json = JsonObject()
        json.addProperty("type", type.toString())
        json.add("content", payload)
        return json
    }

    override fun toString(): String = serialize().toString()

    enum class RequestType { ADD, REMOVE, UPDATE, PAIR, ACTION }
}