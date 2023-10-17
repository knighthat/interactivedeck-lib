package me.knighthat.lib.util;

import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ShortUUID {

    public static @NotNull String from( @NotNull UUID uuid ) {
        String uuidStr = uuid.toString();
        String shortUuid = uuidStr.substring( uuidStr.length() - 5 );

        Log.deb( "Converted " + uuid + " to " + shortUuid );

        return shortUuid;
    }
}
