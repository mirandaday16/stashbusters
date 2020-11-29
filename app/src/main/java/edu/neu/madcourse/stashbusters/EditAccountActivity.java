package edu.neu.madcourse.stashbusters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import edu.neu.madcourse.stashbusters.databinding.EditAccountActivityBinding;
import edu.neu.madcourse.stashbusters.model.User;

public class EditAccountActivity extends AppCompatActivity {

    // Set up ViewBinding for the layout
    private EditAccountActivityBinding binding;

    private SharedPreferences prefs;
    private String sharedUsername = "username";
    private String currentUsername;
    private String currentProfilePic;
    private String currentBio;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting up binding instance and view instances
        binding = EditAccountActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        final ImageButton profilePicButton = binding.imageButton;
        final EditText bioField = binding.bioInput;
        final Button saveButton = binding.saveButton;

        // Store user's current username to store data in Firebase DB
        prefs = getSharedPreferences(sharedUsername, MODE_PRIVATE);
        currentUsername = prefs.getString(sharedUsername, "");
        // TODO: Display user's current profile picture on ImageButton
        currentBio = prefs.getString("bio", "");
        bioField.setText(currentBio);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Setting onClickListener for Profile Picture Button - opens camera (sensor) for user to choose a new profile picture
        profilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Open camera for user to choose a new profile picture
            }
        });

        // Setting onClickListener for Save Button - gathers information entered in form and saves in Firebase
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String bio = bioField.getText().toString();
                setProfileData(mDatabase, currentUsername, bio);
            }
        });

        setContentView(view);
    }

     /*
    // Helper functions:
    */

    // Sets updated photo and bio in Firebase database
    // TODO: Add profile picture after format changed from Bitmap to String
    private void setProfileData(DatabaseReference postRef, final String username, final String bio) {
        postRef
                .child("users")
                .child(username)
                .runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                        User u = currentData.getValue(User.class);
                        // Check that username exists in database; else, return
                        if (u == null) {
                            return Transaction.success(currentData);
                        }
                        // Update bio in database
                        u.setBio(bio);

                        currentData.setValue(u);
                        return Transaction.success(currentData);

                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                        // Transaction completed
                        Log.d(NewAccountActivity.TAG, "sentTransaction:onComplete:" + error);

                    }
                });
    }
}