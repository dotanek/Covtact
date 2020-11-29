package com.bigpharma.covtact.model;

import androidx.annotation.Nullable;

import java.util.Date;

public class PathModel {
    @Nullable
    private Integer id = null;
    private boolean deviceOwner;
    private Date startDate;
    private Date endDate;

    public PathModel(Date startDate) {
        this.startDate = startDate;
        this.endDate = startDate;
        this.id = null;
        this.deviceOwner = false;
    }

    @Nullable
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDeviceOwner() {
        return deviceOwner;
    }

    public void setDeviceOwner(boolean deviceOwner) {
        this.deviceOwner = deviceOwner;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "PathModel{" +
                "id=" + id +
                ", deviceOwner=" + deviceOwner +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
