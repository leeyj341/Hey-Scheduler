package com.second.project.heysched.plan.fragment;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.second.project.heysched.R;
import com.second.project.heysched.plan.adapter.PlaceAutocompleteAdapter;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements PlaceAutocompleteAdapter.PlaceAutoCompleteInterface, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener {

    private int page;

    private static final int REQ_CODE_SPEECH_INPUT = 100;

    // google place api
    private GoogleApiClient googleApiClient;
    private LinearLayoutManager llm;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(-0, 0), new LatLng(0, 0));

    // views
    private RecyclerView rvAutocomplateKeyword;
    private EditText edSearch = null;
    private ImageView search_btn;
    private CardView recommand_card_view;

    public static SearchFragment newInstance(int page) {
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("someint", page);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_location, container, false);

        // call 한 activity 받기
        final Activity activity = getActivity();

        // set views





        init(activity);

        return view;
    }

    private void init(Activity activity) {
        // 장소 찾기 초기화
        initPlace(activity);
    }

    private void initPlace(Activity activity) {


        googleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(this.getActivity(), 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        rvAutocomplateKeyword = (RecyclerView) activity.findViewById(R.id.place_list_view);
        rvAutocomplateKeyword.setHasFixedSize(true);
        llm = new LinearLayoutManager(activity);
        rvAutocomplateKeyword.setLayoutManager(llm);
        edSearch = (EditText) activity.findViewById(R.id.search_view);
        edSearch = activity.findViewById(R.id.search_view);
        search_btn = activity.findViewById(R.id.search_btn);
//        recommand_card_view = activity.findViewById(R.id.recommand_card_view);
        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(activity, R.layout.autocomplete_item, googleApiClient, BOUNDS_INDIA, null);
        rvAutocomplateKeyword.setAdapter(placeAutocompleteAdapter);

//        recommand_card_view.removeAllViews();

        // 글자를 입력하면 place api를 요청
        this.edSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    if (placeAutocompleteAdapter != null) {
                        rvAutocomplateKeyword.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (placeAutocompleteAdapter != null) {
                        placeAutocompleteAdapter.clearList();
                        rvAutocomplateKeyword.setVisibility(View.GONE);
                    }
                }

                if (!s.toString().equals("") && googleApiClient.isConnected()) {
                    placeAutocompleteAdapter.getFilter().filter(s.toString());
                } else if (!googleApiClient.isConnected()) {
                    Log.e("", "NOT CONNECTED");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });


    }


    @Override

    public void onClick(View v) {

        switch (v.getId()) {
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }


    @Override

    public void onPlaceClick(ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> resultList, int position) {
        if (resultList != null) {
            try {
                final String placeId = String.valueOf(resultList.get(position).placeId);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(googleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getCount() == 1) {
                            // 키워드를 선택한 데이터 처리
                            Location location = new Location(places.get(0).getName().toString());
                            location.setLatitude(places.get(0).getLatLng().latitude);
                            location.setLongitude(places.get(0).getLatLng().longitude);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
            } finally {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        placeAutocompleteAdapter.clearList();
                        rvAutocomplateKeyword.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    @Override
    public void onStart() {
        this.googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        this.googleApiClient.disconnect();
        super.onStop();
    }
}
