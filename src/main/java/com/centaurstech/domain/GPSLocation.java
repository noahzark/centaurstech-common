package com.centaurstech.domain;

import java.io.Serializable;

/**
 * Created by Feliciano on 7/11/2018.
 */
@SuppressWarnings("serial")
public class GPSLocation implements Serializable {

    Double lat;
    Double lng;

    public GPSLocation() {
        this(0.0, 0.0);
    }
    
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

    @Override
    public String toString() {
        return lat + ","+ lng;
    }

    public String toGaodeString() {
        return lng + "," + lat;
    }

}
