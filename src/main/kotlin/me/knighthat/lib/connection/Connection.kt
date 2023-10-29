package me.knighthat.lib.connection

import me.knighthat.lib.observable.Observable
import me.knighthat.lib.observable.Observer

class Connection private constructor() {

    companion object {
        private val _internal = Connection()

        /**
         * This value represents the current status of connection.
         *
         * This value is not supposed to be **_null_**, a [NullPointerException]
         * will be thrown as the result of null status.
         *
         * When new value is set, all observers added by [whenConnectionStatusChanged] will be notified.
         *
         * @throws NullPointerException if value is null
         */
        @JvmStatic
        var status: Status
            get() = _internal.status.value.orElseThrow { NullPointerException("status is \"null\"") }
            set(value) = _internal.status.setValue(value)

        /**
         * Add a watcher to the watcher list. Action will be executed when
         * new [Status] is set using [status]
         *
         * @param observer action to perform when new [Status] is set
         */
        @JvmStatic
        fun whenConnectionStatusChanged(observer: Observer<Status>) = _internal.status.observe(observer)

        @JvmStatic
        fun isConnected(): Boolean = status == Status.CONNECTED

        @JvmStatic
        fun isDisconnected(): Boolean = status == Status.DISCONNECTED

        @JvmStatic
        fun isError(): Boolean = Status.ERROR == _internal.status.value.orElse(Status.ERROR)
    }

    private var status: Observable<Status> = Observable.of(Status.DISCONNECTED)

    enum class Status(hexColor: String, label: String) {

        DISCONNECTED("#6c757d", "Disconnected"),
        CONNECTED("#70e000", "Connected"),
        ERROR("#ff0000", "ERROR");
    }
}