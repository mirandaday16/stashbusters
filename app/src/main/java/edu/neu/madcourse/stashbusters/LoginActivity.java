package edu.neu.madcourse.stashbusters;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.LinkedList;

import edu.neu.madcourse.stashbusters.databinding.LoginLandingScreenBinding;

public class LoginActivity extends AppCompatActivity {

    // Set up ViewBinding for the layout
    private LoginLandingScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login_landing_screen);

        // Hide Action Bar - for aesthetic purposes only
        getSupportActionBar().hide();

        // Setting up binding instance and view instances
        binding = LoginLandingScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        final Button logInButton = binding.logInButton;
        final Button signUpButton = binding.signUpButton;

        // Setting onClickListener for Log In Button - opens dialog box to enter username and password
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogBox(LoginActivity.this);
            }
        });

        // Setting onClickListener for Sign Up Button - opens New Account Activity
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });

        setContentView(view);
    }

    /*
    // Helper functions:
    */

    // Starts New Account activity
    private void createNewAccount() {
        Intent intent = new Intent(this, NewAccountActivity.class);
        startActivity(intent);
    }

    // Opens a dialog box for user to input username and password

    private void showDialogBox(Context context) {
        final EditText usernameField = new EditText(context);
        final EditText passwordField = new EditText(context);
        final AlertDialog credentialsBox = new AlertDialog.Builder(context)
                .setTitle("Log In")
                .setMessage("Enter your username and password")
                .setView(usernameField)
                .setView(passwordField)
                .setPositiveButton("Log In", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO: Validate this data using Firebase
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        credentialsBox.show();
    }


}