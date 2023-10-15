package me.knighthat.lib.compress;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipAlgo {

    public static boolean isGZip( @NotNull byte[] bytes ) {
        if (bytes.length < 10)
            return false;

        byte[] gzipHeader = new byte[]{ 31, -117, 8, 0, 0, 0, 0, 0, 0, -1 };
        for (int i = 0 ; i < gzipHeader.length ; i++)
            if (bytes[i] != gzipHeader[i])
                return false;

        return true;
    }

    public static @NotNull byte[] compress( @NotNull byte[] raw ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        GZIPOutputStream gzip = new GZIPOutputStream( baos );
        gzip.write( raw );
        // GZip needs to be closed before reading its bytes.
        // Because the content isn't written until it's closed
        gzip.close();

        byte[] compressBytes = baos.toByteArray();
        baos.close();

        return compressBytes;
    }

    public static byte[] decompress( byte[] deflatedBytes ) throws IOException {
        if (!isGZip( deflatedBytes ))
            throw new IllegalStateException( "not gzip!" );

        ByteArrayInputStream bais = new ByteArrayInputStream( deflatedBytes );
        GZIPInputStream gzip = new GZIPInputStream( bais );

        return gzip.readAllBytes();
    }
}
