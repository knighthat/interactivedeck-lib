package me.knighthat.lib.json;

import com.google.gson.*;
import me.knighthat.lib.compress.GZipAlgo;
import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class Json {

    private static final @NotNull Gson GSON =
            new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    /**
     * Verifies if given {@link JsonElement} is {@link JsonArray}.<br>
     * Then verifies magic numbers by invoking the same function from {@link GZipAlgo}.
     *
     * @param json json array to verify
     */
    public static boolean isGzip( @NotNull JsonElement json ) {
        try {
            JsonArray array = json.getAsJsonArray();
            byte[] bytes = JsonArrayConverter.toByteArray( array );

            return GZipAlgo.isGZip( bytes );
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * Compresses given {@link JsonArray} using GZip algorithm.
     *
     * @param json {@linkplain JsonArray} to compress
     *
     * @return newly compressed data wrapped in {@link JsonArray} instance. If you prefer a byte array, check out {@link GZipAlgo}
     *
     * @throws IOException when given byte array is inaccessible or contains illegal character
     */
    @Contract( pure = true )
    public static @NotNull JsonArray gzipCompress( @NotNull JsonArray json ) throws IOException {
        String jsonString = GSON.toJson( json );
        byte[] compressedBytes = GZipAlgo.compress( jsonString.getBytes() );

        return JsonArrayConverter.fromByteArray( compressedBytes );
    }

    /**
     * Decompresses given {@link JsonArray} and converts inflated bytes to a subclass of {@link JsonElement}.
     *
     * @param deflated compressed {@link JsonArray} to inflate
     *
     * @throws IOException           if **deflated** bytes contains illegal character or inaccessible
     * @throws IllegalStateException if given {@link JsonArray} is not **GZip** byte array
     * @see #isGzip(JsonElement)
     */
    @Contract( pure = true )
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
     * @throws FileNotFoundException destination folder does not exist
     * @throws IllegalStateException destination is not a directory
     * @throws AccessDeniedException destination folder is not writeable
     * @throws IOException           cannot create new file
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
            throw new IOException( "failed to create " + saveAs.getAbsolutePath() );

        FileWriter writer = new FileWriter( saveAs );
        GSON.toJson( instance.serialize(), writer );
        writer.close();

        Log.info( "Saved " + instance.getDisplayName() + " under file " + instance.getFullName() );
    }
}
