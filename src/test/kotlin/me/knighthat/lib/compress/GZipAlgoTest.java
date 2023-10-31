package me.knighthat.lib.compress;

import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class GZipAlgoTest {

    private static final Map<File, File> subjects = new HashMap<>( 3 );

    @BeforeAll
    static void setUp() {
        URL resDirURL = GZipAlgoTest.class.getClassLoader().getResource( "profiles" );
        if (resDirURL == null)
            Assertions.fail( "Cannot find resources folder!" );

        try {

            Path resPath = Paths.get( resDirURL.toURI() );
            File[] files = resPath.toFile().listFiles();
            if (files == null) {
                Log.warn( "No test profile found!" );
                return;
            }

            for (File profile : files) {
                String name = profile.getName();

                if (!name.endsWith( ".profile" ))
                    continue;
                Log.info( "Found profile " + profile.getName() );

                File GZipProfile = new File( profile.getParent(), name + ".gz" );
                if (GZipProfile.exists())
                    subjects.put( profile, GZipProfile );
            }

        } catch (URISyntaxException e) {
            Assertions.fail( "Failed to convert URL to URI!" );
        }
    }


    @Test
    void isGZip() {
        subjects.values().forEach( file -> {
            Log.info( "Testing " + file.getName() );

            byte[] array = fileToByteArray( file );

            Assertions.assertNotEquals( 0, array.length );
            Assertions.assertTrue( GZipAlgo.isGZip( array ) );

        } );
    }

    @Test
    void compressAndDecompress() {
        for (Map.Entry<File, File> entry : subjects.entrySet()) {

            byte[] uncompressed = fileToByteArray( entry.getKey() );
            Log.info( Arrays.toString( GZipAlgo.compress( uncompressed ) ) );
            Assertions.assertFalse( GZipAlgo.isGZip( uncompressed ) );

            byte[] compressed = fileToByteArray( entry.getValue() );
            Log.info( Arrays.toString( compressed ) );
            Assertions.assertTrue( GZipAlgo.isGZip( compressed ) );

            String uncompressedStr = new String( uncompressed );
            String decompressedStr = new String( GZipAlgo.decompress( compressed ) );

            Assertions.assertEquals( uncompressedStr, decompressedStr );

        }
    }

    private byte[] fileToByteArray( @NotNull File file ) {
        byte[] result = new byte[0];

        try (FileInputStream inStream = new FileInputStream( file ) ;
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while (( bytesRead = inStream.read( buffer ) ) != -1)
                baos.write( buffer, 0, bytesRead );

            result = baos.toByteArray();

        } catch (IOException e) {
            Log.err( "Failed to convert " + file.getName() + " to byte array!" );
        }

        return result;
    }
}