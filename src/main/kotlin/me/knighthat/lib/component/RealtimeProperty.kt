package me.knighthat.lib.component

import com.google.gson.JsonObject

@FunctionalInterface
interface RealtimeProperty {

    fun update(json: JsonObject)
}