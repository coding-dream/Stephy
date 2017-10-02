package com.less.imagemanager.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Administrator on 2017/9/28.
 */

public class Zips {
    public static byte[] compress(byte[] data) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(data);
            gzipOutputStream.close();
            byteArrayOutputStream.close();
            byte [] result = byteArrayOutputStream.toByteArray();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] deCompress(byte[] compressedData) {
        try {
            int BUFFER_SIZE = 32;
            ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();

            GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(compressedData));
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer,0,bytesRead);
            }
            gzipInputStream.close();
            byteArrayOutputStream.close();

            byte [] bytes = byteArrayOutputStream.toByteArray();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
