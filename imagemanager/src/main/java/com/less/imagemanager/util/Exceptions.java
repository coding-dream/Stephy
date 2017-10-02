package com.less.imagemanager.util;

/**
 * Created by Administrator on 2017/9/28.
 */

public class Exceptions {
    public static void unSupportOperation(String msg, Object... params) {
        throw new UnsupportedOperationException(String.format(msg, params));
    }

    public static void runtime(String msg, Object... params) {
        throw new RuntimeException(String.format(msg, params));
    }

}
