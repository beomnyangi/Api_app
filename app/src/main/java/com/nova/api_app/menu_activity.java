package com.nova.api_app;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhn.android.naverlogin.OAuthLogin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class menu_activity extends AppCompatActivity {

	ImageButton ib_logout;
	OAuthLogin mOAuthLoginModule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		Intent intent = getIntent();
  		String intent_name = intent.getStringExtra("name");

		TextView TextView = findViewById(R.id.textView);
		TextView.setText(intent_name);


		ib_logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mOAuthLoginModule.logout(menu_activity.this);
				Toast.makeText(menu_activity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(menu_activity.this, searchyoutube_activity.class);
				startActivity(intent);
			}
		});


		Button bt_youtube_api = findViewById(R.id.bt_youtube_api);
		bt_youtube_api.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				//버튼이 클릭 됐을 때
				Intent intent = new Intent(menu_activity.this, searchyoutube_activity.class);
				startActivity(intent);
			}
		});

		Button bt_translation_api = findViewById(R.id.bt_translation_api);
		bt_translation_api.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				//버튼이 클릭 됐을 때
				Intent intent = new Intent(menu_activity.this, papago_translate_activity.class);
				startActivity(intent);
			}
		});

		Button bt_voicerecognition_api = findViewById(R.id.bt_voicerecognition_api);
		bt_voicerecognition_api.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				//버튼이 클릭 됐을 때
				//Intent intent = new Intent(menu_activity.this, searchyoutube_activity.class);
				//startActivity(intent);
			}
		});

		getHashKey();

	}

	private void getHashKey(){
		PackageInfo packageInfo = null;
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		if (packageInfo == null) {
			Log.e("KeyHash", "KeyHash:null");
		}
		for (Signature signature : packageInfo.signatures) {
			try {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			} catch (NoSuchAlgorithmException e) {
				Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
			}
		}
	}
}