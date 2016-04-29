package com.example.Easy_Food.app.activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.Easy_Food.app.R;
import com.example.Easy_Food.app.Restaurant;
import com.example.Easy_Food.app.RestaurantListAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alberto on 13/04/2016.
 * Java code that manages the SearchFragment
 *
 * http://stackoverflow.com/questions/4249897/how-to-send-objects-through-bundle
 */
public class SearchFragment extends Fragment {
    private FragmentActivity _listener;
    private RestaurantListAdapter _adapter;
    private ListView _listView_search_reults;

    /* This event fires 1st, before creation of fragment or any views
     * The onAttach method is called when the Fragment instance is associated with an Activity.
     * This does not mean the Activity is fully initialized.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this._listener = (FragmentActivity) context;
        }
    }
    /* This event fires 2nd, before views are created for the fragment
     * The onCreate method is called when the Fragment instance is being created, or re-created.
     * Standard setups that does not require the activity to be fully created
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /* Called when Fragment should create its View object hierarchy, either dynamically or via XML layout inflation. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, parent, false);
    }

    /* This event is triggered soon after onCreateView().
     * onViewCreated() is only called if the view returned from onCreateView() is non-null.
     * Any view setup should occur here.  E.g., view lookups and attaching view listeners.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        _listView_search_reults = (ListView) _listener.findViewById(R.id.list_search_results);
        List list = new LinkedList();
        /* examples of data */
        list.add(new Restaurant("Kale's Bar", "Margarita", "Via Roma", "10", "Cuneo", "CN", 3.4));
        list.add(new Restaurant("Jocasta", "Peveragno", "Via degli Artigiani", "30", "Cuneo", "CN", 3.9));
        list.add(new Restaurant("800 Bar Caffetteria", "Cuneo", "Via Roma", "53", "Cuneo", "CN", 3.9));
        list.add(new Restaurant("Galì food & drink", "Cuneo", "Piazza Tancredi Galimberti", "6", "Cuneo", "CN", 3.0));
        list.add(new Restaurant("Black Bull", "San Giovenale", "Via Funga di San Giovenale", "16", "Cuneo", "CN", 3.4));
        list.add(new Restaurant("McDonald's", "Cuneo", "Centro Commerciale Auchan, Via Margarita", "8", "Cuneo", "CN", 3.4));
        /* Setting the adapter to the listView */
        _listView_search_reults.setAdapter((_adapter = new RestaurantListAdapter(this.getActivity(), R.layout.list_item_search, list)));

        _listView_search_reults.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Restaurant selectedItem = (Restaurant) _adapter.getItemAtPosition(position);

                Intent intent = new Intent(_listener.getContext(),RestaurantActivity.class);
                /* adding a Restaurant object to the intent extras */
                intent.putExtras("selectedRestaurant", new Gson().toJson(selectedItem));
                /* starting the activity bounded */
                _listener.getContext().startService(intent);

            }


        });
    }

    /* This method is called after the parent Activity's onCreate() method has completed.
     * Accessing the view hierarchy of the parent activity must be done here.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }




}
