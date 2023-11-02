package me.knighthat.lib.json;

import com.google.gson.JsonArray;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class JsonArrayConverter {

    /**
     * Turns array of {@link String} to {@link  JsonArray} of String
     *
     * @param strings array to convert
     *
     * @return new {@link JsonArray} contains given strings
     */
    @Contract( pure = true )
    public static @NotNull JsonArray fromStringArray( String[] strings ) {
        if (strings == null || strings.length == 0)
            return new JsonArray( 0 );

        JsonArray jsonArray = new JsonArray( strings.length );
        for (String str : strings)
            jsonArray.add( str );

        return jsonArray;
    }

    /**
     * Converts {@link JsonArray} into array of {@link String}.
     *
     * @param array {@link JsonArray} that store strings
     *
     * @return new array of {@link String}.
     */
    @Contract( pure = true )
    public static String @NotNull [] toStringArray( @NotNull JsonArray array ) {
        String[] result = new String[array.size()];

        if (!array.isEmpty())
            for (int i = 0 ; i < array.size() ; i++)
                result[i] = array.get( i ).getAsString();

        return result;
    }

    public static @NotNull JsonArray fromByteArray( byte[] bytes ) {
        if (bytes == null || bytes.length == 0)
            return new JsonArray( 0 );

        JsonArray jsonArray = new JsonArray( bytes.length );
        for (byte b : bytes)
            jsonArray.add( b );

        return jsonArray;
    }

    /**
     * Turn every element of {@link JsonArray} into byte despite the actual element's type
     *
     * @param array array of elements to convert
     *
     * @return new byte array of given array
     */
    @Contract( pure = true )
    public static byte @NotNull [] toByteArray( @NotNull JsonArray array ) {
        byte[] bytes = new byte[array.size()];

        for (int i = 0 ; i < bytes.length ; i++)
            bytes[i] = array.get( i ).getAsByte();

        return bytes;
    }
}
