package me.knighthat.lib.util;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ShortUUID {

    /**
     * Extracts the last five characters from provided {@link UUID}
     *
     * @param uuid to extract the five ending characters
     *
     * @return five ending characters of provided {@link UUID}
     */
    public static @NotNull String from( @NotNull UUID uuid ) {
        String uuidStr = uuid.toString();
        return uuidStr.substring( uuidStr.length() - 5 );
    }
}
