package me.knighthat.lib.json

import com.google.gson.JsonElement


@FunctionalInterface
interface JsonSerializable {

    /**
     * Converts this class into [JsonElement]
     */
    fun serialize(): JsonElement
}