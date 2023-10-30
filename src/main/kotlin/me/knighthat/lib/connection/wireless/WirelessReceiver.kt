package me.knighthat.lib.connection.wireless

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import me.knighthat.lib.connection.request.AbstractRequestHandler
import me.knighthat.lib.connection.request.Request
import me.knighthat.lib.logging.Log
import java.io.InputStream

class WirelessReceiver(
    private val inStream: InputStream,
    private val buffer: ByteArray,
    private val handler: AbstractRequestHandler
) : Runnable {


    private fun process(payload: String) {
        Log.deb("Processing: $payload")

        try {

            val json = JsonParser.parseString(payload)

            if (json !is JsonObject)
                Log.err("request must be ${JsonObject::class.qualifiedName} not ${json::class.qualifiedName}")
            else
                handler.process(Request.fromJson(json))

        } catch (e: JsonParseException) {
            Log.exc("json parse failed!", e, false)
        }
    }

    override fun run() {
        var bytesRead: Int
        var finalStr = ""

        while (inStream.read(buffer).also { bytesRead = it } != -1) {
            val decoded = String(buffer, 0, bytesRead)
            Log.deb("Received: $decoded")

            val sliced = decoded.split('\u0000')

            finalStr += sliced[0]
            if (sliced.size > 1)
                process(finalStr)

            for (index in 1 ..< sliced.size) {

                if (sliced.size - 1 == index) {
                    finalStr = sliced[index]
                    break
                }

                process(sliced[index])
            }
        }
    }
}