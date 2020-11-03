package com.example.api_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public class menu_activity extends YouTubeBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);


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

	}
}