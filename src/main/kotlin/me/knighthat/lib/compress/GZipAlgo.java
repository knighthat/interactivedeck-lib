package me.knighthat.lib.compress;

import me.knighthat.lib.logging.Log;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * <a href="https://docs.fileformat.com/compression/gz/">GZip File Format</a>
 */
public class GZipAlgo {

    /**
     * Verifies if the given byte array has valid length
     * (10 bytes of header and at least 1 byte of null footer)
     * and contains magic numbers (0x1f, 0x8b) {@link GZIPOutputStream}.
     * Additionally, file must be in compressed state (which is 0x8 - deflate).<br>
     *
     * Read more: <a href="https://docs.fileformat.com/compression/gz/">GZip header</a>
     *
     * @param bytes array of bytes to check against
     */
    public static boolean isGZip( byte @NotNull [] bytes ) {
        if (bytes.length < 11)
            return false;

        byte[] gzipHeader = new byte[]{ 31, -117, 8 };
        for (int i = 0 ; i < gzipHeader.length ; i++)
            if (bytes[i] != gzipHeader[i])
                return false;

        return true;
    }

    /**
     * Compresses the given byte array using GZip algorithm.
     * If the given byte array is already compressed with GZip,
     * then no more compression will be applied to it.
     *
     * @param raw array of uncompressed bytes to compress
     *
     * @see #isGZip(byte[])
     */
    @Contract( pure = true )
    public static byte @NotNull [] compress( byte @NotNull [] raw ) {
        byte[] deflatedBytes = raw;

        if (isGZip( raw ))
            return deflatedBytes;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream() ;
             GZIPOutputStream gzip = new GZIPOutputStream( baos )) {

            gzip.write( raw );
            // GZip needs to be closed before reading its bytes.
            // Because the content isn't written until it's closed
            gzip.close();

            deflatedBytes = baos.toByteArray();

        } catch (IOException e) {
            Log.exc( "compression failed", e, true );
            Log.reportBug();
        }

        /*
         * A newly compressed byte array has 10 bytes of
         * GZ header that needs to be removed for accuracy.
         */
        int deflNoHeader = deflatedBytes.length - 10;
        String deb = "Compressed: %s bytes to %s bytes";
        Log.deb( String.format( deb, raw.length, deflNoHeader ) );

        return deflatedBytes;
    }

    /**
     * Decompresses given bytes using GZip algorithm.
     * If the given byte array is not GZip compress,
     * then return a new byte array contains the same
     * values of the provided array.
     *
     * @param deflatedBytes array of compressed bytes to inflate.
     *
     * @see #isGZip(byte[])
     */
    @Contract( pure = true )
    public static byte @NotNull [] decompress( byte @NotNull [] deflatedBytes ) {
        byte[] inflatedBytes = new byte[0];

        if (!isGZip( deflatedBytes ))
            return inflatedBytes;

        try (ByteArrayInputStream bais = new ByteArrayInputStream( deflatedBytes ) ;
             GZIPInputStream gzip = new GZIPInputStream( bais ) ;
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while (( bytesRead = gzip.read( buffer ) ) != -1)
                baos.write( buffer, 0, bytesRead );

            inflatedBytes = baos.toByteArray();
        } catch (IOException e) {
            Log.exc( "decompression failed!", e, true );
            Log.reportBug();
        }

        /*
         * A compressed byte array has 10 bytes of GZ header
         * that needs to be removed for accuracy.
         */
        int deflNoHeader = deflatedBytes.length - 10;
        int inflLen = inflatedBytes.length;
        Log.deb( String.format( "Inflated: %s bytes to %s bytes", deflNoHeader, inflLen ) );

        return inflatedBytes;
    }
}
