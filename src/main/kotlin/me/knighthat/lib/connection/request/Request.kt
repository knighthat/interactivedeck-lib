package me.knighthat.lib.connection.request

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.knighthat.lib.connection.Connection
import me.knighthat.lib.connection.action.Action
import me.knighthat.lib.connection.wireless.WirelessSender
import me.knighthat.lib.exception.RequestException
import me.knighthat.lib.json.JsonSerializable
import me.knighthat.lib.logging.Log
import java.util.*

open class Request(
    val type: RequestType,
    open val payload: JsonElement
) : JsonSerializable {

    companion object {
        @JvmStatic
        @Throws(RequestException::class)
        fun fromJson(json: JsonObject): Request {
            if (!json.has("type"))
                throw RequestException("missing type!")
            if (!json.has("content"))
                throw RequestException("missing content!")

            val content = json["content"]

            return when (val type = RequestType.valueOf(json["type"].asString)) {
                RequestType.ADD,
                RequestType.REMOVE,
                RequestType.UPDATE -> TargetedRequest.fromJson(json, type, content)

                RequestType.PAIR   -> PairRequest(content)

                RequestType.ACTION -> ActionRequest(Action.fromJson(content))
            }
        }
    }

    init {
        Log.deb("Request to ${type.name} is created!")
    }

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