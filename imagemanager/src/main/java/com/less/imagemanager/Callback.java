package com.less.imagemanager;

/**
 * Created by Administrator on 2017/9/30.
 */

public interface Callback<T> {
    T parseData(byte[] data);

    void done(T t);
}
