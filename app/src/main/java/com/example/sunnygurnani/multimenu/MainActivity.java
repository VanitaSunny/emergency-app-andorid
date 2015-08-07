package com.example.sunnygurnani.multimenu;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();

        switch (position)
        {
            //Home
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, Home.newInstance(position))
                        .commit();
                break;
            //Map
            case 1:
                Context context= this;
                Intent intent=new Intent(context,Geolocation.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            //Flash
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, FlashLight.newInstance(position ))
                        .commit();
                break;
            //Developers
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, DevelopersProfile.newInstance(position ))
                        .commit();
                break;
            default:


        }

        /*
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
                */
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_home);
                break;
            case 1:
                mTitle = getString(R.string.title_map);

                break;
            case 2:
                mTitle = getString(R.string.title_flash);
                break;
            case 3:
                mTitle = getString(R.string.title_developer_profile);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = null;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            intent = new Intent(this, EmergencyContactActivity.class);
        }
        else if (id == R.id.action_about_us){
            intent = new Intent(this, AboutUs.class);

        }
        if(intent != null) {
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentAttach(int sectionNumber) {
        onSectionAttached(sectionNumber);
    }



}
