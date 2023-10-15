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
     * Turn every element of {@link JsonArray} into byte despite the actual element's type
     *
     * @param array array of elements to convert
     *
     * @return new byte array of given array
     */
    @Contract( pure = true )
    public static @NotNull byte[] toByteArray( @NotNull JsonArray array ) {
        byte[] bytes = new byte[array.size()];

        for (int i = 0 ; i < bytes.length ; i++)
            bytes[i] = array.get( i ).getAsByte();

        return bytes;
    }
}
