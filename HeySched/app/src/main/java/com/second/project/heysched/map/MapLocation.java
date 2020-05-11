package com.second.project.heysched.map;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class MapLocation {
    Context context;
    String[] permission_list;
    LocationManager locationManager;

    public MapLocation(Context context) {
        this.context = context;
    }

    public MapLocation(Context context, String[] permission_list) {
        this.context = context;
        this.permission_list = permission_list;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public LatLng getMyLocation() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for(String permission : permission_list) {
                if(context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
            }
        }
        Location gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(gps_loc != null) {
            return new LatLng(gps_loc.getLatitude(), gps_loc.getLongitude());
        } else if (network_loc != null) {
            return new LatLng(network_loc.getLatitude(), network_loc.getLongitude());
        }

        return null;
    }

    public LatLng getLatLngFromAddress(String address) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> list = null;
        try {
            list = geocoder.getFromLocationName(address, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude());

    }
}
