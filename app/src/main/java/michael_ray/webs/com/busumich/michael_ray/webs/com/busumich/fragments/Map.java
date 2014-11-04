package michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.fragments;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import michael_ray.webs.com.busumich.R;
import michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.logic.Bus;
import michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.logic.Parser;

public class Map extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private Location myLocation;
    private LocationClient mLocationClient;
    private boolean mapSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));
        actionBar.setDisplayHomeAsUpEnabled(true);
        mLocationClient = new LocationClient(this, this, this);
        mapSet = false;
        setUpMapIfNeeded();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Create the menu in the action bar for the interface
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_map:
                return true;
            case R.id.action_refresh:
                setUpMap();
                return true;
            case R.id.home:
                Intent goHome = new Intent(Map.this, Home.class);
                startActivity(goHome);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            mMap.setMyLocationEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.276946, -83.738220), 14));
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
            handler.post(new Runnable() {
                    public void run() {
                        new Refresh().execute();
                    }
                });
            }
        };
        timer.schedule(task, 0, 4000);
    }

    class Refresh extends AsyncTask<Void, Void, ArrayList<Bus>> {
        @Override
        protected ArrayList<Bus> doInBackground(Void...params) {
            Parser parser = new Parser(getApplicationContext());
            return parser.getBuses();
        }

        @Override
        protected void onPostExecute(ArrayList<Bus> buses) {
            mMap.clear();
            for (int i = 0; i < buses.size(); i++)
                mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(buses.get(i).getLat(), buses.get(i).getLon()))
                                .title(buses.get(i).getName())
                                .icon(BitmapDescriptorFactory.defaultMarker((int) ((Integer.parseInt(buses.get(i).getBusRoute().getColor(), 16) / 16777215.0) * 360)))
                                .visible(buses.get(i).getBusRoute().getActive()));
            if (!mapSet) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 17));
                mapSet = true;
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Google Play Location services methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onConnected(Bundle bundle) {
        myLocation = mLocationClient.getLastLocation();
    }

    @Override
    public void onDisconnected() { }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) { }
}
