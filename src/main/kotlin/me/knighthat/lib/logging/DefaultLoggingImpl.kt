package me.knighthat.lib.logging

import java.util.concurrent.TimeUnit

class DefaultLoggingImpl : Logger {

    override fun log(level: Log.LogLevel, s: String, skipQueue: Boolean): Runnable {
        return Runnable {
            val format = "[${level.name}] $s"
            if (level == Log.LogLevel.ERROR)
                println(format)
            else
                println(format)
        }
    }

    override fun issueWebsite(): String = ""

    override fun sysSkipQueue(): Boolean = true

    override fun waitTime(): Long = 0

    override fun timeUnit(): TimeUnit = TimeUnit.SECONDS
}