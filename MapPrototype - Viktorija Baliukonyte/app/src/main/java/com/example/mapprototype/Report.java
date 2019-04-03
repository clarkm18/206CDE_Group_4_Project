package com.example.mapprototype;

import com.google.android.gms.maps.model.LatLng;

public class Report {
    private String Title;
    private String Description;
    private LatLng Position;
    private float Latitude, Longitude;

    public Report(){}

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public LatLng getPos() {
        return Position;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public void setPos(LatLng pos) {
        this.Position = pos;
    }

    public float getLatitude() {
        return Latitude;
    }

    public void setLatitude(float latitude) {
        this.Latitude = latitude;
    }

    public float getLongitude() {
        return Longitude;
    }

    public void setLongitude(float longitude) {
        this.Longitude = longitude;
    }
}
