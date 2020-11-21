package edu.neu.madcourse.stashbusters;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NewPanelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_panel);
    }

    // How to implement user inserting an image:
    // https://stackoverflow.com/questions/14433096/allow-the-user-to-insert-an-image-in-android-app
}