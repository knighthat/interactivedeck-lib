package me.knighthat.lib.json

import com.google.gson.JsonElement


@FunctionalInterface
interface JsonSerializable {
    fun serialize(): JsonElement
}