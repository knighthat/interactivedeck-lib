package me.knighthat.lib.component.ibutton

import me.knighthat.lib.component.LiveComponent
import me.knighthat.lib.component.Removable
import java.util.*

interface InteractiveButton : LiveComponent, Removable {

    val profile: UUID

    val posX: Int

    val posY: Int
}