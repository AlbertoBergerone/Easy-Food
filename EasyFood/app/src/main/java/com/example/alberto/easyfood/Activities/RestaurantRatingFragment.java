package com.example.alberto.easyfood.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.alberto.easyfood.R;
import com.example.alberto.easyfood.RestaurantModule.Restaurant;

/**
 * Created by Alberto on 02/07/2016.
 */
public class RestaurantRatingFragment extends Fragment {
    private View myView;
    private Restaurant restaurant;
    private ListView myListView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_restaurant_rating, container, false);

        restaurant = ((RestaurantActivity) getActivity()).getRestaurant();

        return myView;
    }
}
