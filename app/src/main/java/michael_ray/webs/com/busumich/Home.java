package michael_ray.webs.com.busumich;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Home extends FragmentActivity implements ActionBar.TabListener {

    private ViewPager mPager;
    private TabsAdapter mPagerAdapter;
    private ActionBar actionBar;

    /*
	 * Manages the navigation, fragments and panels for swipey tabs and action bar menus
	 * PreCondtion: Title has been determined and a view exists to put the content
	 * @param Title a string representing the title of the interface
	 * @return void
	 */
    private void managePageNavigation() {
        mPager = (ViewPager) findViewById(R.id.pager);
        //Sets an action bar with tabs
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setTitle(getResources().getString(R.string.app_name));
        actionBar.setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        managePageNavigation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Create the menu in the action bar for the interface
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        mPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

    }
}
