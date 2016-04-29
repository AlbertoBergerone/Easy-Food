package com.example.Easy_Food.app.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.Easy_Food.app.R;
import com.example.Easy_Food.app.User;

public class MainActivity extends Activity {
    User user;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }


    private Dialog createDialog(int idTitle, int idLayout){
        Dialog retDialog = new Dialog(this);

        retDialog.setCancelable(false);
        /* Setting the title */
        retDialog.setTitle(idTitle);
        /* Setting the layout */
        retDialog.setContentView(idLayout);
        /**/
        return retDialog;
    }

    /**
	 * Method that manages clicks for login and signup dialog buttons
	 */
    public void onClick(View view) {
        Dialog dialog;

        if (view.getId() == R.id.btn_home_login) {
            /* I've pressed the login button */
            /* Creating a new Dialog */
            dialog = createDialog(R.string.login, R.layout.login_dialog);

            EditText txt_username;
            EditText txt_password;
            Button btn_dialog_login;
            Button btn_dialog_cancel;

            /* Associating view objects */
            txt_username = (EditText) dialog.findViewById(R.id.txt_login_username);
            txt_password = (EditText) dialog.findViewById(R.id.txt_login_password);
            btn_dialog_login = (Button) dialog.findViewById(R.id.btn_login_dialog_login);
            btn_dialog_cancel = (Button) dialog.findViewById(R.id.btn_login_dialog_cancel);
            ;

            /* setting onClickListeners */
            /* login button onClickListener */
            btn_dialog_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /* Reading the content of input texts */
                    String username = txt_username.getText().toString().trim();
                    String password = txt_password.getText().toString();
                    /* Input control */
                    if (username == null) {
                        /* Invalid username */
                        txt_username.setBackgroundColor(getResources().getColor(R.color.error_color));
                    }
                    if (password == null) {
                        /* Invalid password */
                        txt_password.setBackgroundColor(getResources().getColor(R.color.error_color));
                    } else if (username != null) {
                        /* All fields are correct */
                        user = new User(username, password);
                        /* Sending the user information to the server for authenticate it*/
                        if (user.loginMe()) {
                            /* If the login was successful it will load a new activity */
                            txt_password.setBackgroundColor(getResources().getColor(R.color.error_color));
                            txt_password.setBackgroundColor(getResources().getColor(R.color.error_color));
                            Intent intent = new Intent(getApplicationContext(), MainContainerActivity.class);
                        } else {
                            Toast toast = Toast.makeText(dialog.getContext(), R.string.login_error, Toast.LENGTH_SHORT);
                            toast.show();
                            /*******************************

                             https://www.youtube.com/watch?v=166IePEj5GM

                             ***********************************/
                        }
                    }
                }
            });
            /* cancel button onClickListener */
            btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /* Closing the dialog */
                    dialog.dismiss();
                }
            });
            /* Showing the dialog */
            dialog.show();
        } else if (view.getId() == R.id.btn_home_signup) {
            /* I've pressed the sign-up button */
            /* Creating a new Dialog */
            dialog = createDialog(R.string.sign_up, R.layout.signup_dialog);


            // Set up the input edit texts
            EditText txt_username;
            EditText txt_password;
            EditText txt_name;
            EditText txt_last_name;
            EditText txt_email;
            txt_username = (EditText) dialog.findViewById(R.id.txt_signup_username);
            txt_password = (EditText) dialog.findViewById(R.id.txt_signup_password);
            txt_name = (EditText) dialog.findViewById(R.id.txt_signup_name);
            txt_last_name = (EditText) dialog.findViewById(R.id.txt_signup_last_name);
            txt_email = (EditText) dialog.findViewById(R.id.txt_signup_email);
        }
    }

}
