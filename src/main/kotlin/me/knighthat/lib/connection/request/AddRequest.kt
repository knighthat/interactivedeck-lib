package me.knighthat.lib.connection.request

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.knighthat.lib.component.ibutton.InteractiveButton
import me.knighthat.lib.json.Json
import me.knighthat.lib.profile.AbstractProfile
import java.util.function.Consumer

class AddRequest private constructor(target: Target) : TargetedRequest(RequestType.ADD, target, JsonArray()) {

    override val payload: JsonArray
        get() {
            val array = super.payload.asJsonArray
            return if (Json.isGzip(array))
                Json.gzipDecompress(array).asJsonArray
            else
                array
        }

    constructor(target: Target, payload: JsonArray) : this(target) {
        this.payload.addAll(payload)
    }

    constructor(target: Target, payload: Consumer<JsonArray>) : this(target) {
        payload.accept(this.payload)
    }

    private constructor(target: Target, payload: Array<out RequestJson>) : this(target) {
        payload.map(RequestJson::toRequest).forEach(this.payload::add)
    }

    constructor(payload: Array<out InteractiveButton>)
            : this(Target.BUTTON, payload.filter { it is RequestJson }.map { it as RequestJson }.toTypedArray())

    constructor(payload: Array<out AbstractProfile<out InteractiveButton>>)
            : this(Target.PROFILE, payload.filter { it is RequestJson }.map { it as RequestJson }.toTypedArray())


    override fun serialize(): JsonObject {
        val json = super.serialize()
        json.add("content", Json.gzipCompress(this.payload))

        return json
    }
}

