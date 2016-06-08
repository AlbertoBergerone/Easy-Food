package com.example.alberto.easyfood.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alberto.easyfood.ServerCommunicationModule.InternetConnection;
import com.example.alberto.easyfood.R;
import com.example.alberto.easyfood.UserModule.User;

/**
 * Created by inf.bergeronea1610 on 06/06/2016.
 */
public class EditUser extends Activity {
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        user = (User) this.getIntent().getSerializableExtra("user");

        final EditText txtName = (EditText) findViewById(R.id.edit_account_user_name);
        final EditText txtLastName = (EditText) findViewById(R.id.edit_account_user_last_name);
        final EditText txtResidence = (EditText) findViewById(R.id.edit_account_user_residence);
        final EditText txtAddress = (EditText) findViewById(R.id.edit_account_user_address);
        final EditText txtPhone = (EditText) findViewById(R.id.edit_account_user_phone_number);
        final EditText txtEmail = (EditText) findViewById(R.id.edit_account_user_email);

        txtName.setText(user.get_name());
        txtLastName.setText(user.get_last_name());
        txtResidence.setText(user.get_residence());
        txtAddress.setText(user.get_address());
        txtPhone.setText(user.get_phone());
        txtEmail.setText(user.get_email());

        Button btnUpdate = (Button) findViewById(R.id.btnUpdateUser);
        Button btnDelete = (Button) findViewById(R.id.btnDeleteUser);

        /* The user is trying to update his profile */
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetConnection.haveIInternetConnection(getApplicationContext())) {
                    user.set_name(String.valueOf(txtName.getText()));
                    user.set_last_name(String.valueOf(txtLastName.getText()));
                    user.set_email(String.valueOf(txtEmail.getText()));
                    user.set_phone(String.valueOf(txtPhone.getText()));
                    user.set_residence(String.valueOf(txtResidence.getText()));
                    user.set_address(String.valueOf(txtAddress.getText()));

                    if(user.updateMe()){
                        if (!String.valueOf(txtEmail.getText()).equals(user.get_email())) {
                            /* email already used but the other information were updated */
                            Toast.makeText(getApplicationContext(), R.string.email_already_used_but_updated, Toast.LENGTH_LONG).show();
                        }else {
                            /* The user was updated successfully */
                            Toast.makeText(getApplicationContext(), R.string.user_updated_successfully, Toast.LENGTH_LONG).show();
                        }
                    }else{
                        /* The user wasn't updated successfully */
                        Toast.makeText(getApplicationContext(), R.string.user_not_updated, Toast.LENGTH_LONG).show();
                    }
                }else{
                    /* No Internet connection */
                    Toast.makeText(getApplicationContext(), R.string.No_internet_connection, Toast.LENGTH_LONG).show();
                }
            }
        });

        /* The user want to delete his account */
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getApplicationContext()).setTitle(R.string.delete_profile).setMessage(R.string.are_you_sure_delete_profile).setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /* Checking the status of the Internet connection */
                        if (InternetConnection.haveIInternetConnection(getApplicationContext())) {
                        /* The user will be deleted */
                            if (user.deleteMe()) {
                            /* The user was deleted */
                                Toast.makeText(getApplicationContext(), R.string.user_deleted_successfully, Toast.LENGTH_LONG).show();
                                Intent exitProfileIntent = new Intent(EditUser.this, LoginActivity.class);
                                EditUser.this.startActivity(exitProfileIntent);
                            } else {
                            /* An error occurred in the elimination */
                                Toast.makeText(getApplicationContext(), R.string.user_not_deleted, Toast.LENGTH_LONG).show();
                            }
                        }else{
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
            }
        });
    }
}
