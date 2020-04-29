package com.second.project.heysched.map;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class DecodePolyline {
    public static List<LatLng> getPoints(String encodedPoints)
    {
        if (encodedPoints == null || encodedPoints == "") return null;
        List<LatLng> poly = new ArrayList<LatLng>();
        char[] polylinechars = encodedPoints.toCharArray();
        int index = 0;

        int currentLat = 0;
        int currentLng = 0;
        int next5bits;
        int sum;
        int shifter;

        try
        {
            while (index < polylinechars.length)
            {
                // calculate next latitude
                sum = 0;
                shifter = 0;
                do
                {
                    next5bits = (int)polylinechars[index++] - 63;
                    sum |= (next5bits & 31) << shifter;
                    shifter += 5;
                } while (next5bits >= 32 && index < polylinechars.length);

                if (index >= polylinechars.length)
                    break;

                currentLat += (sum & 1) == 1 ? ~(sum >> 1) : (sum >> 1);

                //calculate next longitude
                sum = 0;
                shifter = 0;
                do
                {
                    next5bits = (int)polylinechars[index++] - 63;
                    sum |= (next5bits & 31) << shifter;
                    shifter += 5;
                } while (next5bits >= 32 && index < polylinechars.length);

                if (index >= polylinechars.length && next5bits >= 32)
                    break;

                currentLng += (sum & 1) == 1 ? ~(sum >> 1) : (sum >> 1);
                LatLng p = new LatLng((double) currentLat / 100000.0,(double) currentLng / 100000.0);
                poly.add(p);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return poly;
    }

}
