package com.example.api_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class papago_translate_activity extends YouTubeBaseActivity {
	EditText et_search;
	TextView tv_result;
	Button bt_search;

	String value;

	String result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_papago_translate);

		et_search = findViewById(R.id.et_search);
		bt_search = findViewById(R.id.bt_search);
		tv_result = findViewById(R.id.tv_result);

		bt_search.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				//버튼이 클릭 됐을 때
				value = String.valueOf(et_search.getText());

				Thread thread = new Thread(new get_info());
				thread.start();

				tv_result.setText(""+result);
			}
		});

		et_search.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// 입력란에 변화가 있을 시 조치
				value = String.valueOf(et_search.getText());
				Thread thread = new Thread(new get_info());
				thread.start();
				tv_result.setText(""+result);
				System.out.println("111111111");
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// 입력이 끝났을 때 조치
				value = String.valueOf(et_search.getText());
				Thread thread = new Thread(new get_info());
				thread.start();
				tv_result.setText(""+result);
				System.out.println("22222222");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// 입력하기 전에 조치
				value = String.valueOf(et_search.getText());
				Thread thread = new Thread(new get_info());
				thread.start();
				tv_result.setText(""+result);
				System.out.println("3333333");
			}
		});
	}

	class get_info implements Runnable {
		@Override
		public void run() {
			String clientId = "1648z0s13gQ4UBrJYDhh";//애플리케이션 클라이언트 아이디값";
			String clientSecret = "DRy2mkQWKI";//애플리케이션 클라이언트 시크릿값";

			String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
			String text;
			try {
				text = URLEncoder.encode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("인코딩 실패", e);
			}

			Map<String, String> requestHeaders = new HashMap<>();
			requestHeaders.put("X-Naver-Client-Id", clientId);
			requestHeaders.put("X-Naver-Client-Secret", clientSecret);

			String responseBody = post(apiURL, requestHeaders, text);

			System.out.println(responseBody);

			try {
				//넘어온 result 값을 JSONObject 로 변환해주고, 값을 가져오면 되는데요.
				// result 를 Log에 찍어보면 어떻게 가져와야할 지 감이 오실거에요.
				JSONObject object = new JSONObject(responseBody);
				Log.d("LoginData","결과 : "+responseBody);


				JSONObject messageobject = new JSONObject(object.getString("message"));

				JSONObject resultobject = new JSONObject(messageobject.getString("result"));

				result = resultobject.getString("translatedText");

				Log.i("LoginData","value : "+ result);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		private String post(String apiUrl, Map<String, String> requestHeaders, String text){
			HttpURLConnection con = connect(apiUrl);
			String postParams = "source=ko&target=en&text=" + text; //원본언어: 한국어 (ko) -> 목적언어: 영어 (en)
			try {
				con.setRequestMethod("POST");
				for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
					con.setRequestProperty(header.getKey(), header.getValue());
				}

				con.setDoOutput(true);
				try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
					wr.write(postParams.getBytes());
					wr.flush();
				}

				int responseCode = con.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
					return readBody(con.getInputStream());
				} else {  // 에러 응답
					return readBody(con.getErrorStream());
				}
			} catch (IOException e) {
				throw new RuntimeException("API 요청과 응답 실패", e);
			} finally {
				con.disconnect();
			}
		}

		private HttpURLConnection connect(String apiUrl){
			try {
				URL url = new URL(apiUrl);
				return (HttpURLConnection)url.openConnection();
			} catch (MalformedURLException e) {
				throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
			} catch (IOException e) {
				throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
			}
		}

		private String readBody(InputStream body){
			InputStreamReader streamReader = new InputStreamReader(body);

			try (BufferedReader lineReader = new BufferedReader(streamReader)) {
				StringBuilder responseBody = new StringBuilder();

				String line;
				while ((line = lineReader.readLine()) != null) {
					responseBody.append(line);
				}

				return responseBody.toString();
			} catch (IOException e) {
				throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
			}
		}

	}


}