package com.example.vibhuyadav.sleepguard.backend;

/**
 * Created by vibhuyadav on 3/8/2015.
 */
public class Util {

    public static boolean computeDistance(Double lat1, Double lat2, Double lon1, Double lon2) {
        final int R = 6371; // Radius of the earth

        Double latDistance = deg2rad(lat2 - lat1);
        Double lonDistance = deg2rad(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        if (distance <= 100) {
            return true;
        } else
            return false;


    }

    private static double deg2rad(double deg) {

        return (deg * Math.PI / 180.0);
    }
}
