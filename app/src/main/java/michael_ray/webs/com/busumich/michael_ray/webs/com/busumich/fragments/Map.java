package michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.fragments;

import android.app.ActionBar;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

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
                setUpMap(false);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mMap != null)
                setUpMap(true);
        }
    }

    private void setUpMap(boolean cond) {
        Test tester = new Test(this, cond);
        tester.execute();
    }

    class Test extends AsyncTask<Void, Void, ArrayList<Bus>> {
        private Context context;
        private boolean condition;
        public Test(Context context, boolean cond) {
            condition = cond;
            this.context = context;
        }
        @Override
        protected ArrayList<Bus> doInBackground(Void...params) {
            Parser parser = new Parser();
            return parser.getBuses();
        }

        @Override
        protected void onPostExecute(ArrayList<Bus> buses) {
            mMap.clear();
            for (int i = 0; i < buses.size(); i++) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(buses.get(i).getLat(), buses.get(i).getLon())).title(buses.get(i).getName()));
            }
            if (condition)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(buses.get(0).getLat(), buses.get(0).getLon()), 15));
        }
    }
}
