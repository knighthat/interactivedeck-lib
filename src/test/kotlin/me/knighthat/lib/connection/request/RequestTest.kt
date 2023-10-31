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

        val fileNames = arrayOf("MissingContent_PairRequest", "MissingTarget_UpdateRequest", "MissingType", "MissingUUID_RemoveRequest")
        for (name in fileNames) {
            val json = get(name)
            Assertions.assertThrows(RequestException::class.java) { Request.fromJson(json!!) }
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

    //@Test
    //fun perfectRequest() {
    //    val perfectRequest = JsonParser.parseString("{\"type\":\"PAIR\",\"content\":{\"task\":null}}")
    //
    //    var perfReq: Request? = null
    //    Assertions.assertDoesNotThrow { perfReq = Request.fromJson(perfectRequest) }
    //    Assertions.assertNotNull(perfReq)
    //    Assertions.assertEquals(perfReq!!.type, Request.RequestType.PAIR)
    //    Assertions.assertInstanceOf(JsonObject::class.java, perfReq!!.payload)
    //}
    //
    //@Test
    //fun perfectButtonRequest() {
    //    val perfectButtonRequest =
    //        JsonParser.parseString("{\"type\":\"ADD\",\"content\":[\"72884bd1-7852-43c3-b877-a3d051f1253d\",\"1dc48e36-3f5d-4141-85ae-7d4dfec878fd\"],\"target\":\"BUTTON\",\"uuid\":\"78252640-6a68-4981-84be-a66340defb76\"} ")
    //
    //    var perfBtnReq: Request? = null
    //    Assertions.assertDoesNotThrow { perfBtnReq = Request.fromJson(perfectButtonRequest) }
    //    Assertions.assertNotNull(perfBtnReq)
    //    Assertions.assertInstanceOf(AddRequest::class.java, perfBtnReq)
    //    Assertions.assertEquals((perfBtnReq!! as TargetedRequest).target, TargetedRequest.Target.BUTTON)
    //    Assertions.assertNotNull((perfBtnReq!! as TargetedRequest).uuid)
    //    Assertions.assertFalse(Json.isGzip(perfBtnReq!!.payload))
    //
    //    val payload = perfBtnReq!!.serialize().get("content")
    //    Assertions.assertTrue(Json.isGzip(payload))
    //}
    //
    //@Test
    //fun perfectProfileRequest() {
    //    val perfectProfileRequest =
    //        JsonParser.parseString("{\"type\":\"REMOVE\",\"content\":[\"72884bd1-7852-43c3-b877-a3d051f1253d\",\"1dc48e36-3f5d-4141-85ae-7d4dfec878fd\"],\"target\":\"PROFILE\",\"uuid\":null}")
    //
    //    var perfProfReq: Request? = null
    //    Assertions.assertDoesNotThrow { perfProfReq = Request.fromJson(perfectProfileRequest) }
    //    Assertions.assertNotNull(perfProfReq)
    //    Assertions.assertInstanceOf(TargetedRequest::class.java, perfProfReq)
    //    Assertions.assertEquals((perfProfReq!! as TargetedRequest).target, TargetedRequest.Target.PROFILE)
    //    Assertions.assertNull((perfProfReq!! as TargetedRequest).uuid)
    //
    //    val payload = perfProfReq!!.serialize().get("content")
    //    Assertions.assertFalse(Json.isGzip(payload))
    //}
    //
    //@Test
    //fun imperfectRequest() {
    //    val wrongType = JsonParser.parseString("[\"ADD\",null]")
    //    val missingType = JsonParser.parseString("{\"content\":{\"task\":null}}")
    //    val missingContent = JsonParser.parseString("{\"type\":\"ADD\"}")
    //    val missingTarget = JsonParser.parseString("{\"type\":\"ADD\",\"content\":{\"task\":null},\"uuid\":null}")
    //
    //    Assertions.assertThrows(RequestException::class.java) { Request.fromJson(wrongType) }
    //    Assertions.assertThrows(RequestException::class.java) { Request.fromJson(missingContent) }
    //    Assertions.assertThrows(RequestException::class.java) { Request.fromJson(missingType) }
    //    Assertions.assertThrows(RequestException::class.java) { Request.fromJson(missingTarget) }
    //}
}