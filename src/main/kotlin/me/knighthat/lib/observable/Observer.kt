package me.knighthat.lib.observable

@FunctionalInterface
interface Observer<T> {

    fun update(oldValue: T?, newValue: T?)
}