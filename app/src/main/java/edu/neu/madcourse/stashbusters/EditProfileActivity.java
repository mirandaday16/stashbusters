package edu.neu.madcourse.stashbusters;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import edu.neu.madcourse.stashbusters.databinding.EditAccountActivityBinding;

public class EditProfileActivity extends AppCompatActivity {

    // Set up ViewBinding for the layout
    private EditAccountActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account_activity);

        // Setting up binding instance and view instances
        binding = EditAccountActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        final ImageButton profilePicButton = binding.imageButton;
        final EditText bioField = binding.bioInput;
        final Button saveButton = binding.saveButton;

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
                // TODO: Get all information from form and save to Firebase
            }
        });

        setContentView(view);
    }
}