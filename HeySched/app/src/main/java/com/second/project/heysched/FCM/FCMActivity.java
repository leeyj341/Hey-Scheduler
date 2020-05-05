package com.second.project.heysched.FCM;

import android.os.AsyncTask;
import android.os.Bundle;
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

public class FCMActivity extends AppCompatActivity {
    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcm);
        button = findViewById(R.id.fcm_send);
        textView = findViewById(R.id.fcm_message);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FCMTask().execute("테스트으");
            }
        });
    }

    class FCMTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                JSONObject object = new JSONObject();
                String apiKey = "AIzaSyA-n7dsLmvRFsKjEJJoWnTIkd1XhxaIgBg";

                object.put("test", strings[0]);
                String data = object.toString();

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .addHeader("Authorization", "key="+apiKey)
                        .url(url)
                        .post(RequestBody.create(MediaType.parse("application/json"), data))
                        .build();

                Response response = client.newCall(request).execute();
                result = response.body().string();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView.setText(s);
        }
    }
}
