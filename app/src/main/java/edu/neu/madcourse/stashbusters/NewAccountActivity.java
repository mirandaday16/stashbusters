package edu.neu.madcourse.stashbusters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import edu.neu.madcourse.stashbusters.databinding.NewAccountActivityBinding;
import edu.neu.madcourse.stashbusters.model.User;


public class NewAccountActivity extends AppCompatActivity {

    // Set up ViewBinding for the layout
    private NewAccountActivityBinding binding;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_account_activity);

        // Setting up binding instance and view instances
        binding = NewAccountActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        final EditText usernameField = binding.usernameInput;
        final EditText passwordField = binding.passwordInput;
        final ImageButton profilePicButton = binding.imageButton;
        final EditText bioField = binding.bioInput;
        final Button saveButton = binding.saveButton;

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
                // TODO: Get all information from form and save to Firebase
                String username_str = usernameField.getText().toString();
                String password_str = passwordField.getText().toString();
                String photoUrl_str = ""; // TODO change this
                String bio_str = bioField.getText().toString();
                String email = username_str + "@example.com"; // TODO: change this
                String deviceToken = "TEMP";

                register(username_str, email, password_str, photoUrl_str, bio_str);
            }
        });

        setContentView(view);

    }

    private void register(final String username, final String email, String password, final String photoUrl, final String bio) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(NewAccountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userId = firebaseUser.getUid();

                            userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                            User user = new User(username, email, photoUrl, bio);
                            Map<String, Object> hashMap = user.toMap();

                            userRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        System.out.println("Save new user to DB successfully");
                                        Intent intent = new Intent(NewAccountActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }
                });
    }
}