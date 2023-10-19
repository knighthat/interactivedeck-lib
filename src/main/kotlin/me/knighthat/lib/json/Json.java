package me.knighthat.lib.json;

import com.google.gson.*;
import me.knighthat.lib.compress.GZipAlgo;
import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class Json {

    private static final @NotNull Gson GSON =
            new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    public static boolean isGzip( @NotNull JsonElement json ) {
        try {
            JsonArray array = json.getAsJsonArray();
            byte[] bytes = JsonArrayConverter.toByteArray( array );

            return GZipAlgo.isGZip( bytes );
        } catch (Exception ignored) {
            return false;
        }
    }

    public static @NotNull JsonArray gzipCompress( @NotNull JsonElement json ) throws IOException {
        String jsonString = GSON.toJson( json );
        byte[] compressedBytes = GZipAlgo.compress( jsonString.getBytes() );

        return JsonArrayConverter.fromByteArray( compressedBytes );
    }

    public static @NotNull JsonElement gzipDecompress( @NotNull JsonArray deflated ) throws IOException {
        if (!isGzip( deflated ))
            throw new IllegalStateException( "not gzip!" );

        byte[] deflatedBytes = JsonArrayConverter.toByteArray( deflated );
        byte[] inflatedBytes = GZipAlgo.decompress( deflatedBytes );

        String inflated = new String( inflatedBytes );
        Log.deb( "Inflated string: " + inflated );

        return JsonParser.parseString( inflated );
    }

    /**
     * Save {@link SaveAsJson} to physical file.
     *
     * @param instance instance that can be saved
     * @param saveTo   where to store this file
     *
     * @throws IOException when directory is not found,
     *                     saveTo is not a directory,
     *                     saveTo is inaccessible,
     *                     new file exists,
     *                     cannot create new file.
     */
    public static void dump( @NotNull SaveAsJson instance, @NotNull String saveTo ) throws IOException {
        File directory = new File( saveTo );
        if (!directory.exists())
            throw new FileNotFoundException( "directory " + saveTo + " does not exist!" );
        if (!directory.isDirectory())
            throw new IllegalStateException( saveTo + " is not a directory!" );
        if (!directory.canWrite())
            throw new AccessDeniedException( saveTo + " is not writeable" );

        File saveAs = new File( directory, instance.getFullName() );
        if (!saveAs.exists() && !saveAs.createNewFile())
            throw new FileNotFoundException( "failed to create " + saveAs.getAbsolutePath() );

        FileWriter writer = new FileWriter( saveAs );
        GSON.toJson( instance.serialize(), writer );
        writer.close();

        Log.info( "Saved " + instance.getDisplayName() + " under file " + instance.getFullName() );
    }
}
