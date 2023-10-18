package me.knighthat.lib.connection.action

import java.util.*

abstract class AbstractActionHandler {

    protected abstract fun handlePress(uuid: UUID)

    fun process(action: Action) {
        when (action.type) {
            Action.ActionType.PRESS -> handlePress(action.uuid)
        }
    }
}