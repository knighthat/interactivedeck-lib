package me.knighthat.lib.connection.wireless

import me.knighthat.lib.connection.Connection
import me.knighthat.lib.connection.request.Request
import me.knighthat.lib.logging.Log
import java.io.IOException
import java.io.OutputStream
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class WirelessSender(private val outStream: OutputStream) : Thread() {

    companion object {

        private val QUEUE = LinkedBlockingQueue<Request>(20)

        fun send(request: Request) {
            try {
                if (Connection.isConnected())
                    QUEUE.offer(request, 5000L, TimeUnit.MILLISECONDS)
                else {
                    Log.warn("Failed to send request. Connection is not established!")
                    Log.reportBug()
                    Log.deb("Request: $request")
                }
            } catch (e: InterruptedException) {
                Log.exc("Thread was interrupted while send request is pending!", e, true)
            }
        }
    }

    init {
        name = "NET/O"
        QUEUE.clear()
    }

    override fun run() {
        while (!interrupted()) {
            try {
                val serialized = QUEUE.take().toString()
                Log.deb("Sending: $serialized")

                outStream.write(appendNull(serialized))
                outStream.flush()
            } catch (e: InterruptedException) {

                //TODO Needs proper error handling
                if (Connection.isDisconnected())
                    return

                Log.exc("Thread interrupted!", e, true)
                break
            } catch (e: IOException) {
                //TODO Needs proper error handling
                Log.exc("Error occurs while sending out request", e, true)
                break
            }
        }
    }

    private fun appendNull(input: String): ByteArray = input.plus("\u0000").toByteArray()
}