package com.second.project.heysched.map.adapter;

public class MapRouteItem {
    int imageResource;
    String instruction;
    String duration;
    String distance;
    String endStop;

    public MapRouteItem(int imageResource, String instruction, String duration, String distance) {
        this.imageResource = imageResource;
        this.instruction = instruction;
        this.duration = duration;
        this.distance = distance;
    }

    public MapRouteItem(int imageResource, String instruction, String duration, String distance, String endStop) {
        this.imageResource = imageResource;
        this.instruction = instruction;
        this.duration = duration;
        this.distance = distance;
        this.endStop = endStop;
    }

    @Override
    public String toString() {
        return "MapRouteItem{" +
                "imageResource=" + imageResource +
                ", instruction='" + instruction + '\'' +
                ", duration='" + duration + '\'' +
                ", distance='" + distance + '\'' +
                ", endStop='" + endStop + '\'' +
                '}';
    }
}
