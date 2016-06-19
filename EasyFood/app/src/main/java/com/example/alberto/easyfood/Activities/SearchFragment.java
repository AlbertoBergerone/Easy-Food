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
import com.example.alberto.easyfood.RestaurantModule.RestaurantManager;
import com.example.alberto.easyfood.ServerCommunicationModule.InternetConnection;

import java.util.ArrayList;

/**
 * Created by Alberto on 30/05/2016.
 */
public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    private static final String RESTAURANT_EXTRA_NAME = "restaurant";
    private Toolbar toolbar;
    private View myView;
    private ArrayList<Restaurant> myRestaurantList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_search, container, false);

        /* Setting the toolbar */
        toolbar = (Toolbar) myView.findViewById(R.id.default_toolbar);
        ((HomeActivity)SearchFragment.this.getActivity()).setSupportActionBar(toolbar);
        /* Hiding the title of the toolbar */
        ((HomeActivity)SearchFragment.this.getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        /* Displaying the hamburger icon */
        ((HomeActivity)SearchFragment.this.getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((HomeActivity)SearchFragment.this.getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        setHasOptionsMenu(true);

        Intent searchIntent = SearchFragment.this.getActivity().getIntent();
        if(Intent.ACTION_SEARCH.equals(searchIntent.getAction())){
            String queryString = searchIntent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(SearchFragment.this.getActivity(), queryString, Toast.LENGTH_LONG).show();
            RestaurantManager restaurantManager = new RestaurantManager();
            if(InternetConnection.haveIInternetConnection(SearchFragment.this.getActivity())){

                myRestaurantList = restaurantManager.getRestaurantsForListView(queryString);
                if(myRestaurantList != null){
                    /* Creating a dictionary to simplify the GUI/Adapter association */
                    BindDictionary<Restaurant> restaurantBindDictionary = new BindDictionary<>();
                    restaurantBindDictionary.addStringField(R.id.txt_list_restaurant_name, new StringExtractor<Restaurant>() {
                        @Override
                        public String getStringValue(Restaurant restaurant, int position) {
                            return restaurant.get_restaurantName();
                        }
                    });
                    restaurantBindDictionary.addStringField(R.id.txt_list_location, new StringExtractor<Restaurant>() {
                        @Override
                        public String getStringValue(Restaurant restaurant, int position) {
                            return restaurant.get_full_address();
                        }
                    });
                    restaurantBindDictionary.addStringField(R.id.txt_list_rating, new StringExtractor<Restaurant>() {
                        @Override
                        public String getStringValue(Restaurant restaurant, int position) {
                            return String.valueOf(restaurant.get_ratingToString());
                        }
                    });

                    FunDapter funAdapter = new FunDapter(SearchFragment.this.getActivity(), myRestaurantList, R.layout.list_item_search, restaurantBindDictionary);

                    ListView listViewRestaurants = (ListView)myView.findViewById(R.id.searchResult_listView);
                    listViewRestaurants.setAdapter(funAdapter);

                    listViewRestaurants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        /* This is the selected restaurant */
                            Restaurant selectedRestaurant = myRestaurantList.get(position);
                        /* Intent to a new activity that displays all the restaurant information */
                            Intent restaurantInfoIntent = new Intent(SearchFragment.this.getActivity(), RestaurantDetailsActivity.class);
                            restaurantInfoIntent.putExtra(RESTAURANT_EXTRA_NAME, selectedRestaurant);
                            SearchFragment.this.getActivity().startActivity(restaurantInfoIntent);
                        }
                    });
                }
            }else{
                Toast.makeText(SearchFragment.this.getActivity(), R.string.No_internet_connection, Toast.LENGTH_LONG).show();
            }
        }

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

}