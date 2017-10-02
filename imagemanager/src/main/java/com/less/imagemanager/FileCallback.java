package com.less.imagemanager;

import java.io.File;

/**
 * Created by Administrator on 2017/9/30.
 */

public abstract class FileCallback implements Callback<File>{

    public abstract void done(File imageFile);
}
