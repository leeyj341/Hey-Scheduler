package com.second.project.heysched.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.second.project.heysched.R;
import com.second.project.heysched.map.adapter.MapRouteAdapter;
import com.second.project.heysched.map.adapter.MapRouteItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnCameraMoveListener {
    GoogleMap gMap;
    MarkerOptions markerOptions;
    String[] permission_list = {Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION};
    MapLocation mapLocation;
    long arrivalTime;

    //레이아웃
    LinearLayout container;
    RecyclerView recyclerView;
    HashMap<String, TextView> mapTextView;

    //경로 정보 list
    List<MapRouteItem> wayDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_route);
        setView();

        Intent intent = getIntent();

        Date currentTime = Calendar.getInstance().getTime();
        arrivalTime = currentTime.getTime() / 1000;
        Log.d("test", arrivalTime + "");

        checkPermissions(permission_list);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        if(gMap != null) {
            mapLocation = new MapLocation(this, permission_list);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(mapLocation.getMyLocation());
            markerOptions.title("나의 위치");
            markerOptions.snippet("현재 내가 있는 위치");
            gMap.addMarker(markerOptions);
            gMap.getUiSettings().setZoomControlsEnabled(true);

            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLocation.getMyLocation(), 15));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if(result == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "권한이 설정되지 않았습니다. 앱을 다시 실행해주세요",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        init();
        loadDirections();
    }
    public void checkPermissions(String[] permission_list) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission_list, 101);
        } else {
            init();
            loadDirections();
        }
    }

    public void init() {
        FragmentManager manager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) manager.findFragmentById(R.id.plan_map);
        mapFragment.getMapAsync(this);
    }

    public void setView() {
        mapTextView = new HashMap<String, TextView>();
        mapTextView.put("소요시간", (TextView) findViewById(R.id.map_duration));
        mapTextView.put("출발시간", (TextView) findViewById(R.id.map_departure_time));
        mapTextView.put("도착시간", (TextView) findViewById(R.id.map_arrival_time));

        container = findViewById(R.id.map_bar_container);
        recyclerView = findViewById(R.id.map_route_list);

        wayDataList = new ArrayList<MapRouteItem>();
    }

    public void loadDirections() {
        if(gMap != null) {
            gMap.moveCamera(CameraUpdateFactory.newLatLng(mapLocation.getMyLocation()));
            gMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        }
        HttpsJson jsonWayInfo = new HttpsJson();
        jsonWayInfo.execute();

    }

    @Override
    public void onCameraMove() {

    }

    class HttpsJson extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... voids) {
            if(mapLocation == null )
                mapLocation = new MapLocation(MapActivity.this, permission_list);
            String path = getPath(mapLocation.getMyLocation(), new LatLng(37.3027264,127.0065257), arrivalTime);
            BufferedReader in = null;
            StringBuffer sb = null;
            JSONObject json = null;
            try {
                URL url = new URL(path);
                HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

                if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    sb = new StringBuffer();
                    String data = null;
                    while((data = in.readLine()) != null) {
                        data = data.replaceAll("(^\\p{Z}+|\\p{Z}+$)", "");
                        sb.append(data);
                    }
                    Log.d("test",sb.toString());
                    json = new JSONObject(sb.toString());

                    in.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            try {
                JSONArray legs = json.getJSONArray("routes")
                        .getJSONObject(0)
                        .getJSONArray("legs");
                Log.d("test1", legs.toString());
                JSONObject overview_polyline = json.getJSONArray("routes")
                        .getJSONObject(0)
                        .getJSONObject("overview_polyline");
                //Log.d("test1", overview_polyline.toString());

                ArrayList<LatLng> listPoints =
                        (ArrayList<LatLng>) DecodePolyline.getPoints(overview_polyline.getString("points"));
                /*for (LatLng point : listPoints) {
                    Log.d("test", point.latitude + "," + point.longitude);
                }*/
                Polyline line = gMap.addPolyline(new PolylineOptions()
                        .color(Color.argb(220,86, 129, 218))
                        .width(10));
                line.setPoints(listPoints);

                setViewDetail(legs);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public String getPath(LatLng origin, LatLng destination, long time) {
            StringBuffer path = new StringBuffer("https://maps.googleapis.com/maps/api/directions/json?");
            path.append("origin=").append(origin.latitude).append(",").append(origin.longitude).append("&");
            path.append("destination=").append(destination.latitude).append(",").append(destination.longitude).append("&");
            path.append("mode=transit&");
            path.append("arrival_time=").append(time).append("&");
            path.append("language=ko&");
            path.append("key=").append("AIzaSyABcoK6IL4ctXEp3TOXQ_fxrKN8v0eP9MI");
            return path.toString();
        }

        public void setViewDetail(JSONArray legs) {
            //텍스트뷰 설정
            try {
                String[] info = {
                        legs.getJSONObject(0).getJSONObject("duration").getString("text"),
                        legs.getJSONObject(0).getJSONObject("departure_time").getString("text"),
                        legs.getJSONObject(0).getJSONObject("arrival_time").getString("text"),
                };
                mapTextView.get("소요시간").setText(info[0]);
                mapTextView.get("출발시간").setText(info[1]);
                mapTextView.get("도착시간").setText(info[2]);

                //경로 설정
                int allTime = legs.getJSONObject(0).getJSONObject("duration").getInt("value");
                JSONArray steps = legs.getJSONObject(0).getJSONArray("steps");
                //adpterItem 리스트에 아이템 추가
                Log.d("test", steps.length() + "개");
                for (int i = 0; i < steps.length(); i++) {
                    String distance = steps.getJSONObject(i).getJSONObject("distance").getString("text");
                    String timeString = steps.getJSONObject(i).getJSONObject("duration").getString("text");
                    int time = steps.getJSONObject(i).getJSONObject("duration").getInt("value");
                    String html_instructions = steps.getJSONObject(i).getString("html_instructions");
                    String mode = steps.getJSONObject(i).getString("travel_mode");
                    String detailMode = "";
                    String number = "";
                    int stops = 0;
                    String start_stop = "";
                    String end_stop = "";
                    Log.d("test", distance);
                    if(mode.equals("TRANSIT")) {
                        detailMode = steps.getJSONObject(i).getJSONObject("transit_details")
                                .getJSONObject("line").getJSONObject("vehicle").getString("type");
                        number = steps.getJSONObject(i).getJSONObject("transit_details")
                                .getJSONObject("line").getString("short_name");
                        stops = steps.getJSONObject(i).getJSONObject("transit_details")
                                .getInt("num_stops");
                        start_stop = steps.getJSONObject(i).getJSONObject("transit_details")
                                .getJSONObject("departure_stop").getString("name");
                        end_stop = steps.getJSONObject(i).getJSONObject("transit_details")
                                .getJSONObject("arrival_stop").getString("name") + "역 하차 ";
                        if(detailMode.equals("SUBWAY")) {
                            html_instructions = start_stop + "역 탑승 후\n" + stops + " 정거장 이동";
                            wayDataList.add(new MapRouteItem(
                                    R.drawable.train,
                                    html_instructions,
                                    timeString,
                                    distance,
                                    end_stop
                            ));
                        }
                        else if(detailMode.equals("BUS")) {
                            html_instructions = "버스 " + number + "번 탑승 후\n" + stops + " 정거장 이동";
                            wayDataList.add(new MapRouteItem(
                                    R.drawable.bus,
                                    html_instructions,
                                    timeString,
                                    distance,
                                    end_stop
                            ));
                        }
                    }
                    else if (mode.equals("WALKING")) {
                        wayDataList.add(new MapRouteItem(
                                R.drawable.footprint,
                                html_instructions,
                                timeString,
                                distance
                        ));
                    }


                }

                //RecyclerView Adapter 설정
                for (MapRouteItem item : wayDataList) {
                    Log.d("test", item.toString());
                }
                setAdapter();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void setAdapter() {
            MapRouteAdapter adapter = new MapRouteAdapter(MapActivity.this, R.layout.map_route_row2, wayDataList);
            LinearLayoutManager manager = new LinearLayoutManager(MapActivity.this);
            manager.setOrientation(LinearLayoutManager.VERTICAL);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);

            recyclerView.setAdapter(adapter);


        }
    }
}
