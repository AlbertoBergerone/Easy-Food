package com.example.alberto.easyfood.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.alberto.easyfood.R;
import com.example.alberto.easyfood.UserModule.CurrentUser;

/**
 * Created by Alberto on 12/06/2016.
 */
public class AccountFragment extends Fragment {
    private View myView;
    private TextView txtUsername;
    private TextView txtName;
    private TextView txtLastName;
    private TextView txtEmail;
    private TextView txtPhone;
    private TextView txtResidence;
    private TextView txtAddress;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* Inflating the fragment_account */
        myView = inflater.inflate(R.layout.fragment_account, container, false);

        txtName = (TextView) myView.findViewById(R.id.account_user_name);
        txtLastName = (TextView) myView.findViewById(R.id.account_user_last_name);
        txtUsername = (TextView) myView.findViewById(R.id.account_user_username);
        txtEmail = (TextView) myView.findViewById(R.id.account_user_email);
        txtPhone = (TextView) myView.findViewById(R.id.account_user_phone_number);
        txtResidence = (TextView) myView.findViewById(R.id.account_user_residence);
        txtAddress = (TextView) myView.findViewById(R.id.account_user_address);

        /* Setting the toolbar */
        toolbar = (Toolbar) myView.findViewById(R.id.default_toolbar);
        ((HomeActivity)AccountFragment.this.getActivity()).setSupportActionBar(toolbar);
        /* Hiding the title of the toolbar */
        ((HomeActivity)AccountFragment.this.getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        /* Displaying the hamburger icon */
        ((HomeActivity)AccountFragment.this.getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((HomeActivity)AccountFragment.this.getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        setHasOptionsMenu(true);

        /* Displaying user information */
        displayUserInformation();

        return myView;
    }


    /**
     * Displaying user information in TextViews
     */
    private void displayUserInformation(){
        txtUsername.setText(CurrentUser.get_currentUserProfile().get_username());
        txtLastName.setText(CurrentUser.get_currentUserProfile().get_last_name());
        txtName.setText(CurrentUser.get_currentUserProfile().get_name());
        txtEmail.setText(CurrentUser.get_currentUserProfile().get_email());

        if(CurrentUser.get_currentUserProfile().get_phone() != null && !CurrentUser.get_currentUserProfile().get_phone().isEmpty() &&  !CurrentUser.get_currentUserProfile().get_phone().equalsIgnoreCase("null"))
            txtPhone.setText(CurrentUser.get_currentUserProfile().get_phone());
        else
            txtPhone.setText("");

        if(CurrentUser.get_currentUserProfile().get_residence() != null && !CurrentUser.get_currentUserProfile().get_residence().isEmpty() && !CurrentUser.get_currentUserProfile().get_residence().equalsIgnoreCase("null") && CurrentUser.get_currentUserProfile().get_province() != null && !CurrentUser.get_currentUserProfile().get_province().isEmpty() && !CurrentUser.get_currentUserProfile().get_province().equalsIgnoreCase("null"))
            txtResidence.setText(CurrentUser.get_currentUserProfile().get_residence() + " (" + CurrentUser.get_currentUserProfile().get_province() + ")");
        else
            txtResidence.setText("");

        if(CurrentUser.get_currentUserProfile().get_address() != null && !CurrentUser.get_currentUserProfile().get_address().isEmpty() && !CurrentUser.get_currentUserProfile().get_address().equalsIgnoreCase("null"))
            txtAddress.setText(CurrentUser.get_currentUserProfile().get_address());
        else
            txtAddress.setText("");
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.account_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_edit_account){
            /* Opening the activity where the user is able to modify his profile */
            Intent editUserIntent = new Intent(AccountFragment.this.getActivity(), EditUserActivity.class);
            AccountFragment.this.getActivity().startActivity(editUserIntent);
        }else if(item.getItemId() == R.id.menu_edit_password){
            /* Opening the activity where the user is able to modify his password */
            Intent editPasswordIntent = new Intent(AccountFragment.this.getActivity(), EditPasswordActivity.class);
            AccountFragment.this.getActivity().startActivity(editPasswordIntent);
        }else if(item.getItemId() == R.id.menu_log_out_account){
            /* If the user clicked on logOut item it cleans all user information and it returns to the login activity */
            CurrentUser.set_currentUserProfile(null);
            /* Setting flags to clean up all activities, so the user can't return in the home activity clicking back button */
            Intent logOutIntent = new Intent(AccountFragment.this.getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            AccountFragment.this.getActivity().startActivity(logOutIntent);
            AccountFragment.this.getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
