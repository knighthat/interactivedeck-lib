package me.knighthat.lib.logging;

import me.knighthat.lib.connection.Connection;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static me.knighthat.lib.logging.Log.exc;

class LogHandler extends Thread {

    private final @NotNull LinkedBlockingDeque<LogRecord> LOGS;
    private final @NotNull LogRecord TERMINATOR;

    LogHandler() {
        LOGS = new LinkedBlockingDeque<>( 5 );
        TERMINATOR = new LogRecord( () -> {} );
    }

    void halt() {LOGS.addFirst( TERMINATOR );}

    public void log( @NotNull LogRecord record, long waitTime, @NotNull TimeUnit timeUnit ) {
        try {
            LOGS.offer( record, waitTime, timeUnit );
        } catch (InterruptedException e) {
            if (Connection.isDisconnected())
                return;

            Log.exc( "Timed out while wait to log", e, true );
            Log.reportBug();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                LogRecord log = LOGS.take();
                if (log == TERMINATOR)
                    break;
                Thread.currentThread().setName( log.threadName );
                log.task.run();
            } catch (InterruptedException e) {
                exc( "Logs queue crashed!", e, true );
                break;
            }
        }
    }

    static class LogRecord {
        private final @NotNull String threadName;
        private final @NotNull Runnable task;

        LogRecord( @NotNull Runnable task ) {
            this.threadName = Thread.currentThread().getName();
            this.task = task;
        }
    }
}
