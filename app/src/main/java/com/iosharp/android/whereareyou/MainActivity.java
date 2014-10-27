package com.iosharp.android.whereareyou;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.iosharp.android.whereareyou.model.Point;
import com.iosharp.android.whereareyou.sqlite.MySQLiteHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    GoogleMap mMap;
    Location mLastLocation;
    ArrayList<LatLng> mPoints;
    boolean mAfterRotate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);
        final MySQLiteHelper db = new MySQLiteHelper(this);

        Button button = (Button) findViewById(R.id.clear_points_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteAll();

            }
        });

        initializeMap();
        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (mLastLocation == null) {
                    mLastLocation = location;
                }

//                If the device was just rotated, redraw polyline with previous points.
                if (mAfterRotate) {

                    List<LatLng> latLngs = new LinkedList<LatLng>();

                    for (Point p: db.getAllPoints()) {
                        LatLng latLng = new LatLng(p.getLatitude(), p.getLongitude());
                        latLngs.add(latLng);
                    }

                    LatLng lastLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                    PolylineOptions polylineOptions = new PolylineOptions().add(lastLocation).addAll(latLngs).color(Color.BLUE);
                    mMap.addPolyline(polylineOptions);

                    mAfterRotate = false;
                }

                LatLng lastLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                LatLng thisLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                // Follow the person
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(thisLatLng, 20));

                PolylineOptions polylineOptions1 = new PolylineOptions().add(lastLatLng).add(thisLatLng).color(Color.RED);
                mMap.addPolyline(polylineOptions1);
                Point lastPoint = new Point(lastLatLng.latitude, lastLatLng.longitude);
                db.addPoint(lastPoint);

                mLastLocation = location;
            }
        });

    }

    private void initializeMap() {
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        }
    }
}
