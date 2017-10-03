package com.less.imagemanager;

/**
 * Created by Administrator on 2017/9/30.
 */

public abstract class ByteArrayCallback implements Callback<byte[]>{

    @Override
    public byte[] parseData(byte[] data) {
        return data;
    }

    public abstract void done(byte[] bytes);
}
