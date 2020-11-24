package edu.neu.madcourse.stashbusters;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;


import edu.neu.madcourse.stashbusters.databinding.LoginLandingScreenBinding;

public class LoginActivity extends AppCompatActivity {

    // Set up ViewBinding for the layout
    private LoginLandingScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

        // Hide Action Bar - for aesthetic purposes only
        getSupportActionBar().hide();

        // Setting up binding instance and view instances
        binding = LoginLandingScreenBinding.inflate(getLayoutInflater());
        final Button logInButton = binding.logInButton;
        final Button signUpButton = binding.signUpButton;


    }
}