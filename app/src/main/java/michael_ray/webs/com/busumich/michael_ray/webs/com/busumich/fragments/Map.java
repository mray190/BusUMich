package michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.fragments;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

public class Map extends FragmentActivity {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));
        actionBar.setDisplayHomeAsUpEnabled(true);
        setUpMapIfNeeded();
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
                Intent goHome = new Intent(this, Home.class);
                startActivity(goHome);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mMap != null)
                setUpMap();
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
            Parser parser = new Parser();
            parser.calcBuses();
            parser.calcRoutes();
            parser.assignRoutes();
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
                                .icon(BitmapDescriptorFactory.defaultMarker((int)((Integer.parseInt(buses.get(i).getBusRoute().getColor(), 16)/16777215.0)*360)))
                                .visible(buses.get(i).getBusRoute().getActive()));
            //if (condition)
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(buses.get(0).getLat(), buses.get(0).getLon()), 15));
        }
    }
}
