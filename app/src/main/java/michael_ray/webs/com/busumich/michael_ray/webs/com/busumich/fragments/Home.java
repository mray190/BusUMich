package michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.fragments;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import java.util.ArrayList;

import michael_ray.webs.com.busumich.R;
import michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.adapters.TabsAdapter;
import michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.logic.BusStop;
import michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.logic.Parser;

/**
 * Home.java
 * Home screen that manages the fragments and general activity
 * @author Michael Ray
 * @version 1
 * @since 10-30-14
 */
public class Home extends FragmentActivity implements ActionBar.TabListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private ViewPager mPager;
    private TabsAdapter mPagerAdapter;
    private ActionBar actionBar;
    private LocationClient mLocationClient;
    private Location myLocation;
    private FragmentManager fm;

    /**
	 * Manages the navigation, fragments and panels for swipe tabs and action bar menus
	 */
    private void managePageNavigation() {
        mPager = (ViewPager) findViewById(R.id.pager);
        //Sets an action bar with tabs
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setTitle(getResources().getString(R.string.app_name));
        actionBar.setDisplayHomeAsUpEnabled(false);

        fm = getSupportFragmentManager();

        //Creates a Pager view and sets a listener and adapter to monitor swipes
        mPagerAdapter = new TabsAdapter(fm);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(5);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });

        //Create the tabs and fragments
        ActionBar.Tab tab1 = actionBar.newTab().setText(getResources().getString(R.string.tab1));
        actionBar.addTab(tab1.setTabListener(this));
        ActionBar.Tab tab2 = actionBar.newTab().setText(getResources().getString(R.string.tab2));
        actionBar.addTab(tab2.setTabListener(this));
        ActionBar.Tab tab3 = actionBar.newTab().setText(getResources().getString(R.string.tab3));
        actionBar.addTab(tab3.setTabListener(this));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Menu methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Create the menu in the action bar for the interface
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_map:
                Intent intent = new Intent(this, Map.class);
                startActivity(intent);
                return true;
            case R.id.action_refresh:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Tab control methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        mPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) { }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) { }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Google Play Location services methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onConnected(Bundle bundle) {
        myLocation = mLocationClient.getLastLocation();
        Refresh refresh = new Refresh();
        refresh.execute();
    }

    @Override
    public void onDisconnected() { }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) { }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //App lifecycle methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mLocationClient = new LocationClient(this, this, this);
        managePageNavigation();
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
    public void onBackPressed() {
        if (mPager.getCurrentItem()==0) {
            if (((DisplayFragment) mPagerAdapter.getRegisteredFragment(0)).getLayer()==2) {
                Refresh refresh = new Refresh();
                refresh.execute();
            } else
                super.onBackPressed();
        } else
            super.onBackPressed();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Async Task methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    class Refresh extends AsyncTask<Void, Void, ArrayList<BusStop>> {
        @Override
        protected ArrayList<BusStop> doInBackground(Void...params) {
            Parser parser = new Parser();
            return parser.getClosestStops(myLocation);
        }

        @Override
        protected void onPostExecute(ArrayList<BusStop> stops) {
            ((DisplayFragment) mPagerAdapter.getRegisteredFragment(0)).setBusStopData(stops);
        }
    }

}
