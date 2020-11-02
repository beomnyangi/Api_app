package com.example.api_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class searchyoutube_activity extends AppCompatActivity {
	private String API_KEY = "AIzaSyAp7r8cvdjSIHPbbZyjx7vaKrsAyIAyINI";

	EditText et_search;

	Button bt_search;
	String get_value;
	searchyoutube_adapter adapter;
	RecyclerView recyclerView;

	String channelTitle;
	String Video_id;
	String Title;
	String Thumbnail;

	private Handler handler_send = new Handler(Looper.myLooper());
	Runnable runnable_send = new Runnable() {

		@Override
		public void run() {
			Log.i("LoginData","thread on : ");
			setRecyclerView();
			handler_send.post(runnable_send);
			handler_send.removeCallbacks(runnable_send);
			Log.i("LoginData","thread off : ");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchyoutube);

		adapter.info.clear();

		et_search = findViewById(R.id.et_search);
		bt_search = findViewById(R.id.bt_search);

		recyclerView = (RecyclerView) findViewById(R.id.recycle);
		setRecyclerView();

		bt_search.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				//버튼이 클릭 됐을 때
				System.out.println("asdfasdfasf");
				get_value = et_search.getText().toString();
				YoutubeAsyncTask youtubeAsyncTask = new YoutubeAsyncTask();
				youtubeAsyncTask.execute();
			}
		});
	}

	//recyclerView 와 adapter 를 연결시켜주는 메소드
	void setRecyclerView(){
		LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.removeAllViewsInLayout();
		recyclerView.setLayoutManager(layoutManager);

		//액티비티에 context 와 item 데이터를 Adapter 에 넘겨준다.
		adapter = new searchyoutube_adapter(this, R.layout.activity_searchyoutube_item);
		recyclerView.setAdapter(adapter);
	}

	private class YoutubeAsyncTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... voids) {
			try {
				HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
				final JsonFactory JSON_FACTORY = new JacksonFactory();
				final long NUMBER_OF_VIDEOS_RETURNED = 50;

				YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
					public void initialize(HttpRequest request) throws IOException {
					}
				}).setApplicationName("youtube-search-sample").build();

				YouTube.Search.List search = youtube.search().list("id,snippet");

				search.setKey(API_KEY);

				search.setQ(get_value);
				//search.setChannelId("UCk9GmdlDTBfgGRb7vXeRMoQ"); //레드벨벳 공식 유투브 채널
				search.setOrder("viewCount"); //조회수 높은 순으로 조회

				search.setType("video");

				//search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
				search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
				SearchListResponse searchResponse = search.execute();

				List<SearchResult> searchResultList = searchResponse.getItems();

				if (searchResultList != null) {
					prettyPrint(searchResultList.iterator(), "");
				}
			} catch (GoogleJsonResponseException e) {
				System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
						+ e.getDetails().getMessage());
				System.err.println("There was a service error 2: " + e.getLocalizedMessage() + " , " + e.toString());
			} catch (IOException e) {
				System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
			} catch (Throwable t) {
				t.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
		}

		public void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {
			if (!iteratorSearchResults.hasNext()) {
				System.out.println(" There aren't any results for your query.");
			}

			adapter.info.clear();
			while (iteratorSearchResults.hasNext()) {
				SearchResult singleVideo = iteratorSearchResults.next();
				ResourceId rId = singleVideo.getId();

				// Double checks the kind is video.
				if (rId.getKind().equals("youtube#video")) {

					channelTitle = singleVideo.getSnippet().getChannelTitle();
					Video_id = singleVideo.getId().getVideoId();

					Title = String.valueOf(singleVideo.getSnippet().getTitle());
					//Title = singleVideo.getSnippet().getTitle();

					Thumbnail = singleVideo.getSnippet().getThumbnails().getMedium().getUrl();

					System.out.println(" channelTitle : " + channelTitle);
					System.out.println(" Video Id : " + Video_id);
					System.out.println(" Title : " + Title);
					System.out.println(" Thumbnail : " + Thumbnail);
					System.out.println("\n-------------------------------------------------------------\n");

					adapter.addInfo(new searchyoutube_data(Thumbnail, Title, Video_id));
				}
			}
			handler_send.post(runnable_send);
		}
	}

}