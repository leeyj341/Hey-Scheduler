package com.second.project.heysched.plan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchPlaceActivity extends AppCompatActivity implements View.OnClickListener {
    EditText search_view;
    ImageView search_btn;
    RecyclerView place_list_view;
    TextView recommand_place_title;
    TextView recommand_place_location;
    TextView recommand_place_hash;

    // search result list
    List<PlaceItem> recycler_data = new ArrayList<PlaceItem>();

    // crawling
    String crawlURL;
    String stringFormat;

    // adapter
    SearchPlaceAdapter adapter;

    final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            // 원래 하려던 동작 (UI변경 작업 등)
            adapter.notifyDataSetChanged();
        }
    };

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
                    Log.d("jsoup", "enter works");
                    search();
                    return true;
                }
                return false;
            }
        });


        adapter = new SearchPlaceAdapter(this, R.layout.place_row, recycler_data);

        // 장소 목록 list item의 onclickListener 설정
        adapter.setOnItemClickListener(new SearchPlaceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                PlaceItem item = recycler_data.get(position);

                Intent intent = getIntent();
                intent.putExtra("place_title", item.getPlace_title());
                intent.putExtra("place_location", item.getPlace_location());
                //intent.putExtra("place_hash", item.place_hash);

                setResult(RESULT_OK, intent);

                finish();
            }
        });

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
            case R.id.recommand_place_title:
                selectRecommand();
                break;
        }
    }
    private void selectRecommand(){
        Intent intent = getIntent();
        intent.putExtra("place_title", recommand_place_title.getText().toString());
        intent.putExtra("place_location", recommand_place_location.getText().toString());
        //intent.putExtra("place_hash", item.place_hash);

        setResult(RESULT_OK, intent);

        finish();

    }
    private void search() {
        Log.d("jsoup", "search method works");
        String search_words = search_view.getText().toString();
        Toast.makeText(this, "검색어 : " + search_words, Toast.LENGTH_SHORT).show();
        search_words = search_words.replace(" ", "+");
        crawlURL = "https://search.naver.com/search.naver?query=" + search_words;

        recycler_data.clear();
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("jsoup", "doInBackground");
            placeCrawling();
            Message msg = handler.obtainMessage();
            handler.sendMessage(msg);

            Log.d("jsoup", "finish");
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

    private void placeCrawling() {
        try {

            Document doc = Jsoup.connect(crawlURL).get();

            // place - title
            Elements places = doc.select("a.name");
            for (Element e : places) {
                Log.d("jsoup", "\n=========place========");
                // 장소명
                String title = e.attr("title");

                // 네이버 상세정보 url
                String innerURL = e.attr("href");
                Log.d("jsoup", "title" + title + "innerURL : " + innerURL);

                Document innerdoc = Jsoup.connect(innerURL).get();

                Elements element2 = innerdoc.select("ul.list_address");

                String location = "";
                for (Element e1 : element2) {
                    Elements e2 = e1.select("span.addr");
                    location = e2.text();
                    Log.d("jsoup", "addr : " + location);
/*                    PlaceItem item= new PlaceItem("세상의 모든 아침","서울","#브런치맛집");
                    recycler_data.add(item);*/

                }

                Elements element3 = innerdoc.select("span.kwd");

                String hashes = "";
                for (Element el : element3) {
                    hashes = hashes + "#" + el.text() + " ";

                }

                Log.d("jsoup", "hashes : " + hashes);

                PlaceItem item = new PlaceItem(title, location, hashes);
                recycler_data.add(item);

                Log.d("jsoup", "=========================");

                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
            }

            Elements maps = doc.select("dl.info_area");

            for (Element map : maps) {
                Elements e1 = map.select("a.tit");

                String title = e1.attr("title");
                String innerURL = e1.attr("href");
                Elements e2 = map.select("span.ad_txt");
                String location = e2.attr("title");
                Log.d("jsoup", "title : " + title + "\tlocation:" + location);
                Document innerdoc = Jsoup.connect(innerURL).get();

                Elements element2 = innerdoc.select("span.kwd");

                String hashes = "";
                for (Element el : element2) {
                    hashes = hashes + "#" + el.text() + " ";

                }

                Log.d("jsoup", "hashes : " + hashes);

                PlaceItem item = new PlaceItem(title, location, hashes);
                recycler_data.add(item);

                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setItem() {

    }


}





