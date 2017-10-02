package com.less.imagemanager.util;

import android.util.Base64;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class DESedeCoder {
	private static final String KEY_ALGORITHM= "DESede";
	private static final String CIPHER_ALGORITHM= "DESede/ECB/PKCS5Padding";

	//生成秘钥
	public static String initKey(){
		try {
			KeyGenerator generator = KeyGenerator.getInstance(KEY_ALGORITHM);
			generator.init(168);//JDK仅支持112和 168位
			SecretKey secretKey = generator.generateKey();
			String secret = Base64.encodeToString(secretKey.getEncoded(), Base64.URL_SAFE);
			return secret;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	//把 秘钥 byte字节转为 Key
	private static Key getKey(byte[] key){
		SecretKey secretKey = null;
		try {
			//实例化DES秘钥材料
			DESedeKeySpec dks = new DESedeKeySpec(key);
			//实例化秘钥工厂
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
			//生成秘钥
			secretKey = keyFactory.generateSecret(dks);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return secretKey;

	}

	public static byte[] encrypt(byte[] data,String base64SecretKey){
		byte[] key = Base64.decode(base64SecretKey,Base64.URL_SAFE);
		byte [] encryptData = null;
		try {
			Key secretKey = getKey(key);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			// 初始化，设置为解密模式
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			// 执行操作
			encryptData = cipher.doFinal(data);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptData;
	}

	public static byte[] decrypt(byte[] data,String base64SecretKey){
		byte[] key = Base64.decode(base64SecretKey,Base64.URL_SAFE);
		byte [] decryptData = null;
		try {
			Key secretKey = getKey(key);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			decryptData = cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decryptData;
	}
}