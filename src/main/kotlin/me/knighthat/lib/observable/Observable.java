package me.knighthat.lib.observable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Observable<T> {

    public static <T> @NotNull Observable<T> of( @Nullable T value ) {return new Observable<>( value );}

    private final @NotNull Set<Observer<T>> observers;
    private volatile @Nullable T value;

    private Observable( @Nullable T value ) {
        this.value = value;
        this.observers = new HashSet<>();
    }

    private void notifyObservers( @Nullable T oldValue, @Nullable T newValue ) {observers.forEach( obs -> obs.update( oldValue, newValue ) );}

    /**
     * @return {@link Optional} that may contain null value
     */
    public synchronized @NotNull Optional<T> getValue() {return Optional.ofNullable( value );}

    /**
     * Calls all observers to update to this new value.
     *
     * @param value new value
     */
    public synchronized void setValue( @Nullable T value ) {
        if (Objects.equals( this.value, value ))
            return;

        notifyObservers( this.value, value );
        this.value = value;
    }

    /**
     * Add call to observers, when new value is set; all functions get called.
     *
     * @param observer what happen when new value is updated.
     */
    public void observe( @NotNull Observer<T> observer ) {observers.add( observer );}
}
