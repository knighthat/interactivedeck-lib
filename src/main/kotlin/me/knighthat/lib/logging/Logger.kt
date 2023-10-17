package me.knighthat.lib.logging

import java.util.concurrent.TimeUnit

interface Logger {

    fun log(level: Log.LogLevel, s: String, skipQueue: Boolean): Runnable

    fun issueWebsite(): String

    fun sysSkipQueue(): Boolean

    fun waitTime(): Long

    fun timeUnit(): TimeUnit
}