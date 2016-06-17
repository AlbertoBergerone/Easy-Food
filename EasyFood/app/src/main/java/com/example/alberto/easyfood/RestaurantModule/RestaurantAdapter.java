package com.example.alberto.easyfood.RestaurantModule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alberto.easyfood.R;

import java.util.ArrayList;

/**
 * Created by Alberto on 12/04/2016.
 */
public class RestaurantAdapter extends ArrayAdapter<Restaurant> {

    public RestaurantAdapter(Context context, int list_item_layout_id, ArrayList<Restaurant> restaurants) {
        super(context, list_item_layout_id, restaurants);
    }

    /** Class that allows to hold the reference with textViews */
    private class ViewHolder {
        public TextView restaurantName;
        public TextView restaurantLocation;
        public TextView restaurantRating;
    }

    /* Loading the listView */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            /* If the listView is empty */
            /* Inflating the listView */
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_search, null);
            viewHolder = new ViewHolder();
            viewHolder.restaurantName = (TextView)convertView.findViewById(R.id.txt_list_restaurant_name);
            viewHolder.restaurantLocation = (TextView)convertView.findViewById(R.id.txt_list_location);
            viewHolder.restaurantRating = (TextView)convertView.findViewById(R.id.txt_list_rating);
            /* The View class allows us - thanks to the setTag() method - to associate an object to his class instance. */
            convertView.setTag(viewHolder);
        }else{
            /* If the listView is not empty */
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Restaurant restaurant = getItem(position);
        viewHolder.restaurantName.setText(restaurant.get_restaurantName());
        viewHolder.restaurantLocation.setText(restaurant.get_full_address());
        viewHolder.restaurantRating.setText(Double.toString(restaurant.get_rating()));

        return convertView;
    }
}
