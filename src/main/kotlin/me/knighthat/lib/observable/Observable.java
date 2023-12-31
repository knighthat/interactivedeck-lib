package me.knighthat.lib.observable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Observable<T> {

    /**
     * Instantiate an observable object that stores a nullable value.
     *
     * @param value initial value (nullable)
     *
     * @return new {@link Observable} object contains input value
     */
    @Contract( pure = true )
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
     * If stored value and input value is unchanged,
     * then no observers will be called into action.
     *
     * @param value new value
     */
    public synchronized void setValue( @Nullable T value ) {
        if (Objects.equals( this.value, value ))
            return;

        T oldValue = this.value;
        this.value = value;
        notifyObservers( oldValue, value );
    }

    /**
     * Add call to observers, when new value is set; all functions get called.
     *
     * @param observer what happen when new value is updated.
     */
    public void observe( @NotNull Observer<T> observer ) {observers.add( observer );}
}
