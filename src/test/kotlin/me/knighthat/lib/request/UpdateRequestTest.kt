package me.knighthat.lib.request

import com.google.gson.JsonNull
import com.google.gson.JsonObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class UpdateRequestTest {

    private var content = JsonObject()

    @BeforeEach
    fun setUp() {
        content = JsonObject()
        content.addProperty("displayName", "ThisIsDisplayName")
    }

    @Test
    fun profileConstructorTest() {
        val request = UpdateRequest {
            it.addProperty("displayName", "ThisIsDisplayName")
        }

        val expected = JsonObject()
        expected.addProperty("type", Request.RequestType.UPDATE.name)
        expected.add("uuid", JsonNull.INSTANCE)
        expected.add("content", content)
        expected.addProperty("target", TargetedRequest.Target.PROFILE.name)

        Assertions.assertEquals(expected, request.serialize())
    }

    @Test
    fun buttonConstructorTest() {
        val pId = UUID.randomUUID()

        val expected = JsonObject()
        expected.addProperty("type", Request.RequestType.UPDATE.name)
        expected.addProperty("uuid", pId.toString())
        expected.add("content", content)
        expected.addProperty("target", TargetedRequest.Target.BUTTON.name)

        Assertions.assertEquals(expected, UpdateRequest(pId, content).serialize())
    }
}