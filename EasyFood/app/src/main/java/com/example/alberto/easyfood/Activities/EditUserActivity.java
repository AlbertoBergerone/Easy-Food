package com.example.alberto.easyfood.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.example.alberto.easyfood.GeolocationModule.Residence;
import com.example.alberto.easyfood.ServerCommunicationModule.InternetConnection;
import com.example.alberto.easyfood.R;
import com.example.alberto.easyfood.UserModule.CurrentUser;
import com.example.alberto.easyfood.UserModule.User;
import com.example.alberto.easyfood.UserModule.UserManager;

import java.util.ArrayList;

/**
 * Created by inf.bergeronea1610 on 06/06/2016.
 * EditUserActivity
 * This is the activity that is used to change user information
 */
public class EditUserActivity extends AppCompatActivity {
    private UserManager userManager;
    private EditText txtName;
    private EditText txtLastName;
    private AutoCompleteTextView txtResidence;
    private EditText txtAddress;
    private EditText txtPhone;
    private EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        txtName = (EditText) findViewById(R.id.edit_account_user_name);
        txtLastName = (EditText) findViewById(R.id.edit_account_user_last_name);
        txtResidence = (AutoCompleteTextView) findViewById(R.id.edit_account_user_residence);
        txtAddress = (EditText) findViewById(R.id.edit_account_user_address);
        txtPhone = (EditText) findViewById(R.id.edit_account_user_phone_number);
        txtEmail = (EditText) findViewById(R.id.edit_account_user_email);

        /* Displaying user information */
        displayUserInfo();

        final Button btnUpdate = (Button) findViewById(R.id.btnUpdateUser);
        final Button btnDelete = (Button) findViewById(R.id.btnDeleteUser);

        /* Setting the toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        this.setSupportActionBar(toolbar);
        /* Hiding the title of the toolbar */
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        /* Displaying the hamburger icon */
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        userManager = new UserManager();

        txtResidence.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String residence_initials = String.valueOf(txtResidence.getText());
                if(!residence_initials.isEmpty()){
                    ArrayList<Residence> residenceArray = userManager.getResidences(residence_initials);
                    if(residenceArray != null){
                        BindDictionary<Residence> residenceBindDictionary = new BindDictionary<Residence>();
                        residenceBindDictionary.addStringField(android.R.layout.simple_list_item_1, new StringExtractor<Residence>() {
                            @Override
                            public String getStringValue(Residence item, int position) {
                                return item.get_full_residence();
                            }
                        });
                        FunDapter funDapter = new FunDapter(EditUserActivity.this, residenceArray, android.R.layout.simple_list_item_1, residenceBindDictionary);
                        txtResidence.setAdapter(funDapter);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        /* The user is trying to update his profile */
        if (btnUpdate != null) {
            btnUpdate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        setBackgroundDrawable(btnUpdate, R.drawable.primary_color_button_pressed);
                        if (InternetConnection.haveIInternetConnection(getApplicationContext())) {
                            User user = new User(CurrentUser.get_currentUserProfile());
                            user.set_name(String.valueOf(txtName.getText()));
                            user.set_last_name(String.valueOf(txtLastName.getText()));
                            if (!String.valueOf(txtResidence.getText()).isEmpty()) {
                                //user.set_province();
                                user.set_address(String.valueOf(txtAddress.getText()));
                                user.set_residence(String.valueOf(txtResidence.getText()));
                            }
                            if (!String.valueOf(txtPhone.getText()).isEmpty()) {
                                user.set_phone(String.valueOf(txtPhone.getText()));
                            }
                            if (!String.valueOf(txtEmail.getText()).isEmpty() && UserManager.isValidEmail(String.valueOf(txtEmail.getText()))) {
                                user.set_email(String.valueOf(txtEmail.getText()));
                            }
                            userManager.set_user(user);
                            if (userManager.updateUser()) {
                                CurrentUser.set_currentUserProfile(userManager.get_user());
                                if (!String.valueOf(txtEmail.getText()).equals(user.get_email())) {
                                    /* email already used but the other information were updated */
                                    Toast.makeText(getApplicationContext(), R.string.email_already_used_but_updated, Toast.LENGTH_LONG).show();
                                } else {
                                    /* The user was updated successfully */
                                    Toast.makeText(getApplicationContext(), R.string.user_updated_successfully, Toast.LENGTH_LONG).show();
                                }
                                /* Displaying information after the update */
                                displayUserInfo();
                            } else {
                                /* The user wasn't updated successfully */
                                Toast.makeText(getApplicationContext(), R.string.user_not_updated, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            /* No Internet connection */
                            Toast.makeText(getApplicationContext(), R.string.No_internet_connection, Toast.LENGTH_LONG).show();
                        }
                    }else if (event.getAction() == MotionEvent.ACTION_UP) {
                        setBackgroundDrawable(btnUpdate, R.drawable.primary_color_button);
                    }
                    return true;
                }
            });
        }

