package com.example.alberto.easyfood.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alberto.easyfood.R;
import com.example.alberto.easyfood.ServerCommunicationModule.InternetConnection;
import com.example.alberto.easyfood.UserModule.CurrentUser;
import com.example.alberto.easyfood.UserModule.UserManager;

/**
 * Created by Alberto on 01/07/2016.
 */
public class EditPasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        /* Setting the toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        this.setSupportActionBar(toolbar);
        /* Hiding the title of the toolbar */
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        /* Displaying the hamburger icon */
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        final EditText txtCurrentPassword = (EditText) findViewById(R.id.edit_user_current_password);
        final EditText txtNewPassword = (EditText) findViewById(R.id.edit_user_new_password);
        final EditText txtRepeatPassword = (EditText) findViewById(R.id.edit_user_repeat_password);

        final Button btnUpdate = (Button) findViewById(R.id.btnUpdateUserPassword);

        if (btnUpdate != null) {
            btnUpdate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        /* Changing button background */
                        setBackgroundDrawable(btnUpdate, R.drawable.primary_color_button_pressed);
                        if (InternetConnection.haveIInternetConnection(getApplicationContext())) {
                            /* Checking if the current password is valid */
                            if(CurrentUser.get_currentUserProfile().get_password().equals(String.valueOf(txtCurrentPassword.getText()))){
                                String newPassword = String.valueOf(txtNewPassword.getText());
                                /* Checking if the password is inserted correctly */
                                if(!newPassword.isEmpty()) {
                                    if (newPassword.equals(String.valueOf(txtRepeatPassword.getText()))) {
                                        UserManager userManager = new UserManager();
                                        userManager.set_user(CurrentUser.get_currentUserProfile());
                                        userManager.get_user().set_password(newPassword);
                                        if (userManager.updateUser()) {
                                        /* The user was updated successfully */
                                        /* Changing the current user profile information */
                                            CurrentUser.set_currentUserProfile(userManager.get_user());
                                            Toast.makeText(getApplicationContext(), R.string.user_updated_successfully, Toast.LENGTH_LONG).show();
                                            Intent homeIntent = new Intent(EditPasswordActivity.this, HomeActivity.class);
                                            EditPasswordActivity.this.startActivity(homeIntent);
                                            finish();
                                        } else {
                                        /* The user wasn't updated successfully */
                                            Toast.makeText(getApplicationContext(), R.string.user_not_updated, Toast.LENGTH_LONG).show();
                                        }

                                    } else {
                                        Toast.makeText(EditPasswordActivity.this, R.string.passwords_not_equals, Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(EditPasswordActivity.this, R.string.input_empty_fields, Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(EditPasswordActivity.this, R.string.password_not_valid, Toast.LENGTH_LONG).show();
                            }
                        }else{
                            /* No Internet connection */
                            Toast.makeText(getApplicationContext(), R.string.No_internet_connection, Toast.LENGTH_LONG).show();
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        setBackgroundDrawable(btnUpdate, R.drawable.primary_color_button);
                    }
                    return true;
                }
            });
        }
    }



    /**
     * Method that changes the background to the View that is passed
     * @param view (View) view you want to change background
     * @param drawable (int) drawable-id background
     */
    private void setBackgroundDrawable(View view, int drawable){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            view.setBackground(getDrawable(drawable));
        else
            view.setBackgroundDrawable(getResources().getDrawable(drawable));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* If it is pressed the back button it will call the onBackPressed() method */
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
