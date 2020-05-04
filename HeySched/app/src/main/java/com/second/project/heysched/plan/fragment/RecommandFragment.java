package com.second.project.heysched.plan.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.second.project.heysched.MainActivity;
import com.second.project.heysched.R;
import com.second.project.heysched.plan.SearchPlaceActivity;
import com.second.project.heysched.plan.adapter.PlaceItem;
import com.second.project.heysched.plan.adapter.SearchPlaceAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecommandFragment extends Fragment {

    private String title;
    private int page;

    // views
    EditText search_view;
    ImageView search_btn;
    RecyclerView place_list_view;
    TextView recommand_place_title;
    TextView recommand_place_location;
    TextView recommand_place_hash;

    // adapter
    SearchPlaceAdapter adapter = null;

    // search result list
    List<PlaceItem> recycler_data;

    // crawling
    String crawlURL;

    public static RecommandFragment newInstance(int page) {
        RecommandFragment fragment = new RecommandFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("someint", page);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt",0);
        title = getArguments().getString("someTitle");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_location, container, false);

        // call 한 activity의 context 받기
        final Activity activity = getActivity();

        // set views
        search_view = view.findViewById(R.id.search_view);
        search_btn = view.findViewById(R.id.search_btn);
        place_list_view = view.findViewById(R.id.place_list_view);
        recommand_place_title = view.findViewById(R.id.recommand_place_title);
        recommand_place_location = view.findViewById(R.id.recommand_place_location);
        recommand_place_hash = view.findViewById(R.id.recommand_place_hash);


        // 검색 버튼 눌렀을때
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(activity);
            }
        });

        // enter 눌렀을때 검색
        search_view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //Enter키눌렀을떄 처리
                    Log.d("jsoup", "enter works");
                    search(activity);
                    return true;
                }
                return false;
            }
        });
        recycler_data = new ArrayList<PlaceItem>();

        recycler_data.add(new PlaceItem("맛집","서울",""));
        recycler_data.add(new PlaceItem("맛집","서울",""));
        recycler_data.add(new PlaceItem("맛집","서울",""));

        adapter = new SearchPlaceAdapter(activity, R.layout.place_row, recycler_data);

        // 장소 목록 list item의 onclickListener 설정
        adapter.setOnItemClickListener(new SearchPlaceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                PlaceItem item = recycler_data.get(position);

                Intent intent = activity.getIntent();
                intent.putExtra("place_title", item.getPlace_title());
                intent.putExtra("place_location", item.getPlace_location());
                //intent.putExtra("place_hash", item.place_hash);

                activity.setResult(activity.RESULT_OK, intent);

                activity.finish();
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(activity.getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        place_list_view.setLayoutManager(manager);

        place_list_view.setAdapter(adapter);

        return view;
    }

    private void search(Activity activity) {
        Log.d("jsoup", "search method works");
        String search_words = search_view.getText().toString();
        Toast.makeText(activity.getApplicationContext(), "검색어 : " + search_words, Toast.LENGTH_SHORT).show();
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
            adapter.notifyDataSetChanged();
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
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}