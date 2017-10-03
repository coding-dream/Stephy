package com.less.imagemanager;

/**
 * Created by Administrator on 2017/9/30.
 */

public abstract class StringCallback implements Callback<String>{

    @Override
    public String parseData(byte[] data) {
        return new String(data);
    }

    public abstract void done(String s);

}
