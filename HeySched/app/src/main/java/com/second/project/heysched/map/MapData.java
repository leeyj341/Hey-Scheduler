package com.second.project.heysched.map;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MapData {
    ArrayList<String> info;
    int allTime;
    String distance;
    String timeString;
    int time;
    String html_instructions;
    String mode;
    String detailMode;
    String number;
    int stops;
    String start_stop;
    String end_stop;

    public MapData(JSONArray legs) {
        setData(legs);
    }

    public void setData(JSONArray legs) {
        try {
            info.add(legs.getJSONObject(0).getJSONObject("duration").getString("text"));
            info.add(legs.getJSONObject(0).getJSONObject("departure_time").getString("text"));
            info.add(legs.getJSONObject(0).getJSONObject("arrival_time").getString("text"));
            allTime = legs.getJSONObject(0).getJSONObject("duration").getInt("value");

            this.distance = distance;
            this.timeString = timeString;
            this.time = time;
            this.html_instructions = html_instructions;
            this.mode = mode;
            this.detailMode = detailMode;
            this.number = number;
            this.stops = stops;
            this.start_stop = start_stop;
            this.end_stop = end_stop;

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
