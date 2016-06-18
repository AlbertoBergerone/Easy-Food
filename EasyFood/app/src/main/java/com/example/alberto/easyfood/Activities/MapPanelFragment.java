package com.example.alberto.easyfood.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alberto.easyfood.GeolocationModule.GeolocationManager;
import com.example.alberto.easyfood.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Alberto on 30/05/2016.
 */
public class MapPanelFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private Toolbar toolbar;
    private View myView;
    private GeolocationManager myLocationManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initializingMap(savedInstanceState);
        myView = inflater.inflate(R.layout.fragment_map_panel, container, false);

        myLocationManager = new GeolocationManager(MapPanelFragment.this.getActivity());

        /* Setting the toolbar */
        toolbar = (Toolbar) myView.findViewById(R.id.default_toolbar);
        ((HomeActivity)MapPanelFragment.this.getActivity()).setSupportActionBar(toolbar);
        /* Hiding the title of the toolbar */
        ((HomeActivity)MapPanelFragment.this.getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        /* Displaying the hamburger icon */
        ((HomeActivity)MapPanelFragment.this.getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((HomeActivity)MapPanelFragment.this.getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        setHasOptionsMenu(true);
        return myView;
    }

    private void initializingMap(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getChildFragmentManager();
        if (mapFragment == null) {
            mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.map_container, mapFragment, "mapFragment");
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.default_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(!myLocationManager.isGPSEnabled()){
            Toast.makeText(MapPanelFragment.this.getActivity(), R.string.gps_not_enabled, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            if (ActivityCompat.checkSelfPermission(MapPanelFragment.this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapPanelFragment.this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
        }
    }
}
