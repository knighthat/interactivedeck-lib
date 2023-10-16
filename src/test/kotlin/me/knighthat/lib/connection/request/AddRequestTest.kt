package me.knighthat.lib.connection.request

import com.google.gson.JsonArray
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import me.knighthat.lib.json.Json
import me.knighthat.lib.json.JsonArrayConverter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.util.*

class AddRequestTest {

    companion object {

        private var uuids = ArrayList<UUID>(20)

        @JvmStatic
        @BeforeAll
        fun setUp() {
            for (i in 0 ..< 20)
                uuids.add(UUID.randomUUID())
        }
    }

    @Test
    fun profileConstructorTest() {
        val request = AddRequest {
            for (id in uuids)
                it.add(id.toString())
        }

        val uuidStrArray = uuids.map(UUID::toString).toTypedArray()
        val uuidArray = JsonArrayConverter.fromStringArray(uuidStrArray)
        val expected = JsonObject()
        expected.addProperty("type", Request.RequestType.ADD.name)
        expected.add("uuid", JsonNull.INSTANCE)
        expected.add("content", Json.gzipCompress(uuidArray))
        expected.addProperty("target", TargetedRequest.Target.PROFILE.name)

        Assertions.assertEquals(expected, request.serialize())
    }

    @Test
    fun buttonConstructorTest() {
        val pId = UUID.randomUUID()

        val idArray = JsonArray()
        for (id in uuids)
            idArray.add(id.toString())

        val request = AddRequest(pId, idArray)

        val uuidStrArray = uuids.map(UUID::toString).toTypedArray()
        val uuidArray = JsonArrayConverter.fromStringArray(uuidStrArray)
        val expected = JsonObject()
        expected.addProperty("type", Request.RequestType.ADD.name)
        expected.addProperty("uuid", pId.toString())
        expected.add("content", Json.gzipCompress(uuidArray))
        expected.addProperty("target", TargetedRequest.Target.BUTTON.name)

        Assertions.assertEquals(expected, request.serialize())
    }
}