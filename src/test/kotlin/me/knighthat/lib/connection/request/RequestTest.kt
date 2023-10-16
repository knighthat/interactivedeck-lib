package me.knighthat.lib.connection.request

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.knighthat.lib.exception.RequestException
import me.knighthat.lib.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RequestTest {

    @Test
    fun perfectRequest() {
        val perfectRequest = JsonParser.parseString("{\"type\":\"PAIR\",\"content\":{\"task\":null}}")

        var perfReq: Request? = null
        Assertions.assertDoesNotThrow { perfReq = Request.fromJson(perfectRequest) }
        Assertions.assertNotNull(perfReq)
        Assertions.assertEquals(perfReq!!.type, Request.RequestType.PAIR)
        Assertions.assertInstanceOf(JsonObject::class.java, perfReq!!.payload)
    }

    @Test
    fun perfectButtonRequest() {
        val perfectButtonRequest =
            JsonParser.parseString("{\"type\":\"ADD\",\"content\":[\"72884bd1-7852-43c3-b877-a3d051f1253d\",\"1dc48e36-3f5d-4141-85ae-7d4dfec878fd\"],\"target\":\"BUTTON\",\"uuid\":\"78252640-6a68-4981-84be-a66340defb76\"} ")

        var perfBtnReq: Request? = null
        Assertions.assertDoesNotThrow { perfBtnReq = Request.fromJson(perfectButtonRequest) }
        Assertions.assertNotNull(perfBtnReq)
        Assertions.assertInstanceOf(AddRequest::class.java, perfBtnReq)
        Assertions.assertEquals((perfBtnReq!! as TargetedRequest).target, TargetedRequest.Target.BUTTON)
        Assertions.assertNotNull((perfBtnReq!! as TargetedRequest).uuid)

        val payload = perfBtnReq!!.serialize().get("content")
        Assertions.assertTrue(Json.isGzip(payload))
    }

    @Test
    fun perfectProfileRequest() {
        val perfectProfileRequest =
            JsonParser.parseString("{\"type\":\"REMOVE\",\"content\":[\"72884bd1-7852-43c3-b877-a3d051f1253d\",\"1dc48e36-3f5d-4141-85ae-7d4dfec878fd\"],\"target\":\"PROFILE\",\"uuid\":null}")

        var perfProfReq: Request? = null
        Assertions.assertDoesNotThrow { perfProfReq = Request.fromJson(perfectProfileRequest) }
        Assertions.assertNotNull(perfProfReq)
        Assertions.assertInstanceOf(TargetedRequest::class.java, perfProfReq)
        Assertions.assertEquals((perfProfReq!! as TargetedRequest).target, TargetedRequest.Target.PROFILE)
        Assertions.assertNull((perfProfReq!! as TargetedRequest).uuid)

        val payload = perfProfReq!!.serialize().get("content")
        Assertions.assertFalse(Json.isGzip(payload))
    }

    @Test
    fun imperfectRequest() {
        val wrongType = JsonParser.parseString("[\"ADD\",null]")
        val missingType = JsonParser.parseString("{\"content\":{\"task\":null}}")
        val missingContent = JsonParser.parseString("{\"type\":\"ADD\"}")
        val missingTarget = JsonParser.parseString("{\"type\":\"ADD\",\"content\":{\"task\":null},\"uuid\":null}")

        Assertions.assertThrows(RequestException::class.java) { Request.fromJson(wrongType) }
        Assertions.assertThrows(RequestException::class.java) { Request.fromJson(missingContent) }
        Assertions.assertThrows(RequestException::class.java) { Request.fromJson(missingType) }
        Assertions.assertThrows(RequestException::class.java) { Request.fromJson(missingTarget) }
    }
}