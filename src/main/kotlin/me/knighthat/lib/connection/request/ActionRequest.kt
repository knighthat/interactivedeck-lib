package me.knighthat.lib.connection.request

import me.knighthat.lib.connection.action.Action

class ActionRequest(action: Action) : Request(RequestType.ACTION, action.serialize()), RequiredConnection