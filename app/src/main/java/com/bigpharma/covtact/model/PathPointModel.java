package com.bigpharma.covtact.model;

public class PathPointModel {
    private int id;
    private int pathPointIndex;
    private int pathId;
    private java.sql.Date date;
    private double longtitude;
    private double latitude;

    public PathPointModel(java.sql.Date date, double longtitude, double latitude) {
        this.date = date;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.pathPointIndex = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPathPointIndex() {
        return pathPointIndex;
    }

    public void setPathPointIndex(int pathPointIndex) {
        this.pathPointIndex = pathPointIndex;
    }

    public int getPathId() {
        return pathId;
    }

    public void setPathId(int pathId) {
        this.pathId = pathId;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(java.sql.Date date) {
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
