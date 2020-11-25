package edu.neu.madcourse.stashbusters;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import edu.neu.madcourse.stashbusters.databinding.PersonalProfileActivityBinding;

public class PersonalProfileActivity extends AppCompatActivity {

    // Set up ViewBinding for the layout
    private PersonalProfileActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_profile_activity);
    }
}