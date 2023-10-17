package me.knighthat.lib.logging;

import me.knighthat.lib.util.ShortUUID;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Log {

    private static Logger logger;
    private static LogHandler handler;

    public static void setLogger( @NotNull Logger logger ) {
        destroy();
        Log.logger = logger;
        handler = new LogHandler();
        handler.start();
    }

    public static void destroy() {
        if (handler != null)
            handler.halt();
    }

    public static void log( @NotNull LogLevel level, @NotNull String s, boolean skipQueue ) {
        if (logger == null || handler == null) {
            setLogger( new DefaultLoggingImpl() );
            warn( "Logger is not initialized, use default!" );
        }

        Runnable task = logger.log( level, s, skipQueue );

        if (skipQueue || logger.sysSkipQueue())
            task.run();
        else {
            LogHandler.LogRecord record = new LogHandler.LogRecord( task );
            handler.log( record, logger.waitTime(), logger.timeUnit() );
        }
    }

    public static void deb( @NotNull String s ) {log( LogLevel.DEBUG, s, false );}

    public static void info( @NotNull String s ) {log( LogLevel.INFO, s, false );}

    public static void warn( @NotNull String s ) {log( LogLevel.WARNING, s, false );}

    public static void warn( @NotNull String s, boolean skipQueue ) {log( LogLevel.WARNING, s, skipQueue );}

    public static void err( @NotNull String s ) {log( LogLevel.ERROR, s, false );}

    public static void err( @NotNull String s, boolean skipQueue ) {log( LogLevel.ERROR, s, skipQueue );}

    /**
     * A shorthand to print error to console or file.<br>
     *
     * @param s               Custom/Additional message
     * @param t               Throwable error
     * @param printStackTrace Print stack trace
     */
    public static void exc( @NotNull String s, @NotNull Throwable t, boolean printStackTrace ) {
        if (!s.isBlank())
            err( s );

        if (t.getMessage() != null)
            err( "Reason: " + t.getMessage(), true );

        if (t.getCause() != null)
            err( "Cause: " + t.getCause().getMessage(), true );

        if (printStackTrace)
            t.printStackTrace();
    }

    /**
     * Prints out exception but at warning level
     *
     * @param s               Custom/Additional message
     * @param t               Throwable error
     * @param printStackTrace Print stack trace
     */
    public static void wexc( @NotNull String s, @NotNull Throwable t, boolean printStackTrace ) {
        if (!s.isBlank())
            warn( s );

        if (t.getMessage() != null)
            warn( "Reason: " + t.getMessage(), true );

        if (t.getCause() != null)
            warn( "Cause: " + t.getCause().getMessage(), true );

        if (printStackTrace)
            t.printStackTrace();
    }

    public static void reportBug() {
        err( "Unexpected error occurs, please report this to:", true );
        err( logger.issueWebsite() );
    }

    public static void logUpdate( @NotNull String object, @NotNull String id, @NotNull String property, @NotNull Object from, @NotNull Object to ) {
        String info = "%s %s changed %s from \"%s\" to \"%s\"";
        info = info.formatted( object, id, property, from, to );
        info( info );
    }

    public static void buttonUpdate( @NotNull UUID uuid, @NotNull String property, @NotNull Object from, @NotNull Object to ) {
        logUpdate( "Button", ShortUUID.from( uuid ), property, from, to );
    }

    public static void profileUpdate( @NotNull String displayName, @NotNull String property, @NotNull Object from, @NotNull Object to ) {
        logUpdate( "Profile", displayName, property, from, to );
    }

    public enum LogLevel {
        DEBUG, INFO, WARNING, ERROR
    }
}
