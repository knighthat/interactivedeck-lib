package me.knighthat.lib.connection.request

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.knighthat.lib.exception.RequestException
import me.knighthat.lib.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.nio.file.Paths

class RequestTest {

    companion object {

        lateinit var files: Map<String, JsonObject>

        private fun readFile(file: File): String {

            var result: ByteArray

            ByteArrayOutputStream().use { baos ->
                FileInputStream(file).use { inStream ->

                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while (inStream.read(buffer).also { bytesRead = it } != -1)
                        baos.write(
                            buffer, 0, bytesRead
                        )
                }

                result = baos.toByteArray()
            }

            return String(result)
        }

        @JvmStatic
        @BeforeAll
        fun setUp() {

            val map = HashMap<String, JsonObject>()

            val classLoader = RequestTest::class.java.classLoader
            val reqURL = classLoader.getResource("requests") ?: return
            val reqURI = Paths.get(reqURL.toURI())
            val requests = reqURI.toFile().listFiles() ?: return

            for (request in requests) {

                val content = readFile(request)
                val json = JsonParser.parseString(content)

                map[request.name] = json.asJsonObject

            }

            files = map
        }
    }

    private fun get(key: String): JsonObject? {
        val json = files[key]
        return if (json == null) {
            Assertions.fail<String>("$key returns null!")
            null
        } else
            json
    }

    @Test
    fun requestExceptions() {

        val fileNames = arrayOf("MissingContent_PairRequest", "MissingTarget_UpdateRequest", "MissingType", "MissingUUID_UpdateRequest")
        for (name in fileNames) {
            val json = get(name)
            if (json == null) {
                Assertions.fail<String> { "$name returns null!" }
                continue
            }
            Assertions.assertThrows(RequestException::class.java) { Request.fromJson(json) }
        }

    }

    @Test
    fun perfectAddRequest() {
        val json = get("Perfect_AddRequest") ?: return

        var request: Request? = null
        Assertions.assertDoesNotThrow { request = Request.fromJson(json) }
        Assertions.assertNotNull(request)
        Assertions.assertEquals(request!!.type, Request.RequestType.ADD)
        Assertions.assertInstanceOf(JsonArray::class.java, request!!.payload)
        Assertions.assertFalse(Json.isGzip(request!!.payload))
        Assertions.assertTrue(Json.isGzip(request!!.serialize()["content"]))
    }

    @Test
    fun unknownTargetAddRequest() {
        val json = get("UnknownTarget_AddRequest") ?: return
        Assertions.assertThrows(IllegalArgumentException::class.java) { Request.fromJson(json) }
    }

    @Test
    fun wrongPayloadFormatAddRequest() {
        val json = get("WrongPayloadFormat_AddRequest") ?: return
        Assertions.assertThrows(IllegalStateException::class.java) { Request.fromJson(json) }
    }
}