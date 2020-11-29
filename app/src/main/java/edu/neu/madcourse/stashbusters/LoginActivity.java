package edu.neu.madcourse.stashbusters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.neu.madcourse.stashbusters.databinding.LoginLandingScreenBinding;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = NewAccountActivity.class.getSimpleName();

    // Set up ViewBinding for the layout
    private LoginLandingScreenBinding binding;

    // Set up a FirebaseAuth object for login
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_landing_screen);

        // Setting up binding instance and view instances
        binding = LoginLandingScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        final Button logInButton = binding.logInButton;
        final Button signUpButton = binding.signUpButton;

        // Initializing auth object for Firebase login
        mAuth = FirebaseAuth.getInstance();


        // Setting onClickListener for Log In Button - opens dialog box to enter username and password
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogBox(LoginActivity.this, R.style.AlertDialogCustom);
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in; if so, go to World Feed activity
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startWorldFeedActivity();
        }
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

    private void showDialogBox(Context context, int themeResId) {
        // Creating a Linear Layout containing EditText fields for entering username and password
        LayoutInflater factory = LayoutInflater.from(this);
        final View credentialsView = factory.inflate(R.layout.login_dialog, null);
        final EditText usernameField = (EditText) credentialsView.findViewById(R.id.usernameEntry);
        final EditText passwordField = (EditText) credentialsView.findViewById(R.id.passwordEntry);

        // Creating an Alert Dialog for the user to enter their username and password
        final AlertDialog credentialsBox = new AlertDialog.Builder(context, themeResId)
                .setView(credentialsView)
                .setPositiveButton("Log In", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Pass username and password entered by the user into Firebase for validation
                        String username = usernameField.getText().toString().trim();
                        String password = passwordField.getText().toString().trim();
                        // Check if either username or password is blank. If so, display toast message to user.
                        if (username.matches("") || password.matches("")) {
                            Toast.makeText(LoginActivity.this, "Please enter a username and password", Toast.LENGTH_LONG).show();
                        } else {
                            mAuth.signInWithEmailAndPassword(username, password)
                                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, send user to World Feed
                                                Log.d(TAG, "signInWithEmail:success");
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                startWorldFeedActivity(); // TODO: "user" needs to be passed to this somehow
                                            } else {
                                                // If sign in fails, display a toast message to the user
                                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        credentialsBox.show();
    }

    // Starts World Feed Activity
    private void startWorldFeedActivity() {
        // TODO: When World Feed activity exists, change this function to go  to World Feed
        Intent intent = new Intent(this, PersonalProfileActivity.class);
        startActivity(intent);
    }

}