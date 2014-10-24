package com.iosharp.android.whereareyou.model;

public class Point {

    private int mId;
    private double mLatitude;
    private double mLongitude;

    public Point() {}

    public Point(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    @Override
    public String toString() {
        return "Point{" +
                "mId=" + mId +
                ", mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                '}';
    }
}
