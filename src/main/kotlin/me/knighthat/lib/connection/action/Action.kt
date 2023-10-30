package me.knighthat.lib.connection.action

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.knighthat.lib.exception.ActionException
import me.knighthat.lib.json.JsonSerializable
import java.util.*

class Action(
    val uuid: UUID,
    val type: ActionType
) : JsonSerializable {

    companion object {
        @JvmStatic
        fun fromJson(element: JsonElement): Action {
            if (!element.isJsonObject)
                throw IllegalStateException("invalid format!")

            val json = element.asJsonObject

            if (!json.has("uuid"))
                throw ActionException("missing uuid!")
            if (!json.has("type"))
                throw ActionException("missing type!")

            val uuid = UUID.fromString(json["uuid"].asString)
            val type = ActionType.valueOf(json["type"].asString)

            return Action(uuid, type)
        }
    }

    override fun serialize(): JsonObject {
        val json = JsonObject()

        json.addProperty("uuid", uuid.toString())
        json.addProperty("type", type.name)

        return json
    }

    enum class ActionType { PRESS }
}