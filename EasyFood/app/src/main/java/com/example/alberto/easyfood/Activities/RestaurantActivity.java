package com.example.alberto.easyfood.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.example.alberto.easyfood.R;
import com.example.alberto.easyfood.RestaurantModule.Restaurant;
import com.example.alberto.easyfood.RestaurantModule.RestaurantManager;
import com.example.alberto.easyfood.ServerCommunicationModule.InternetConnection;

public class RestaurantActivity extends AppCompatActivity {
    private static final String RESTAURANT_EXTRA_NAME = "restaurant";
    private SectionsPagerAdapter mySectionsPagerAdapter;
    private ViewPager myViewPager;
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        Intent myIntent = getIntent();
        restaurant = (Restaurant) myIntent.getSerializableExtra(RESTAURANT_EXTRA_NAME);

        /* Setting the toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        this.setSupportActionBar(toolbar);
        /* Hiding the title of the toolbar */
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        /* Displaying the back arrow icon */
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);


        if(InternetConnection.haveIInternetConnection(this)){
            RestaurantManager restaurantManager = new RestaurantManager();
            Restaurant tmp = restaurantManager.getRestaurant(restaurant.get_restaurantID());
            if(tmp != null)
                restaurant = tmp;
            else
                Toast.makeText(RestaurantActivity.this, R.string.server_unreachable, Toast.LENGTH_LONG).show();
        }else
            Toast.makeText(this, R.string.No_internet_connection, Toast.LENGTH_LONG).show();


        /* Create the adapter that will return a fragment for each section of the activity. */
        mySectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        /* Set up the ViewPager with the sections adapter. */
        myViewPager = (ViewPager) findViewById(R.id._restaurant_fragment_container);
        myViewPager.setAdapter(mySectionsPagerAdapter);

        /* Setting the tabbed bar */
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_bar);
        tabLayout.setupWithViewPager(myViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurant, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Handling action bar item clicks */
        if (item.getItemId() == R.id.rate_icon){
            Toast.makeText(this, "To be continue...", Toast.LENGTH_SHORT).show();
            return true;
        }else if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    /* Method that returns the Restaurant property */
    public Restaurant getRestaurant() {
        return restaurant;
    }

    /**
     * Method that returns a fragment corresponding to one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new RestaurantDetailsFragment();
                default:
                    return new RestaurantRatingFragment();
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.Details);
                case 1:
                    return getResources().getString(R.string.Rating);
            }
            return null;
        }
    }
}