        /* The user want to delete his account */
        if (btnDelete != null) {
            btnDelete.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        setBackgroundDrawable(btnDelete, R.drawable.primary_color_button_pressed);
                        new AlertDialog.Builder(getApplicationContext()).setTitle(R.string.delete_profile).setMessage(R.string.are_you_sure_delete_profile).setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /* Checking the status of the Internet connection */
                                if (InternetConnection.haveIInternetConnection(getApplicationContext())) {
                                    /* The user will be deleted */
                                    userManager.set_user(CurrentUser.get_currentUserProfile());
                                    if (userManager.deleteUser()) {
                                        /* The user was deleted */
                                        Toast.makeText(getApplicationContext(), R.string.user_deleted_successfully, Toast.LENGTH_LONG).show();
                                        Intent logOutIntent = new Intent(EditUserActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        EditUserActivity.this.startActivity(logOutIntent);
                                        finish();
                                    } else {
                                        /* An error occurred in the elimination */
                                        Toast.makeText(getApplicationContext(), R.string.user_not_deleted, Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    /* No Internet connection */
                                    Toast.makeText(getApplicationContext(), R.string.No_internet_connection, Toast.LENGTH_LONG).show();
                                }
                                dialog.dismiss();
                            }
                        }).setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /* Nothing to do */
                                dialog.dismiss();
                            }
                        });
                    }else if (event.getAction() == MotionEvent.ACTION_UP) {
                        setBackgroundDrawable(btnDelete, R.drawable.primary_color_button);
                    }
                    return true;
                }
            });
        }
    }

    private void setBackgroundDrawable(View view, int drawable){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            view.setBackground(getDrawable(drawable));
        else
            view.setBackgroundDrawable(getResources().getDrawable(drawable));
    }

    /**
     * Method that displays the user information setting EditTexts texts
     */
    private void displayUserInfo(){
        txtName.setText(CurrentUser.get_currentUserProfile().get_name());
        txtLastName.setText(CurrentUser.get_currentUserProfile().get_last_name());
        txtEmail.setText(CurrentUser.get_currentUserProfile().get_email());

        if(CurrentUser.get_currentUserProfile().get_phone() != null && !CurrentUser.get_currentUserProfile().get_phone().isEmpty())
            txtPhone.setText(CurrentUser.get_currentUserProfile().get_phone());
        else
            txtPhone.setText("");

        if(CurrentUser.get_currentUserProfile().get_residence() != null && !CurrentUser.get_currentUserProfile().get_residence().isEmpty())
            txtResidence.setText(CurrentUser.get_currentUserProfile().get_residence_and_province());
        else
            txtResidence.setText("");

        if(CurrentUser.get_currentUserProfile().get_address() != null && !CurrentUser.get_currentUserProfile().get_address().isEmpty())
            txtAddress.setText(CurrentUser.get_currentUserProfile().get_address());
        else
            txtAddress.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}