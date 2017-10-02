package com.less.imagemanager.util;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2017/9/29.
 */

public class Bytes {

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE / Byte.SIZE);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE / Byte.SIZE);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    public static String printBinaryString(byte[] bytes) {
        String s = "";
        for (byte b : bytes) {
            s += "" + b + ",";// Integer.toBinaryString(b);
        }
        return s;
    }
}
