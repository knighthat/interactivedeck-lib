package me.knighthat.lib.component

import java.util.*

@FunctionalInterface
interface Identifiable {

    val uuid: UUID
}