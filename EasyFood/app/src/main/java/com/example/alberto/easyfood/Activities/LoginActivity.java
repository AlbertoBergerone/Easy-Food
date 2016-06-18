package com.example.alberto.easyfood.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alberto.easyfood.R;
import com.example.alberto.easyfood.UserModule.CurrentUser;
import com.example.alberto.easyfood.UserModule.User;
import com.example.alberto.easyfood.ServerCommunicationModule.InternetConnection;
import com.example.alberto.easyfood.UserModule.UserManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginActivity extends AppCompatActivity {
    UserManager userManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        final Button btnLogin = (Button) findViewById(R.id.btnLogin);
        final TextView txtSignUp = (TextView) findViewById(R.id.txtTakeMeToSignUpActivity);
        final EditText txtUsername = (EditText) findViewById(R.id.txtLoginUsername);
        final EditText txtPassword = (EditText) findViewById(R.id.txtLoginPassword);

        userManager = new UserManager();
        /* Setting onClickListeners to trigger events */
        /* On a click on the login button it will try to contact the web server for login the user.
         * The server will check the correctness of user information. If they are valid it will return all the user information
         * and it will open the activity that permits to search restaurants.
         * If something is wrong it will ask again user information.
         * */
        if (btnLogin != null && txtSignUp != null && txtUsername != null && txtPassword != null) {
            btnLogin.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        setBackgroundDrawable(btnLogin, R.drawable.transparent_button_pressed);
						boolean areAllTheDetailsEntered = true;

						/* Checking the status of password and username fields */
						if(isTheFieldEmpty(txtUsername))
							areAllTheDetailsEntered = false;
						if(isTheFieldEmpty(txtPassword))
							areAllTheDetailsEntered = false;

						if(areAllTheDetailsEntered){
							/* All information are valid */
							/* Checking if the internet connection is turned on */
							if(InternetConnection.haveIInternetConnection(getApplicationContext())){
								/* If it's turned on */
								/* Trying to login the user */
								/* Creating a new User only with the username and the password properties.
								* The other properties will be loaded only if the user is already signed */
								userManager.set_user(new User(String.valueOf(txtUsername.getText()), String.valueOf(txtPassword.getText())));
								if(userManager.loginUser()){
                                    CurrentUser.set_currentUserProfile(userManager.get_user());
									Intent homeActivityIntent = new Intent(LoginActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									LoginActivity.this.startActivity(homeActivityIntent);
                                    LoginActivity.this.finish();
								}else{
									/* Something went wrong with the login */
									Toast.makeText(getApplicationContext(), R.string.login_not_valid, Toast.LENGTH_LONG).show();
								}
							}else{
								/* There is no Internet connection */
								Toast.makeText(getApplicationContext(), R.string.No_internet_connection, Toast.LENGTH_LONG).show();
							}
						}else{
							/* At least one of the input fields are empty */
							Toast.makeText(getApplicationContext(), R.string.input_empty_fields, Toast.LENGTH_LONG).show();
						}
					}else if (event.getAction() == MotionEvent.ACTION_UP) {
                        setBackgroundDrawable(btnLogin, R.drawable.transparent_button);
					}
                    return true;
				}
            });

            /* On a click on the signup label it will open another activity which will ask user details to sign up him */
            txtSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /* Starting the activity for user's sign-up */
                    Intent registerIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                    LoginActivity.this.startActivity(registerIntent);
                }
            });
        }
    }

    private boolean isTheFieldEmpty(EditText editText){
        boolean is_empty;
        if(String.valueOf(editText.getText()).isEmpty()) {
            is_empty = true;
            /* Setting red borders if the field is empty */
            setBackgroundDrawable(editText, R.drawable.error_edit_text);
        }else{
            is_empty = false;
           /* Setting a drawable element without red borders if the field is not empty */
            setBackgroundDrawable(editText, R.drawable.transparent_edit_text);
        }
        return(is_empty);
    }

    private void setBackgroundDrawable(View view, int drawable){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            view.setBackground(getDrawable(drawable));
        else
            view.setBackgroundDrawable(getResources().getDrawable(drawable));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

}
