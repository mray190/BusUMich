package michael_ray.webs.com.busumich.michael_ray.webs.com.busumich.fragments;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

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
    private DrawerLayout mDrawer;
    private TabsAdapter mPagerAdapter;
    private ActionBar actionBar;
    private LocationClient mLocationClient;
    private ActionBarDrawerToggle mDrawerToggle;
    private Location myLocation;
    private MenuItem searchItem;
    public static FragmentManager fm;
    private static final int NEAR = 0, FAVORITE = 1, MAP = 2;

    /**
	 * Manages the navigation, fragments and panels for swipe tabs and action bar menus
	 */
    private void managePageNavigation() {
        mPager = (ViewPager) findViewById(R.id.pager);
        //Sets an action bar with tabs
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setTitle(getResources().getString(R.string.app_name));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ListView mDrawerList = (ListView)findViewById(R.id.left_drawer);
        String[] drawer_items = getResources().getStringArray(R.array.drawer_items);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, drawer_items));
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawer,R.drawable.ic_navi_bar,R.string.drawer_open,R.string.drawer_close);
        mDrawer.setDrawerListener(mDrawerToggle);

        fm = getSupportFragmentManager();

        //Creates a Pager view and sets a listener and adapter to monitor swipes
        mPagerAdapter = new TabsAdapter(fm);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(3);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                invalidateOptionsMenu();
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
    //App lifecycle methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        mLocationClient = new LocationClient(this, this, this);
        managePageNavigation();
        LoadFavorites task = new LoadFavorites();
        task.execute();
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
        if (((DisplayFragment) mPagerAdapter.getRegisteredFragment(mPager.getCurrentItem())).getLayer()==2) {
            if (mPager.getCurrentItem() == NEAR) {
                LoadNear task = new LoadNear();
                task.execute();
            } else if (mPager.getCurrentItem() == FAVORITE) {
                LoadFavorites task = new LoadFavorites();
                task.execute();
            }
        } else
            super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Menu methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Create the menu in the action bar for the interface
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);

        searchItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)searchItem.getActionView();
        if (null != searchView ) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {

                return true;
            }

            public boolean onQueryTextSubmit(String query) {

                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_refresh:
                myLocation = mLocationClient.getLastLocation();
                LoadNear task = new LoadNear();
                task.execute();
                LoadFavorites task2 = new LoadFavorites();
                task2.execute();
                return true;
            case R.id.search:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private TextWatcher filterTextWatcher = new TextWatcher()
    {
        public void afterTextChanged(Editable s)
        {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            // your search logic here
        }
    };

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
        LoadNear task = new LoadNear();
        task.execute();
    }

    @Override
    public void onDisconnected() { }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) { }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Async Task methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    class LoadNear extends AsyncTask<Void, Void, ArrayList<BusStop>> {
        @Override
        protected ArrayList<BusStop> doInBackground(Void...params) {
            Parser parser = new Parser(getApplicationContext());
            return parser.getClosestStops(myLocation);
        }

        @Override
        protected void onPostExecute(ArrayList<BusStop> stops) {
            ((DisplayFragment) mPagerAdapter.getRegisteredFragment(NEAR)).setBusStopData(stops);
        }
    }

    class LoadFavorites extends AsyncTask<Integer, Void, ArrayList<BusStop>> {
        @Override
        protected ArrayList<BusStop> doInBackground(Integer...params) {
            Parser parser = new Parser(getApplicationContext());
            return parser.getFavorites();
        }

        @Override
        protected void onPostExecute(ArrayList<BusStop> stops) {
            ((DisplayFragment) mPagerAdapter.getRegisteredFragment(FAVORITE)).setBusStopData(stops);
        }
    }

}
