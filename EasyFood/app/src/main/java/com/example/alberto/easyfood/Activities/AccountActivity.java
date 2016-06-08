package com.example.alberto.easyfood.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alberto.easyfood.R;
import com.example.alberto.easyfood.UserModule.User;

public class AccountActivity extends AppCompatActivity {
    private User user;
    private static final int EDIT_ACCOUNT_ACTIVITY = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        /* Getting User object that contains th user information */
        user = (User) this.getIntent().getSerializableExtra("user");

        /* Displaying user information */
        TextView txtUsername = (TextView) findViewById(R.id.account_user_username);
        TextView txtName = (TextView) findViewById(R.id.account_user_name);
        TextView txtLastName = (TextView) findViewById(R.id.account_user_last_name);
        TextView txtEmail = (TextView) findViewById(R.id.account_user_email);
        Button btnUpdate = (Button) findViewById(R.id.btnUpdateUserInformation);
        Button btnLogOut = (Button) findViewById(R.id.btnLogOutUser);

        txtUsername.setText(user.get_username());
        txtLastName.setText(user.get_last_name());
        txtName.setText(user.get_name());
        txtEmail.setText(user.get_email());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editUserIntent = new Intent(AccountActivity.this, EditUser.class).putExtra("user", user);
                AccountActivity.this.startActivityForResult(editUserIntent, EDIT_ACCOUNT_ACTIVITY);
            }
        });

        /* Logging out */
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = null;
                /* Setting flags to clean up all activities */
                Intent logOutIntent = new Intent(AccountActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                AccountActivity.this.startActivity(logOutIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /* Returning the user (maybe it was modified) */
        Intent returnIntent = new Intent().putExtra("user", user);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
