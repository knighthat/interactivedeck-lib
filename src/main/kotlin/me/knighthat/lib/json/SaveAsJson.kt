package me.knighthat.lib.json

interface SaveAsJson : JsonSerializable {

    val displayName: String

    val fileName: String

    val fileExtension: String

    fun getFullName() = "$fileName.$fileExtension"
}