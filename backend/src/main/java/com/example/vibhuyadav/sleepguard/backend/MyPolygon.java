package com.example.vibhuyadav.sleepguard.backend;

/**
 * Created by vibhuyadav on 3/8/2015.
 */

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

public class MyPolygon extends Polygon {
    private long ID;

    public MyPolygon(LinearRing shell, LinearRing[] holes, GeometryFactory factory, long ID) {
        super(shell, holes, factory);
        this.ID = ID;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }
}
