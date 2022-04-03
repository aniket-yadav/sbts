package com.app.sbts.models;

public class BusLocation {
    String Bus_No;
    double Latitude;
    double Longitude;

    public BusLocation(String bus_No, double latitude, double longitude) {
        Bus_No = bus_No;
        Latitude = latitude;
        Longitude = longitude;
    }

    public BusLocation() {
    }

    public String getBus_No() {
        return Bus_No;
    }

    public void setBus_No(String bus_No) {
        Bus_No = bus_No;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
