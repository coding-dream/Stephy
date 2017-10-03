# Steganography
:star2:
本项目的灵感在于曾经开发Android过程中，苦于没有相关的API或远程数据库存储
信息，而某些时候仅仅需要是微量的《配置》信息却需要建立自己的服务器等。尝试过自建PHP站点,
但是每年都要为此支付大量费用，服务器却大多处于闲置状态。后来出现Bmob后端云，但Bmob好像
是不是挂一下，服务不稳定或数据不安全。由此想到把配置写入图片，而图片是各大网站均可以
保存的介质，速度和隐匿性都极好。

项目思路源于1年前某旧项目的改进，旧项目因思路方式的设计问题导致内存耗用极大（已舍弃），本
项目加密和解密部分均由NDK优化，数据由Gzip和3DES处理后加密，所以加密性和隐匿性极高，请保存好
3DES的秘钥,否则将无法解密。核心NDK部分源码未开源，但so库已编译好，直接使用即可。

### 原理
空间域隐写技术是最容易实现的一种方式，通过直接修改图像的最低有效位(LSB)隐藏数据。
图片一般分为黑白和彩色，其中黑白图像的每个像素由8bit(1byte)组成，而彩色图片则由
ARGB(32bit)组成,A(8bit),R(8bit),G(8bit),B(8bit),通过修改RGB的最低有效位，在视觉
上是无法察觉的。
你知道这张图片里面藏着什么信息吗?

![](screenshots/hide.jpg)

### 用法
创建秘钥
```
ImageManager.createKey();
```
设置秘钥(可选)
```
ImageManager.getInstance().setKey(secretKey);
```

加密字符串
```
ImageManager.getInstance().encrypt(text, bitmap_encrypt, outEncryptImg, new ResultCallback() {
    @Override
    public void done(File imageFile) {
        toast("encrypt success ==> %s",imageFile.getAbsolutePath());
    }
});
```
加密文件
```
byte[] data = FileUtils.readRawData(getResources(), R.raw.file);
ImageManager.getInstance().encrypt(data, bitmap_encrypt, outEncryptImg, new ResultCallback() {
    @Override
    public void done(File imageFile) {
        toast("encrypt success ==> Save %s",imageFile.getAbsolutePath());
    }
});
```
解密字符串
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

解密文件
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

### License
    Copyright 2017 wangli

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
 
        http://www.apache.org/licenses/LICENSE-2.0
 
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.