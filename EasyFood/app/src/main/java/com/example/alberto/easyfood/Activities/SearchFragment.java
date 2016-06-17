package com.example.alberto.easyfood.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.example.alberto.easyfood.R;
import com.example.alberto.easyfood.RestaurantModule.Restaurant;

import java.util.ArrayList;

/**
 * Created by Alberto on 30/05/2016.
 */
public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    private EditText search_text;
    private ImageView clear_search_text;
    private Toolbar toolbar;
    private View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_search, container, false);


        toolbar = (Toolbar) myView.findViewById(R.id.default_toolbar);
        ((HomeActivity)SearchFragment.this.getActivity()).setSupportActionBar(toolbar);
        ((HomeActivity)SearchFragment.this.getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((HomeActivity)SearchFragment.this.getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);


        /* Da spostare dove dovrei effettuare la ricerca */
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//        BindDictionary<Restaurant> restaurantBindDictionary = new BindDictionary<>();
//        restaurantBindDictionary.addStringField(R.id.txt_list_restaurant_name, new StringExtractor<Restaurant>() {
//            @Override
//            public String getStringValue(Restaurant restaurant, int position) {
//                return restaurant.get_restaurantName();
//            }
//        });
//        restaurantBindDictionary.addStringField(R.id.txt_list_location, new StringExtractor<Restaurant>() {
//            @Override
//            public String getStringValue(Restaurant restaurant, int position) {
//                return restaurant.get_full_address();
//            }
//        });
//        restaurantBindDictionary.addStringField(R.id.txt_list_rating, new StringExtractor<Restaurant>() {
//            @Override
//            public String getStringValue(Restaurant restaurant, int position) {
//                return String.valueOf(restaurant.get_rating());
//            }
//        });
//
//        /* Here I should get an array of restaurants */
//        final ArrayList<Restaurant> restaurants = new ArrayList<>();
//        FunDapter funAdapter = new FunDapter(SearchFragment.this.getActivity(), restaurants, R.layout.list_item_search, restaurantBindDictionary);
//
//        ListView listViewRestaurants = (ListView)myView.findViewById(R.id.searchResult_listView);
//        listViewRestaurants.setAdapter(funAdapter);
//
//        listViewRestaurants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                /* This is the selected restaurant */
//                Restaurant selectedRestaurant = restaurants.get(position);
//                /* Intent to a new activity that displays all the restaurant information */
//                Intent restaurantInfoIntent = new Intent(SearchFragment.this.getActivity(), RestaurantDetailsActivity.class);
//                restaurantInfoIntent.putExtra("Restaurant", selectedRestaurant);
//                SearchFragment.this.getActivity().startActivity(restaurantInfoIntent);
//            }
//        });
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        return myView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search_icon).getActionView();
        SearchManager searchManager = (SearchManager) SearchFragment.this.getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(SearchFragment.this.getActivity().getComponentName()));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivity)getActivity()).setSupportActionBar(toolbar);
    }
    

}