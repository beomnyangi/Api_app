package com.nova.api_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String OAUTH_CLIENT_ID = "1648z0s13gQ4UBrJYDhh";
    private String OAUTH_CLIENT_SECRET = "DRy2mkQWKI";
    private String OAUTH_CLIENT_NAME = "test";

    boolean login_check = false;
    boolean thread_check = false;

    String accessToken;

    ImageButton ib_login;
    ImageButton ib_logout;

    TextView textView;

    String name;

    OAuthLogin mOAuthLoginModule;
    Context mContext;

    Handler handler_get = new Handler(Looper.myLooper());
    Runnable runnable_get = new Runnable() {
        @Override
        public void run() {

        }
    };

    Handler handler = new Handler(Looper.myLooper());
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Thread thread = new Thread(new get_info());
            thread.start();
            if(login_check == false){
                ib_logout.setVisibility(View.INVISIBLE);
                ib_login.setVisibility(View.VISIBLE);
                System.out.println("asdfsdfasfdasfdasf");
                textView.setText(name);
                //handler.removeCallbacks(runnable);
                //Toast.makeText(getApplicationContext(), "로그인 해야됨", Toast.LENGTH_SHORT).show();
            }
            if(login_check == true){
                ib_login.setVisibility(View.INVISIBLE);
                ib_logout.setVisibility(View.VISIBLE);
                //Toast.makeText(getApplicationContext(), name+"님, 로그인 성공", Toast.LENGTH_SHORT).show();
                textView.setText(name+"님이 로그인 함");
                //handler.removeCallbacks(runnable);
            }

            handler.postDelayed(runnable,500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        ib_login = findViewById(R.id.ib_login);
        ib_logout = findViewById(R.id.ib_logout);
        textView = findViewById(R.id.textView);


        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                MainActivity.this
                ,OAUTH_CLIENT_ID
                ,OAUTH_CLIENT_SECRET
                ,OAUTH_CLIENT_NAME
                //,OAUTH_CALLBACK_INTENT
                // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.
        );

        ib_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * OAuthLoginHandler를 startOAuthLoginActivity() 메서드 호출 시 파라미터로 전달하거나 OAuthLoginButton
                 객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다.
                 */
                @SuppressLint("HandlerLeak")
                OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
                    @Override
                    public void run(boolean success) {
                        if (success) {
                            accessToken = mOAuthLoginModule.getAccessToken(mContext);
                            String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);
                            long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);
                            String tokenType = mOAuthLoginModule.getTokenType(mContext);

                            Log.i("LoginData","accessToken : "+ accessToken);
                            Log.i("LoginData","refreshToken : "+ refreshToken);
                            Log.i("LoginData","expiresAt : "+ expiresAt);
                            Log.i("LoginData","tokenType : "+ tokenType);

                        } else {
                            String errorCode = mOAuthLoginModule
                                    .getLastErrorCode(mContext).getCode();
                            String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                            Toast.makeText(mContext, "errorCode:" + errorCode
                                    + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                        }
                    };

                };
                mOAuthLoginModule.startOauthLoginActivity(MainActivity.this, mOAuthLoginHandler);
            }
        });

        ib_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOAuthLoginModule.logout(MainActivity.this);
                //Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                accessToken = null;
            }
        });


        Button bt_youtube_api = findViewById(R.id.bt_youtube_api);
        bt_youtube_api.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버튼이 클릭 됐을 때
                Intent intent = new Intent(MainActivity.this, searchyoutube_activity.class);
                startActivity(intent);
            }
        });

        Button bt_translation_api = findViewById(R.id.bt_translation_api);
        bt_translation_api.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버튼이 클릭 됐을 때
                Intent intent = new Intent(MainActivity.this, papago_translate_activity.class);
                startActivity(intent);
            }
        });

        handler.post(runnable);

    }

    class get_info implements Runnable {
        @Override
        public void run() {
            String token = accessToken; // 네이버 로그인 접근 토큰;
            String header = "Bearer " + token; // Bearer 다음에 공백 추가

            String apiURL = "https://openapi.naver.com/v1/nid/me";

            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Authorization", header);
            String responseBody = get(apiURL,requestHeaders);

            //Log.i("LoginData","get id info : "+ responseBody);
            System.out.println(responseBody);

            try {
                //넘어온 result 값을 JSONObject 로 변환해주고, 값을 가져오면 되는데요.
                // result 를 Log에 찍어보면 어떻게 가져와야할 지 감이 오실거에요.
                JSONObject object = new JSONObject(responseBody);
                //Log.d("LoginData","결과 : "+responseBody);
                if (object.getString("resultcode").equals("00")) {
                    JSONObject jsonObject = new JSONObject(object.getString("response"));
                    String email = jsonObject.getString("email");
                    name = jsonObject.getString("name");
                    //Log.d("jsonObject", jsonObject.toString());

                    //Log.i("LoginData","email : "+ email);
                    //Log.i("LoginData","name : "+ name);

                    login_check = true;
                }
                else {

                    login_check = false;
                    name = "로그인 정보 없음";
                }
                /*else if (object.getString("resultcode").equals("024")) {

                    login_check = false;
                    name = "로그인 정보 없음";
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private String get(String apiUrl, Map<String, String> requestHeaders){
            HttpURLConnection con = connect(apiUrl);
            try {
                con.setRequestMethod("GET");
                for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                    con.setRequestProperty(header.getKey(), header.getValue());
                }

                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                    return readBody(con.getInputStream());
                } else { // 에러 발생
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