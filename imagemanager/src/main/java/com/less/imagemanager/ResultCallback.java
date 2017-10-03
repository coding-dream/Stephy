package com.less.imagemanager;

import java.io.File;

/**
 * Created by Administrator on 2017/9/30.
 */

public abstract class ResultCallback implements Callback<File>{

    @Override
    public File parseData(byte[] data) {
        return null;
    }

    @Override
    public abstract void done(File file);
}
