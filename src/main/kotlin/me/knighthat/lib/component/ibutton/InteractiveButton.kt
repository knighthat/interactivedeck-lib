package me.knighthat.lib.component.ibutton

import me.knighthat.lib.component.LiveComponent
import me.knighthat.lib.component.Removable
import me.knighthat.lib.connection.request.TargetedRequest
import me.knighthat.lib.logging.Log
import java.util.*

interface InteractiveButton : LiveComponent, Removable {

    val profile: UUID

    val posX: Int

    val posY: Int

    override fun logUpdate(property: String, oldValue: Any?, newValue: Any?) = Log.buttonUpdate(uuid, property, oldValue, newValue)

    override val target: TargetedRequest.Target
        get() = TargetedRequest.Target.BUTTON
}