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


        toolbar = (Toolbar) myView.findViewById(R.id.default_toolbar);
        ((HomeActivity)AccountFragment.this.getActivity()).setSupportActionBar(toolbar);

        ((HomeActivity)AccountFragment.this.getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((HomeActivity)AccountFragment.this.getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtName = (TextView) myView.findViewById(R.id.account_user_name);
        txtLastName = (TextView) myView.findViewById(R.id.account_user_last_name);
        txtUsername = (TextView) myView.findViewById(R.id.account_user_username);
        txtEmail = (TextView) myView.findViewById(R.id.account_user_email);
        txtPhone = (TextView) myView.findViewById(R.id.account_user_phone_number);
        txtResidence = (TextView) myView.findViewById(R.id.account_user_residence);
        txtAddress = (TextView) myView.findViewById(R.id.account_user_address);

        displayUserInformation();
        setHasOptionsMenu(true);

        return myView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    private void displayUserInformation(){
        txtUsername.setText(CurrentUser.get_currentUserProfile().get_username());
        txtLastName.setText(CurrentUser.get_currentUserProfile().get_last_name());
        txtName.setText(CurrentUser.get_currentUserProfile().get_name());
        txtEmail.setText(CurrentUser.get_currentUserProfile().get_email());

        if(CurrentUser.get_currentUserProfile().get_phone() != null && !CurrentUser.get_currentUserProfile().get_phone().isEmpty())
            txtPhone.setText(CurrentUser.get_currentUserProfile().get_phone());
        else
            txtPhone.setText("");

        if(CurrentUser.get_currentUserProfile().get_residence() != null && !CurrentUser.get_currentUserProfile().get_residence().isEmpty())
            txtResidence.setText(CurrentUser.get_currentUserProfile().get_residence() + " (" + CurrentUser.get_currentUserProfile().get_province() + ")");
        else
            txtResidence.setText("");

        if(CurrentUser.get_currentUserProfile().get_address() != null && !CurrentUser.get_currentUserProfile().get_address().isEmpty())
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
        if(item.getItemId() == R.id.menu_log_out_account){
            CurrentUser.set_currentUserProfile(null);
            /* Setting flags to clean up all activities */
            Intent logOutIntent = new Intent(AccountFragment.this.getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            AccountFragment.this.getActivity().startActivity(logOutIntent);
            AccountFragment.this.getActivity().finish();
        }else if(item.getItemId() == R.id.menu_edit_account){
            Intent editUserIntent = new Intent(AccountFragment.this.getActivity(), EditUserActivity.class);
            AccountFragment.this.getActivity().startActivity(editUserIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
