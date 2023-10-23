package me.knighthat.lib.profile

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.knighthat.lib.component.LiveComponent
import me.knighthat.lib.component.Removable
import me.knighthat.lib.component.ibutton.InteractiveButton
import me.knighthat.lib.connection.request.TargetedRequest
import me.knighthat.lib.logging.Log

abstract class AbstractProfile<T : InteractiveButton>(
    val isDefault: Boolean = false,
    open val buttons: MutableList<T>,
    open var displayName: String,
    open var columns: Int,
    open var rows: Int,
    open var gap: Int
) : LiveComponent, Removable {

    protected abstract fun updateButtons(buttonJson: JsonElement)

    override fun update(json: JsonObject) {
        if (json.has("displayName"))
            displayName = json["displayName"].asString

        if (json.has("rows"))
            rows = json["rows"].asInt

        if (json.has("columns"))
            columns = json["columns"].asInt

        if (json.has("gap"))
            gap = json["gap"].asInt

        if (json.has("buttons"))
            updateButtons(json["buttons"])
    }

    override fun logUpdate(property: String, oldValue: Any?, newValue: Any?) = Log.profileUpdate(displayName, property, oldValue, newValue)

    override val target = TargetedRequest.Target.PROFILE
}