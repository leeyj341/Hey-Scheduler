package com.second.project.heysched.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.second.project.heysched.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnCameraMoveListener {
    GoogleMap gMap;
    MarkerOptions markerOptions;
    String[] permission_list = {Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION};

    MapLocation mapLocation;
    String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_route);
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");

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

            gMap.moveCamera(CameraUpdateFactory.newLatLng(mapLocation.getMyLocation()));
            gMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if(result == PackageManager.PERMISSION_DENIED) return;
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
            String path = getPath(mapLocation.getMyLocation(), new LatLng(37.5129907,127.1005382), mode);
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


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public String getPath(LatLng origin, LatLng destination, String mode) {
            StringBuffer path = new StringBuffer("https://maps.googleapis.com/maps/api/directions/json?");
            path.append("origin=").append(origin.latitude).append(",").append(origin.longitude).append("&");
            path.append("destination=").append(destination.latitude).append(",").append(destination.longitude).append("&");
            path.append("mode=").append(mode).append("&");
            path.append("departure_time=").append(System.currentTimeMillis() / 1000).append("&");
            path.append("language=ko&");
            path.append("key=").append("AIzaSyABcoK6IL4ctXEp3TOXQ_fxrKN8v0eP9MI");
            return path.toString();
        }
    }
}
