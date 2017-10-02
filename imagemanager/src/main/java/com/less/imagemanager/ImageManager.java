package com.less.imagemanager;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.less.imagemanager.util.Bytes;
import com.less.imagemanager.util.DESedeCoder;
import com.less.imagemanager.util.L;
import com.less.imagemanager.util.Zips;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import static com.less.imagemanager.util.Bytes.bytesToLong;

/**
 * Created by Administrator on 2017/9/27.
 */

public class ImageManager {
    private static ImageManager instance;

    private String base64SecretKey;

    private static final int HEADER_SIZE = Long.SIZE / Byte.SIZE + 4;// start(2 byte) ==> long length(8byte) <== end(2 byte)

    private Executor executorWork = Executors.newCachedThreadPool();

    private Executor executorUI = new Executor() {
        private Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            handler.post(runnable);
        }
    };

    static {
        System.loadLibrary("inject");
    }

    public ImageManager(){
        base64SecretKey = DESedeCoder.initKey();
    }
    public static ImageManager getInstance(){
        if (instance == null) {
            synchronized (ImageManager.class) {
                if (instance == null) {
                    instance = new ImageManager();
                }
            }
        }
        return instance;
    }

    public native int doEncrypt(Bitmap bitmap,byte[] header,byte[] data);
    public native byte[] doDecrypt(Bitmap bitmap, int offset, int length);

    /**
     * 加密
     */
    public void encrypt(final String message, final Bitmap inBitmap, final String savePath, final Callback callback) {
        executorWork.execute(new Runnable() {
            @Override
            public void run() {
                // 压缩数据(注意,只压缩message,不压缩header)
                byte[] data = Zips.compress(message.getBytes());
                // 加密数据
                data = DESedeCoder.encrypt(data, base64SecretKey);
                checkSave(message,inBitmap);

                byte[] header = createLenToHeader(data.length);

                // 填充byte[],为了能被24整除8[byte] * 3[r|g|b],虽然浪费少量空间也是值得的。
                if (data.length % 24 != 0) {
                    data = Arrays.copyOf(data, data.length + (24 - data.length % 24));
                }
                // encrypt
                Bitmap outBitmap = inBitmap.copy(Bitmap.Config.ARGB_8888, true);

                int result = doEncrypt(outBitmap,header,data); // jni 优化
                if (result == 1) {
                    L.d("JNI 回调成功！");
                    save(savePath,outBitmap,callback);
                }

            }
        });
    }

    /**
     * 创建头信息以保存 《message》长度
     * @param length message的长度存入头信息
     */
    private byte[] createLenToHeader(long length) {
        byte[] header = new byte[HEADER_SIZE];
        int i = 0;
        header[i++] = (byte) 0x5B;// 0101 1011
        header[i++] = (byte) 0x5B;// 0101 1011
        // =============================
        for (byte b : Bytes.longToBytes(length)) {
            header[i++] = b;
        }
        // =============================
        header[i++] = (byte) 0x5D;// 0101 1101
        header[i++] = (byte) 0x5D;// 0101 1101
        return header;
    }

    /**
     * 检测是否图片大小是否可存入信息
     * @param message
     */
    private void checkSave(String message,Bitmap bitmap) {
        int need = DESedeCoder.encrypt(Zips.compress(message.getBytes()),base64SecretKey).length;// byte[]
        int maxSaveLen = avaliableByteSize(bitmap);// byte[]
        if(need > maxSaveLen){
            throw new IllegalArgumentException("图片的内存太小，不能保存当前数据");
        }
    }

    /**
     * 解密
     * @param bitmap
     * @return
     */
    public void decrypt(final Bitmap bitmap, final Callback callback) {
        executorWork.execute(new Runnable() {
            @Override
            public void run() {
                byte[] header = doDecrypt(bitmap, 0, HEADER_SIZE);
                int len = (int) bytesToLong(Arrays.copyOfRange(header, 2, HEADER_SIZE - 2));//  取出文本的长度信息。
                byte[] body = doDecrypt(bitmap, HEADER_SIZE, len);
                body = DESedeCoder.decrypt(body, base64SecretKey);// 解密数据
                final byte[] data = Zips.deCompress(body);// 解压缩数据

                executorUI.execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.done(new String(data));
                    }
                });
            }
        });

    }

    private void save(String path, Bitmap outBitmap, final Callback callback){
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            outBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

            final File file = new File(path);
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.flush();
            fileOutputStream.close();
            executorUI.execute(new Runnable() {
                @Override
                public void run() {
                    callback.done(file);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int avaliableByteSize(Bitmap bitmap){
        if (bitmap == null) return 0;
        return (bitmap.getWidth() * bitmap.getHeight())*3/8 - HEADER_SIZE;
    }
}
