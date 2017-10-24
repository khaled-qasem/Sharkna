package com.sharkna.khaled.sharkna.model;

/**
 * Created by Khaled on 10/24/2017.
 * Assumptions
 * Descriptions
 */

public class CurrentLocation {
    private String latitude;
    private String longitude;
    private static volatile CurrentLocation instance;

    private CurrentLocation() {
    }

    public static CurrentLocation getInstance() {
        if (instance == null) {
            synchronized (CurrentLocation.class) {
                if (instance == null) {
                    instance = new CurrentLocation();
                }
            }
        }
        return instance;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
