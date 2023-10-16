package me.knighthat.lib.connection.request

import com.google.gson.JsonElement

class PairRequest(payload: JsonElement) : Request(RequestType.PAIR, payload)
