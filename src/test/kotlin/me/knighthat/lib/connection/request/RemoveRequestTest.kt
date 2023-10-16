package me.knighthat.lib.connection.request

import com.google.gson.JsonArray
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import me.knighthat.lib.json.JsonArrayConverter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class RemoveRequestTest {

    private val uuids = ArrayList<UUID>(20)

    @BeforeEach
    fun setUp() {
        for (i in 0 until uuids.size)
            uuids[i] = UUID.randomUUID()
    }

    @Test
    fun profileConstructorTest() {
        val request = RemoveRequest {
            for (id in uuids)
                it.add(id.toString())
        }

        val uuidArray = uuids.map(UUID::toString).toTypedArray()
        val expected = JsonObject()
        expected.addProperty("type", Request.RequestType.REMOVE.name)
        expected.add("uuid", JsonNull.INSTANCE)
        expected.add("content", JsonArrayConverter.fromStringArray(uuidArray))
        expected.addProperty("target", TargetedRequest.Target.PROFILE.name)

        Assertions.assertEquals(expected, request.serialize())
    }

    @Test
    fun buttonConstructorTest() {
        val pId = UUID.randomUUID()

        val idArray = JsonArray()
        for (id in uuids)
            idArray.add(id.toString())

        val request = RemoveRequest(pId, idArray)

        val uuidArray = uuids.map(UUID::toString).toTypedArray()
        val expected = JsonObject()
        expected.addProperty("type", Request.RequestType.REMOVE.name)
        expected.addProperty("uuid", pId.toString())
        expected.add("content", JsonArrayConverter.fromStringArray(uuidArray))
        expected.addProperty("target", TargetedRequest.Target.BUTTON.name)

        Assertions.assertEquals(expected, request.serialize())
    }
}