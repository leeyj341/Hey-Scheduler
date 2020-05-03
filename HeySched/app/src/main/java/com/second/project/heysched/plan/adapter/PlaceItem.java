package com.second.project.heysched.plan.adapter;

import androidx.annotation.NonNull;

public class PlaceItem {
    String place_title;
    String place_location;
    String place_hash;

    public PlaceItem(String place_title, String place_location, String place_hash) {
        this.place_title = place_title;
        this.place_location = place_location;
        this.place_hash = place_hash;
    }

    public String getPlace_title() {
        return place_title;
    }

    public String getPlace_location() {
        return place_location;
    }

    public String getPlace_hash() {
        return place_hash;
    }

    @Override
    public String toString() {
        return "";
    }
}
