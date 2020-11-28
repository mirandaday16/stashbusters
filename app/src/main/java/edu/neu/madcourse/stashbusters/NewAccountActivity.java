package edu.neu.madcourse.stashbusters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.neu.madcourse.stashbusters.databinding.NewAccountActivityBinding;


public class NewAccountActivity extends AppCompatActivity {
    private static final String TAG = NewAccountActivity.class.getSimpleName();

    // Set up ViewBinding for the layout
    private NewAccountActivityBinding binding;

    // Set up a FirebaseAuth object to save the new account
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_account_activity);

        // Setting up binding instance and view instances
        binding = NewAccountActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        final EditText emailField = binding.emailInput;
        final EditText usernameField = binding.usernameInput;
        final EditText passwordField = binding.passwordInput;
        final ImageButton profilePicButton = binding.imageButton;
        final EditText bioField = binding.bioInput;
        final Button saveButton = binding.saveButton;

        // Initializing auth object for Firebase account creation
        mAuth = FirebaseAuth.getInstance();

        // Setting onClickListener for Profile Picture Button - opens camera (sensor) for user to
        // choose a profile picture
        profilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Open camera for user to choose a profile picture
            }
        });

        // Setting onClickListener for Save Button - gathers information entered in form and saves
        // in Firebase
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = emailField.getText().toString();
                String password = passwordField.getText().toString();
                if (password.length() > 16 || password.length() < 8) {
                    Toast.makeText(NewAccountActivity.this,
                            "Please choose a password between 8-16 characters.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(emailAddress, password)
                            .addOnCompleteListener(NewAccountActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        startWorldFeedActivity(); // TODO: "user" needs to be passed to this somehow
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(NewAccountActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        setContentView(view);

        }

     /*
    // Helper functions:
    */

// Starts World Feed Activity
private void startWorldFeedActivity(){
        // TODO: When World Feed activity exists, change this function to go  to World Feed
        Intent intent=new Intent(this,PersonalProfileActivity.class);
        startActivity(intent);
        }
        }