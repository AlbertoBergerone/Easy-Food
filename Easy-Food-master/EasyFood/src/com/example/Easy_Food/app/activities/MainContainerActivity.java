package com.example.Easy_Food.app.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;
import com.example.Easy_Food.app.R;



/**
 * Created by Alberto on 12/04/2016.
 */
public class MainContainerActivity extends AppCompatActivity {
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_container);
        if (savedInstanceState == null) {
            // Let's first dynamically add a fragment into a frame container
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            // Replace the contents of the container with the new fragment
            fragmentTransaction.replace(R.id.fragment_placeholder, (fragment = new SearchFragment()));
            // Complete the changes
            fragmentTransaction.commit();
        }

    }

}
