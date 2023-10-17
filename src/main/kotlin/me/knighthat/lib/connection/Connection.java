package me.knighthat.lib.connection;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class Connection {

    private static final @NotNull List<Consumer<Status>> EVENTS = new ArrayList<>( 5 );

    private static @NotNull Status status = Status.DISCONNECTED;

    public static boolean isConnected() {return status == Status.CONNECTED;}

    /**
     * Add a watcher to event list. Action will be executed when
     * new {@link Status} is set using {@link #setStatus(Status)}.
     *
     * @param consumer action to perform when new {@link Status} is set
     */
    public static void whenConnectionStatusChanged( @NotNull Consumer<Status> consumer ) {EVENTS.add( consumer );}

    public static @NotNull Status getStatus() {return status;}

    /**
     * Set the global connection status for the app.<br>
     * Then notifies all watchers added by {@link #whenConnectionStatusChanged(Consumer)}.
     *
     * @param status new status of app
     */
    public static void setStatus( @NotNull Status status ) {
        Connection.status = status;
        EVENTS.forEach( it -> it.accept( status ) );
    }

    public enum Status {
        DISCONNECTED( "#6c757d", "Disconnected" ),
        CONNECTED( "#70e000", "Connected" ),
        ERROR( "#ff0000", "ERROR" );

        private final @NotNull String hexColor;

        private final @NotNull String label;

        Status( @NotNull String hexColor, @NotNull String label ) {
            this.hexColor = hexColor;
            this.label = label;
        }

        public @NotNull String getHexColor() {return this.hexColor;}

        public @NotNull String getLabel() {return this.label;}
    }
}
