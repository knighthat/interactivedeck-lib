package me.knighthat.lib.memory;

import me.knighthat.lib.component.ibutton.InteractiveButton;
import me.knighthat.lib.logging.Log;
import me.knighthat.lib.observable.Observable;
import me.knighthat.lib.observable.Observer;
import me.knighthat.lib.profile.AbstractProfile;
import me.knighthat.lib.util.ShortUUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

@SuppressWarnings( "unused" )
public class Persistent {

    private static final @NotNull Persistent INTERNAL = new Persistent();

    private static AbstractProfile<?> DEFAULT;

    /**
     * @return an unmodifiable {@link Set<AbstractProfile>} contains all available profiles.
     */
    public static @NotNull @Unmodifiable Set<AbstractProfile<?>> getProfiles() {return Collections.unmodifiableSet( INTERNAL.profiles );}

    public static AbstractProfile<?> @NotNull [] profileArray() {return INTERNAL.profiles.toArray( new AbstractProfile[0] );}

    /**
     * Gets the first {@link AbstractProfile} that has its {@link UUID} matches the given one.<br>
     * If {@link UUID} is not found in the list, a {@link Optional} of null is returned.
     *
     * @param uuid to find {@link AbstractProfile}
     */
    public static @NotNull Optional<AbstractProfile<?>> findProfile( @NotNull UUID uuid ) {
        AbstractProfile<?> profile = null;
        for (AbstractProfile<?> p : INTERNAL.profiles)
            if (p.getUuid().equals( uuid )) {
                profile = p;
                break;
            }
        return Optional.ofNullable( profile );
    }

    public static void add( @NotNull AbstractProfile<?> profile ) {
        INTERNAL.profiles.add( profile );
        INTERNAL.buttons.addAll( profile.getButtons() );

        if (profile.isDefault())
            DEFAULT = profile;
    }

    public static void remove( @NotNull AbstractProfile<?> profile ) {
        profile.getButtons().forEach( INTERNAL.buttons::remove );
        INTERNAL.profiles.remove( profile );
    }

    /**
     * @return an unmodifiable {@link Set<InteractiveButton>} contains all available profiles.
     */
    public static @NotNull @Unmodifiable Set<InteractiveButton> getButtons() {return Collections.unmodifiableSet( INTERNAL.buttons );}

    /**
     * Gets the first {@link AbstractProfile} that has its {@link UUID} matches the given one.<br>
     * If {@link UUID} is not found in the list, a {@link Optional} of null is returned.
     *
     * @param uuid to find {@link AbstractProfile}
     */
    public static @NotNull Optional<InteractiveButton> findButton( @NotNull UUID uuid ) {
        InteractiveButton result = null;
        for (InteractiveButton btn : INTERNAL.buttons)
            if (btn.getUuid().equals( uuid )) {
                result = btn;
                break;
            }
        return Optional.ofNullable( result );
    }

    public static void add( @NotNull InteractiveButton button ) {INTERNAL.buttons.add( button );}

    public static void remove( @NotNull InteractiveButton button ) {INTERNAL.buttons.remove( button );}

    /**
     * @return currently showing {@link AbstractProfile}, null {@link Optional} if none is set.
     */
    public static @NotNull Optional<AbstractProfile<?>> getActive() {return INTERNAL.active.getValue();}

    /**
     * Sets active {@link AbstractProfile} then notifies all observers about the change.<br>
     *
     * @param profile new active {@link AbstractProfile}
     */
    public static void setActive( @NotNull AbstractProfile<?> profile ) {
        Log.info(
                String.format(
                        "Now showing %s (%s) with %s button(s)",
                        profile.getDisplayName(),
                        ShortUUID.from( profile.getUuid() ),
                        profile.getButtons().size()
                )
        );

        INTERNAL.active.setValue( profile );
    }

    /**
     * Add a "watcher" to active {@link AbstractProfile}. When new active profile is set,<br>
     * this function gets called to action.
     *
     * @param observer "watcher" to active {@link AbstractProfile}
     */
    public static void observeActive( @NotNull Observer<AbstractProfile<?>> observer ) {INTERNAL.active.observe( observer );}

    public static AbstractProfile<?> defaultProfile() {return DEFAULT;}

    final @NotNull Set<AbstractProfile<?>> profiles;
    final @NotNull Set<InteractiveButton> buttons;
    final @NotNull Observable<AbstractProfile<?>> active;

    private Persistent() {
        profiles = new HashSet<>();
        active = Observable.of( null );
        buttons = new HashSet<>();
    }
}
