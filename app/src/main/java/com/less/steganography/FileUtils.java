package com.less.steganography;

import android.content.res.Resources;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2017/9/30.
 */

public class FileUtils {

    public static String readRawString(Resources resources,int rawId){
        try {
            InputStream inputStream = resources.openRawResource(rawId);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            StringBuffer buffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + "\r\n");
            }
            bufferedReader.close();
            inputStream.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] readRawData(Resources resources,int rawId){
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            InputStream inputStream = resources.openRawResource(rawId);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[1024];
            int hasRead = -1;
            while ( (hasRead = bufferedInputStream.read(buffer))!= -1) {
                byteArrayOutputStream.write(buffer, 0, hasRead);
            }
            bufferedInputStream.close();
            inputStream.close();
            byteArrayOutputStream.close();
            byte[] data = byteArrayOutputStream.toByteArray();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeData(byte[] data,String destPath){
        try {
            File file = new File(destPath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
