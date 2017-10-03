package com.less.steganography;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.less.imagemanager.ImageManager;
import com.less.imagemanager.ResultCallback;
import com.less.imagemanager.StringCallback;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener {
	private String path = "/sdcard/test.png";
	private EditText et_input ;

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
		String text = FileUtils.readRaw(getResources(),R.raw.test);
		et_input.setText(text);
	}

	@Override
	public void onClick(View view) {
		Bitmap bitmap_encrypt = BitmapFactory.decodeResource(getResources(), R.raw.meinv);
		switch (view.getId()) {
			case R.id.btn_encrypt:
				String text = et_input.getText().toString();
				// init
				ImageManager.getInstance().encrypt(text, bitmap_encrypt, path, new ResultCallback() {
					@Override
					public void done(File imageFile) {
						Toast.makeText(MainActivity.this, "加密完成-> " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
					}
				});
				break;
			case R.id.btn_decrypt:
				Bitmap bitmap_decrypt = BitmapFactory.decodeFile(path);
				ImageManager.getInstance().decrypt(bitmap_decrypt, new StringCallback() {
					@Override
					public void done(String message) {
						et_input.setText("Success:->" + message);
						Toast.makeText(MainActivity.this, "解密: " + message, Toast.LENGTH_SHORT).show();
					}
				});
				break;
			case R.id.btn_avaliable:
				int size = ImageManager.getInstance().avaliableByteSize(bitmap_encrypt);// 可写字节数
				Toast.makeText(this, "可写大小: " + size + " 个byte", Toast.LENGTH_SHORT).show();
				break;
			case R.id.btn_encrypt_file:

				ImageManager.getInstance().encrypt();
				break;
			case R.id.btn_decrypt_file:

				break;
		}
	}
}
