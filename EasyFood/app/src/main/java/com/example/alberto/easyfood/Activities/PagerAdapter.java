package com.example.alberto.easyfood.Activities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.alberto.easyfood.R;

/**
 * Created by Alberto on 30/05/2016.
 * Class which manage more fragment pages. It can also store fragment state so it will restore it.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int tabsCount;
    Context context;

    /**
     * Constructor that initializes properties
     * @param fragmentManager (FragmentManager) FragmentManager
     * @param tabsCount (int) count of tabs
     * @param context (Context) ApplicationContext
     */
    public PagerAdapter(FragmentManager fragmentManager, int tabsCount, Context context){
        super(fragmentManager);
        this.tabsCount = tabsCount;
        this.context = context;
    }

    /**
     * Get the number of tabs
     * @return the number of tabs
     */
    @Override
    public int getCount() {
        return tabsCount;
    }

    /**
     * Method that returns a new fragment dependent on which is the current one selected
     * @param position position that is the current tab id
     * @return a Fragment instance
     */
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new SearchFragment();
            case 1:
                return new MapPanelFragment();
            default:
                return null;
        }
    }

    /**
     * Method that returns the title of the current fragment
     * @param position current position
     * @return (CharSequence) title of the current tab
     */
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.Search);
            case 1:
                return context.getResources().getString(R.string.Account);
            default:
                return null;
        }
    }
}