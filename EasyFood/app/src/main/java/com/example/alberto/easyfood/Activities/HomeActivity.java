package com.example.alberto.easyfood.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.alberto.easyfood.R;

public class HomeActivity extends AppCompatActivity {
    private Toolbar toolbar = null;
    private DrawerLayout drawer = null;
    private NavigationView navigationView = null;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /* Set the fragment initially */
        setFragment(new SearchFragment());

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        setupNavDrawer();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                displayFragment(item);
                setupNavDrawer();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        Intent searchIntent = getIntent();
        if(Intent.ACTION_SEARCH.equals(searchIntent.getAction())){
            String query = searchIntent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, query, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* It should open the drawer */
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            drawer.openDrawer(GravityCompat.START);
            return true;
        }
        /* Else it will return false so the drawer won't been opened */
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        /* Sync the toggle state after onRestoreInstanceState occurred */
        actionBarDrawerToggle.syncState();
    }

    private void displayFragment(MenuItem item){
        Fragment new_fragment = null;
        View new_toolbar = null;
        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (item.getItemId()){
            case R.id.nav_search:
                new_fragment = new SearchFragment();

                break;
            case R.id.nav_map:
                new_fragment = new MapPanelFragment();

                break;
            case R.id.nav_account:
                new_fragment = new AccountFragment();

                break;
        }
        if(new_fragment != null)
            /* Setting the fragment */
            setFragment(new_fragment);

    }


    /* Inflating the fragment */
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(fragment != null){
            fragmentTransaction.replace(R.id.fragment_container, fragment);
        }else{
            fragmentTransaction.replace(R.id.fragment_container, null);
        }
        fragmentTransaction.commit();
    }

    private void setupNavDrawer(){
        //View drawerHeader = findViewById(R.id.nav_header);
        //TextView nav_username = (TextView)drawerHeader.findViewById(R.id.txt_nav_username);
        //if(nav_username != null)
            //nav_username.setText(CurrentUser.get_currentUserProfile().get_username());

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
    }
}
