package me.knighthat.lib.request

import com.google.gson.JsonElement

class PairRequest(payload: JsonElement) : Request(RequestType.PAIR, payload)
