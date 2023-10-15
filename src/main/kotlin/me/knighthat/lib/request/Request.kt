package me.knighthat.lib.request

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import me.knighthat.lib.exception.RequestException
import me.knighthat.lib.json.JsonSerializable
import java.util.*

open class Request(
    val type: RequestType,
    val payload: JsonElement
) : JsonSerializable {

    companion object {
        @JvmStatic
        @Throws(RequestException::class)
        fun fromJson(element: JsonElement): Request {
            if (!element.isJsonObject)
                throw RequestException("request must be ${JsonObject::class.qualifiedName} not ${element::class.qualifiedName}")

            val json = element.asJsonObject

            if (!json.has("type"))
                throw RequestException("missing type!")
            if (!json.has("content"))
                throw RequestException("missing content!")

            val content = json["content"]
            val typeStr = json["type"].asString
            val type = RequestType.valueOf(typeStr)

            var uuid: UUID? = null
            var target: TargetedRequest.Target? = null
            if (type == RequestType.ADD ||
                type == RequestType.REMOVE ||
                type == RequestType.UPDATE
            ) {
                if (!json.has("uuid"))
                    throw RequestException("missing uuid!")
                if (!json.has("target"))
                    throw RequestException("missing target!")

                if (json["uuid"] != JsonNull.INSTANCE) {
                    val uuidStr = json["uuid"].asString
                    uuid = UUID.fromString(uuidStr)
                }

                val targetStr = json["target"].asString
                target = TargetedRequest.Target.valueOf(targetStr)
            }

            return when (type) {
                RequestType.ADD, RequestType.REMOVE, RequestType.UPDATE ->
                    TargetedRequest(type, content, uuid, target!!)

                else -> Request(type, content)
            }
        }
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