package com.second.project.heysched.dbtest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.second.project.heysched.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DBTestActivity extends AppCompatActivity {
    MemberVO user;
    TextView resultView;
    static final int MODE_INSERT = 0;
    static final int MODE_SELECT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_test);

        Button buttonGet = findViewById(R.id.data_get);
        Button buttonSend = findViewById(R.id.data_send);
        resultView = findViewById(R.id.result);

        user = new MemberVO("lee", "1234", "94/02/21", "test");

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpMember member = new HttpMember();
                member.execute(MODE_INSERT);
            }
        });

        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpMember member = new HttpMember();
                member.execute(MODE_SELECT);
            }
        });
    }

    class HttpMember extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... integers) {
            String result = "";
            if(integers[0] == MODE_INSERT) {
                URL url = null;
                JSONObject object = new JSONObject();

                try {
                    object.put("id",user.id);
                    object.put("pass",user.pass);
                    object.put("birthday",user.birthday);
                    object.put("token",user.token);
                    url = new URL("http://172.30.1.4:8088/heyScheduler/insert.do");

                    OkHttpClient client = new OkHttpClient();
                    String memberInfo = object.toString();
                    Log.d("test",memberInfo);
                    Request request = new Request.Builder()
                            .url(url)
                            .post(RequestBody.create(MediaType.parse("application/json"),memberInfo))
                            .build();

                    Response response = client.newCall(request).execute();
                    result = response.body().toString();
                    Log.d("test", result);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(integers[0] == MODE_SELECT) {
                URL url;
                JSONObject object = new JSONObject();

                try {
                    object.put("id", user.id);
                    object.put("pass", user.pass);
                    url = new URL("http://70.12.230.57:8088/heyScheduler/select.do");

                    OkHttpClient client = new OkHttpClient();
                    String memberInfo = object.toString();
                    Request request = new Request.Builder()
                            .url(url)
                            .post(RequestBody.create(MediaType.parse("applicaion/json"),memberInfo))
                            .build();

                    Response response = client.newCall(request).execute();
                    Log.d("test", response.toString());
                    result = response.body().string();
                    Log.d("test", result);


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            resultView.setText(result);
        }
    }
}
