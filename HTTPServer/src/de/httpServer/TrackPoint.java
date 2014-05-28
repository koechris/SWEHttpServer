package de.httpServer;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class TrackPoint implements Serializable {
    @Expose private double latitude;
    @Expose private double longitude;
    @Expose private double altitude;
    @Expose private long timestamp;
    @Expose private boolean isCheckPoint;
}
