package com.second.project.heysched.plan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.second.project.heysched.R;
import com.second.project.heysched.plan.adapter.PlaceItem;
import com.second.project.heysched.plan.adapter.SearchPlaceAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchPlaceActivity extends AppCompatActivity implements View.OnClickListener {
    EditText search_view;
    ImageView search_btn;
    RecyclerView place_list_view;
    TextView recommand_place_title;
    TextView recommand_place_location;
    TextView recommand_place_hash;

    // crawling
    String crawlURL;
    String stringFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_list);

        // set views
        search_view = findViewById(R.id.search_view);
        search_btn = findViewById(R.id.search_btn);
        place_list_view = findViewById(R.id.place_list_view);
        recommand_place_title = findViewById(R.id.recommand_place_title);
        recommand_place_location = findViewById(R.id.recommand_place_location);
        recommand_place_hash = findViewById(R.id.recommand_place_hash);


        search_btn.setOnClickListener(this);

        // enter 눌렀을때 검색
        search_view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //Enter키눌렀을떄 처리
                    search();
                    return true;
                }
                return false;
            }
        });

        List<PlaceItem> recycler_data = new ArrayList<PlaceItem>();

        for(int i=0;i<5;i++){
            PlaceItem item= new PlaceItem("세상의 모든 아침","서울","#브런치맛집");
            recycler_data.add(item);
        }

        SearchPlaceAdapter adapter = new SearchPlaceAdapter(this, R.layout.place_row, recycler_data);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        place_list_view.setLayoutManager(manager);

        place_list_view.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 검색 버튼 클릭시
            case R.id.search_btn:
                search();
                break;
        }
    }

    private void search(){
        String search_words = search_view.getText().toString();
        Toast.makeText(this, "검색어 : "+search_words, Toast.LENGTH_SHORT).show();
        search_words = search_words.replace(" ","+");
        crawlURL = "https://search.naver.com/search.naver?query="+search_words;

        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

    }
    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(crawlURL).get();

                Elements titles = doc.select("");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


}
