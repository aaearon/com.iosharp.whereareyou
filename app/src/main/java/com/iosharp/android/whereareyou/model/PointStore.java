package com.iosharp.android.whereareyou.model;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class PointStore {

    private ArrayList<LatLng> mPoints;

    private static PointStore sPointStore;
    private Context mAppContext;

    private PointStore(Context appContext) {
        mAppContext = appContext;
        mPoints = new ArrayList<LatLng>();
    }

    public static PointStore get(Context c) {
        if (sPointStore == null) {
            sPointStore = new PointStore(c.getApplicationContext());
        }
        return sPointStore;
    }

    public ArrayList<LatLng> getPoints() {
        return mPoints;
    }
}
