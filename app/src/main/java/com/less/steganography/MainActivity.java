package com.less.steganography;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.less.imagemanager.ByteArrayCallback;
import com.less.imagemanager.ImageManager;
import com.less.imagemanager.ResultCallback;
import com.less.imagemanager.StringCallback;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener {
	private String outEncryptImg = Environment.getExternalStorageDirectory().getAbsolutePath() + "/encrypt.png";
	private EditText et_input ;
	private String secretKey = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.btn_encrypt).setOnClickListener(this);
		findViewById(R.id.btn_decrypt).setOnClickListener(this);
		findViewById(R.id.btn_avaliable).setOnClickListener(this);
		findViewById(R.id.btn_encrypt_file).setOnClickListener(this);
		findViewById(R.id.btn_decrypt_file).setOnClickListener(this);
		et_input = findViewById(R.id.et_input);

		// 测试一段长文本
		String text = FileUtils.readRawString(getResources(),R.raw.msg);
		et_input.setText(text);
		secretKey = ImageManager.createKey();
		toast("!!!---> please must save the secretKey <---!!!");

		ImageManager.getInstance().setKey(secretKey);
	}

	@Override
	public void onClick(View view) {
		Bitmap bitmap_encrypt = BitmapFactory.decodeResource(getResources(), R.raw.meinv);
		switch (view.getId()) {
			case R.id.btn_encrypt:
				String text = et_input.getText().toString();
				// init
				ImageManager.getInstance().encrypt(text, bitmap_encrypt, outEncryptImg, new ResultCallback() {
					@Override
					public void done(File imageFile) {
						toast("encrypt success ==> %s",imageFile.getAbsolutePath());
					}
				});
				break;
			case R.id.btn_decrypt:
				Bitmap bitmap_decrypt1 = BitmapFactory.decodeFile(outEncryptImg);
				ImageManager.getInstance().decrypt(bitmap_decrypt1, new StringCallback() {
					@Override
					public void done(String message) {
						et_input.setText("Success: ==> " + message);
						toast("decrypt success ==> %s",message);
					}
				});
				break;
			case R.id.btn_avaliable:
				int size = ImageManager.getInstance().avaliableByteSize(bitmap_encrypt);// 可写字节数
				toast("avliable size ==> %d(byte)",size);
				break;
			case R.id.btn_encrypt_file:
				byte[] data = FileUtils.readRawData(getResources(), R.raw.file);
				ImageManager.getInstance().encrypt(data, bitmap_encrypt, outEncryptImg, new ResultCallback() {
					@Override
					public void done(File imageFile) {
						toast("encrypt success ==> Save %s",imageFile.getAbsolutePath());
					}
				});
				break;
			case R.id.btn_decrypt_file:
				Bitmap bitmap_decrypt2 = BitmapFactory.decodeFile(outEncryptImg);
				ImageManager.getInstance().decrypt(bitmap_decrypt2, new ByteArrayCallback() {
					@Override
					public void done(byte [] data) {
						String destPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/destFile.txt";
						FileUtils.writeData(data,destPath);
						toast("decrypt success ==> Save %s",destPath);
					}
				});
				break;
		}
	}

	public void toast(String format,Object ... params){
		Toast.makeText(MainActivity.this, String.format(format,params), Toast.LENGTH_SHORT).show();
	}
}
