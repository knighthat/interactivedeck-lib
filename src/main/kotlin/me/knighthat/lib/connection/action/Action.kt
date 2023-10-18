package me.knighthat.lib.connection.action

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.knighthat.lib.json.JsonSerializable
import java.util.*

class Action(
    val uuid: UUID,
    val type: ActionType
) : JsonSerializable {

    override fun serialize(): JsonElement {
        val json = JsonObject()

        json.addProperty("uuid", uuid.toString())
        json.addProperty("type", type.name)

        return json
    }

    enum class ActionType { PRESS }
}