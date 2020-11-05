package com.nova.api_app;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public class playyoutube_activity extends YouTubeBaseActivity {
	private String API_KEY = "AIzaSyAp7r8cvdjSIHPbbZyjx7vaKrsAyIAyINI";

	YouTubePlayerView YouTubePlayerView;
	YouTubePlayer.OnInitializedListener listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playyoutube);

		Intent intent = getIntent();
		String intent_address = intent.getStringExtra("video_id");

		YouTubePlayerView = findViewById(R.id.YouTubePlayerView);

		listener = new YouTubePlayer.OnInitializedListener() {
			@Override
			public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
				//youTubePlayer.loadVideo("C8KdDA1YBh0");
				youTubePlayer.loadVideo(intent_address);
				//https://www.youtube.com/watch?v=NmkYHmiNArc 유투브에서 v="" 이부분이 키에 해당
			}

			@Override
			public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
				//YouTubePlayerView.initialize(API_KEY, listener);
			}
		};

		YouTubePlayerView.initialize(API_KEY, listener);

	}
}