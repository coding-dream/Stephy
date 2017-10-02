package com.less.imagemanager;

/**
 * Created by Administrator on 2017/9/30.
 */

public abstract class ByteArrayCallback implements Callback<byte[]>{

    public abstract void done(byte[] bytes);
}
