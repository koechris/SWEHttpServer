package de.httpServer;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class Route implements Serializable {
    
    @Expose private String name;
    private final ArrayList<TrackPoint> trackPoints;
    
    public Route (String name) {
	this.name = name;
	trackPoints = new ArrayList<TrackPoint>();
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }
    
    public void addTrackPoint (TrackPoint t) {
	trackPoints.add(t);
    }
    
    public ArrayList<TrackPoint> getTrackPoints () {
	return trackPoints;
    }
}
