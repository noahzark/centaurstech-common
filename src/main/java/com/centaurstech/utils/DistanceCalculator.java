package com.centaurstech.utils;

import com.centaurstech.domain.GPSLocation;

/**
 * Created by Feliciano on 7/11/2018.
 */
public class DistanceCalculator {

    public static int EARTH_RADIUS_IN_KM = 6378;

    private static double deg2rad(double deg){
        return deg*(Math.PI/180);
    }

    @Deprecated
    public static double distanceBetween(GPSLocation source, GPSLocation target) {
        return distanceBetween(source.getLat(), source.getLng(), target.getLat(), target.getLng(), EARTH_RADIUS_IN_KM);
    }

    public static double distanceBetweenInKM(GPSLocation source, GPSLocation target) {
        return distanceBetween(source.getLat(), source.getLng(), target.getLat(), target.getLng(), EARTH_RADIUS_IN_KM);
    }

    @Deprecated
    public static double distanceBetween(double sourceLat, double sourceLng, double targetLat, double targetLng) {
        return distanceBetween(sourceLat, sourceLng, targetLat, targetLng, EARTH_RADIUS_IN_KM);
    }

    public static double distanceBetweenInKM(double sourceLat, double sourceLng, double targetLat, double targetLng) {
        return distanceBetween(sourceLat, sourceLng, targetLat, targetLng, EARTH_RADIUS_IN_KM);
    }

    public static double distanceBetween(double sourceLat, double sourceLng, double targetLat, double targetLng, int radius) {
        double dLat = deg2rad(targetLat - sourceLat);
        double dLon = deg2rad(targetLng - sourceLng);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(deg2rad(sourceLat)) * Math.cos(deg2rad(targetLat)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        double d = radius * c;

        return d;
    }

}
