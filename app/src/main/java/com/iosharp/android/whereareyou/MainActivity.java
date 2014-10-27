package com.iosharp.android.whereareyou;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.iosharp.android.whereareyou.model.Point;
import com.iosharp.android.whereareyou.sqlite.MySQLiteHelper;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    GoogleMap mMap;
    Location mLastLocation;
    ArrayList<LatLng> mPoints;
    boolean mAfterRotate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MySQLiteHelper db = new MySQLiteHelper(this);
        initializeMap();
        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (mLastLocation == null) {
                    mLastLocation = location;
                }

//                If the device was just rotated, redraw polyline with previous points.
//                if (mAfterRotate) {
//                    LatLng lastLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//
//                    PolylineOptions polylineOptions = new PolylineOptions().add(lastLocation).addAll(mPoints).color(Color.BLUE);
//                    mMap.addPolyline(polylineOptions);
//
//                    mAfterRotate = false;
//                }

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

    private LatLng toLatLng(Point point) {
        return new LatLng(point.getLatitude(), point.getLongitude());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
