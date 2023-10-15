package me.knighthat.lib.json;

import com.google.gson.*;
import me.knighthat.lib.compress.GZipAlgo;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

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

        JsonArray array = new JsonArray( compressedBytes.length );
        for (byte b : compressedBytes)
            array.add( b );

        return array;
    }

    public static @NotNull JsonElement gzipDecompress( @NotNull JsonArray deflated ) throws IOException {
        if (!isGzip( deflated ))
            throw new IllegalStateException( "not gzip!" );

        byte[] deflatedBytes = JsonArrayConverter.toByteArray( deflated );
        byte[] inflatedBytes = GZipAlgo.decompress( deflatedBytes );
        String inflated = new String( inflatedBytes );

        return JsonParser.parseString( inflated );
    }

    public static @NotNull String toString( @NotNull JsonElement element ) {return GSON.toJson( element );}
}
