package socala.app.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import socala.app.R;
import socala.app.fragments.CalendarFragment;
import socala.app.fragments.CommonTimeFinderFragment;
import socala.app.fragments.FriendListFragment;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.drawer_layout) DrawerLayout drawer;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.nav_view) NavigationView navView;

    private ActionBarDrawerToggle drawerToggle;
    private Map<String, Fragment> fragments;
    private Fragment currFragment;

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public void selectDrawerItem(MenuItem item) {
        Fragment fragment;

        switch (item.getItemId()) {
            case R.id.nav_calendar_fragment:
                fragment = fragments.get("calendar");
                break;
            case R.id.nav_common_time_fragment:
                fragment = fragments.get("commonTime");
                break;
            case R.id.nav_friend_list_fragment:
                fragment = fragments.get("friendList");
                break;
            default:
                fragment = fragments.get("calendar");
        }

        FragmentManager fragmentManager = getSupportFragmentManager();


        if (currFragment != null) {
            fragmentManager.beginTransaction().hide(currFragment).commit();
        }


        fragmentManager.beginTransaction().show(fragment).commit();
        currFragment = fragment;

        item.setChecked(true);

        setTitle(item.getTitle());

        drawer.closeDrawers();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        outState.putStringArrayList("tags", new ArrayList<>(fragments.keySet()));


        for (Map.Entry<String, Fragment> entry : fragments.entrySet()) {
            fragmentManager.putFragment(outState, entry.getKey(), entry.getValue());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        drawerToggle = setupDrawerToggle();
        drawer.addDrawerListener(drawerToggle);

        initializeFragments(savedInstanceState);

        setupDrawerContent(navView);

        selectDrawerItem(navView.getMenu().findItem(R.id.nav_calendar_fragment));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private void setupDrawerContent(NavigationView navView) {
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                }
        );
    }

    private void initializeFragments(Bundle savedInstanceState) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Extract fragments from bundle
        if (savedInstanceState != null) {
            ArrayList<String> tags = savedInstanceState.getStringArrayList("tags");

            for (String tag : tags) {
                fragments.put(tag, fragmentManager.getFragment(savedInstanceState, tag));
            }
        } else {

            fragments = new HashMap<>();

            fragments.put("commonTime", new CommonTimeFinderFragment());
            fragments.put("friendList", new FriendListFragment());
            fragments.put("calendar", new CalendarFragment());
        }

        for (Map.Entry<String, Fragment> entry :fragments.entrySet()) {
            fragmentManager.beginTransaction().add(R.id.frameContent, entry.getValue(), entry.getKey()).hide(entry.getValue()).commit();
        }

        fragmentManager.beginTransaction().show(fragments.get("calendar")).commit();
        currFragment = fragments.get("calendar");
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

}
