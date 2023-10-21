package me.knighthat.lib.connection.request

import com.google.gson.JsonElement

@FunctionalInterface
interface RequestJson {

    fun toRequest(): JsonElement
}