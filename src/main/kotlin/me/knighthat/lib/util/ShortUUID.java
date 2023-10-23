package me.knighthat.lib.util;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ShortUUID {

    public static @NotNull String from( @NotNull UUID uuid ) {
        String uuidStr = uuid.toString();
        return uuidStr.substring( uuidStr.length() - 5 );
    }
}
