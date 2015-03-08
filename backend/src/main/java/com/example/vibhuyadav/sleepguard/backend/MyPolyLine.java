package com.example.vibhuyadav.sleepguard.backend;

/**
 * Created by vibhuyadav on 3/8/2015.
 */

/**
 * Bean for Polylines.
 */
public class MyPolyLine {
    public double[][] coordinates;

    public MyPolyLine() {};

    public MyPolyLine(double[][] coordinates){
        this.coordinates = coordinates;
    }

    public double[][] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[][] coordinates) {
        this.coordinates = coordinates;
    }
}
