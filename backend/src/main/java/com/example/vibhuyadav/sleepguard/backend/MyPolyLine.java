package com.example.vibhuyadav.sleepguard.backend;

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
