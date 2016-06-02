package com.example.alberto.easyfood.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alberto.easyfood.R;
import com.example.alberto.easyfood.UserModule.User;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SignUpActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        final TextView comeBackToLogin = (TextView) findViewById(R.id.txtTakeMeToLoginActivity);
        final EditText txtName = (EditText) findViewById(R.id.txtSignUpName);
        final EditText txtLastName = (EditText) findViewById(R.id.txtSignUpLastName);
        final EditText txtUsername = (EditText) findViewById(R.id.txtSignUpUsername);
        final EditText txtPassword = (EditText) findViewById(R.id.txtSignUpPassword);
        final EditText txtRepeatPassword = (EditText) findViewById(R.id.txtSignUpRepeatPassword);
        final EditText txtEmail = (EditText) findViewById(R.id.txtSignUpEmail);
        final Button btnSignUp = (Button) findViewById(R.id.btnSignUp);

        /* Setting onClickListeners */
        /* A click on the text it will come back to the login activity */
        if (comeBackToLogin != null) {
            comeBackToLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /* Coming back to the login activity */
                    Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                    SignUpActivity.this.startActivity(loginIntent);
                }
            });
        }

        if (btnSignUp != null && txtLastName != null && txtName != null && txtUsername != null && txtPassword != null && txtRepeatPassword != null && txtEmail != null) {
            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean areAllTheDetailsEntered = true;
                    /* Checking if at least one of the input fields is empty */
                    if (isTheFieldEmpty(txtName))
                        areAllTheDetailsEntered = false;
                    if(isTheFieldEmpty(txtLastName))
                        areAllTheDetailsEntered = false;
                    if(isTheFieldEmpty(txtUsername))
                        areAllTheDetailsEntered = false;
                    if(isTheFieldEmpty(txtEmail))
                        areAllTheDetailsEntered = false;
                    if(isTheFieldEmpty(txtPassword))
                        areAllTheDetailsEntered = false;
                    if(isTheFieldEmpty(txtRepeatPassword))
                        areAllTheDetailsEntered = false;

                    if(areAllTheDetailsEntered){
                        /* All fields look like they are valid */
                        /* Checking if the both password fields contain the same password */
                        String password = String.valueOf(txtPassword.getText());
                        if(password.equals(String.valueOf(txtRepeatPassword.getText()))){
                            /* If they are equals I will try to sign up the new user */
                            User user = new User(String.valueOf(txtUsername.getText()), password, String.valueOf(txtName.getText()), String.valueOf(txtLastName.getText()), String.valueOf(txtEmail.getText()));
                            //user.signupMe(); public void try_to_communicate() throws Exception {
                            if(user.signupMe()){
                                Toast.makeText(getApplicationContext(), user.get_userID(), Toast.LENGTH_SHORT).show();
                                Intent mainActivityIntent = new Intent(SignUpActivity.this, MainActivity.class);
                                SignUpActivity.this.startActivity(mainActivityIntent);
                            }
                        }else{
                            /* Setting an 'error background' */
                            setBackgroundDrawable(txtPassword, R.drawable.error_edit_text);
                            setBackgroundDrawable(txtRepeatPassword, R.drawable.error_edit_text);
                            /* Saying to the user to enter the same password */
                            Toast.makeText(getApplicationContext(), R.string.passwords_not_equals, Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        /* At least one of the input fields are empty */
                        Toast.makeText(getApplicationContext(), R.string.input_empty_fields, Toast.LENGTH_SHORT).show();
                    }
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
