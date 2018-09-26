package com.centaurstech.domain;

/**
 * Created by Feliciano on 7/11/2018.
 */
public class GPSLocation {

    Double lat;
    Double lng;

    public GPSLocation(Integer lat, Integer lng) {
        this.lat = lat.doubleValue();
        this.lng = lng.doubleValue();
    }

    public GPSLocation(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String toString() {
        return lat + ","+ lng;
    }

    public String toGaodeString() {
        return lng + "," + lat;
    }

}
