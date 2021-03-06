package com.bigpharma.covtact.model;

import android.location.Location;

import com.bigpharma.covtact.util.Util;

import org.osmdroid.util.GeoPoint;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PathPointModel {
    private Integer id;
    private Integer pathPointIndex;
    private Integer pathId;
    private Date date;
    private double longtitude;
    private double latitude;

    public int getDateHHMM() {
        return Util.dateToHHMMInteger(this.date);
    }

    public PathPointModel(){}

    public PathPointModel(Date date, double longtitude, double latitude) {
        this.date = date;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.pathPointIndex = null;
        this.pathId = null;
        this.id = null;
    }
    public PathPointModel(PathPointModel pathPointModel) {
        this.id = pathPointModel.id;
        this.pathPointIndex = pathPointModel.pathPointIndex;
        this.pathId = pathPointModel.pathId;
        this.date = pathPointModel.date;
        this.longtitude = pathPointModel.longtitude;
        this.latitude = pathPointModel.latitude;
    }

    public GeoPoint toGeoPoint() {
        return new GeoPoint(this.latitude,this.longtitude);
    }

    public Location toLocation() {
        Location a = new Location(this.id == null ? "NULL" : String.format("%d",this.id));
        a.setLatitude(latitude);
        a.setLongitude(longtitude);
        return a;
    }

    /**
     * Returns distance in meters between to points of path
     *
     * @param b the b
     * @return the double
     */
    public double distanceTo(PathPointModel b) {
        return this.toLocation().distanceTo(b.toLocation());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPathPointIndex() {
        return pathPointIndex;
    }

    public void setPathPointIndex(Integer pathPointIndex) {
        this.pathPointIndex = pathPointIndex;
    }

    public Integer getPathId() {
        return pathId;
    }

    public void setPathId(Integer pathId) {
        this.pathId = pathId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> docData = new HashMap<>();
        docData.put("id", id);
        docData.put("pathPointIndex", pathPointIndex);
        docData.put("pathId", pathId);
        docData.put("date", date);
        docData.put("longtitude", longtitude);
        docData.put("latitude", latitude);

        return docData;
    }

    @Override
    public String toString() {
        return "PathPointModel{" +
                "id=" + id +
                ", pathPointIndex=" + pathPointIndex +
                ", pathId=" + pathId +
                ", date=" + date +
                ", longtitude=" + longtitude +
                ", latitude=" + latitude +
                '}';
    }
}
