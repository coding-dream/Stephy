package com.less.steganography;

import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2017/9/30.
 */

public class FileUtils {

    public static String readRaw(Resources resources,int rawId){
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
}
