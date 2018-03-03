
# Steganography

更新
===================================================== 

>tips 
如果你看到了这个库，那么恭喜你，README曾经删除过，so库源码也曾被删除并重建此项目，这也是我经过认真思考过的项目，
也许是国内唯一一个Java版本的图像隐写术项目。
我不是一个纯粹的开源爱好者，开源便意味着盗版和贬值，如果你喜欢，请参考思路开发优化自己的版本，请勿fork，请勿滥用。

:star2:
本项目的灵感在于曾经开发Android过程中，苦于没有相关的API或远程数据库存储信息，
而某些时候仅仅需要是`微量的《配置》`信息却需要自建服务器。尝试过自建PHP站点,
但是每年都要为此支付大量费用，服务器却大多处于闲置状态。后来出现Bmob后端云，但Bmob好像
时不时挂一下，服务不稳定或数据不安全。由此想到把配置写入图片，而图片是各大网站均可以
保存的介质，速度和隐匿性都极好。

项目思路源于1年前某旧项目的改进，旧项目因思路方式的设计问题导致内存耗用极大（已舍弃），
本项目加密和解密部分均由NDK优化，数据由`Gzip和3DES`处理后加密，所以加密性和隐匿性极高，请保存好
3DES的秘钥,否则将无法解密。核心NDK部分源码未开源，但so库已编译好，直接使用即可。

## 原理
空间域隐写技术是最容易实现的一种方式，通过直接修改图像的最低有效位(LSB)隐藏数据。
图片一般分为黑白和彩色，其中黑白图像的每个像素由8bit(1byte)组成，而彩色图片则由
ARGB(32bit)组成,`A(8bit),R(8bit),G(8bit),B(8bit)`,通过修改RGB的最低有效位，在视觉
上是无法察觉的。
你知道这张图片里面藏着什么信息吗?

```
![](screenshots/hide.jpg)
```

![image.png](http://upload-images.jianshu.io/upload_images/1281543-69c9cb03531384eb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 用法
1. 创建秘钥
```
ImageManager.createKey();
```
2. 设置秘钥(可选)
```
ImageManager.getInstance().setKey(secretKey);
```

3. 加密字符串
```
ImageManager.getInstance().encrypt(text, bitmap_encrypt, outEncryptImg, new ResultCallback() {
    @Override
    public void done(File imageFile) {
        toast("encrypt success ==> %s",imageFile.getAbsolutePath());
    }
});
```
4. 加密文件
```
byte[] data = FileUtils.readRawData(getResources(), R.raw.file);
ImageManager.getInstance().encrypt(data, bitmap_encrypt, outEncryptImg, new ResultCallback() {
    @Override
    public void done(File imageFile) {
        toast("encrypt success ==> Save %s",imageFile.getAbsolutePath());
    }
});
```
5. 解密字符串
```
Bitmap bitmap_decrypt = BitmapFactory.decodeFile(outEncryptImg);
ImageManager.getInstance().decrypt(bitmap_decrypt, new StringCallback() {
    @Override
    public void done(String message) {
        et_input.setText("Success: ==> " + message);
        toast("decrypt success ==> %s",message);
    }
});

```

6. 解密文件
```
Bitmap bitmap_decrypt = BitmapFactory.decodeFile(outEncryptImg);
ImageManager.getInstance().decrypt(bitmap_decrypt, new ByteArrayCallback() {
    @Override
    public void done(byte [] data) {
        String destPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/destFile.txt";
        FileUtils.writeData(data,destPath);
        toast("decrypt success ==> Save %s",destPath);
    }
});
```
