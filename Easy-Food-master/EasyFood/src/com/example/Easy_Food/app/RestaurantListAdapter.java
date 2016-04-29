package com.example.Easy_Food.app;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Alberto on 12/04/2016.
 */
public class RestaurantListAdapter extends ArrayAdapter<Restaurant> {

    public RestaurantListAdapter(Context context, int list_item_layout_id, List<Restaurant> restaurants) {
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
        viewHolder.restaurantLocation.setText(restaurant.get_municipality() + ", " + restaurant.get_address() + "(" + restaurant.get_provinceID() + ")");
        viewHolder.restaurantRating.setText(Double.toString(restaurant.get_rating()));

        return convertView;
    }
}
