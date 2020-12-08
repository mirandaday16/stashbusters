package edu.neu.madcourse.stashbusters.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import edu.neu.madcourse.stashbusters.R;

public class SnackIntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack_intro);


        // Only show this activity the first time the user opens SnackBusting
        // Adapted from: https://stackoverflow.com/questions/18612824/how-to-run-an-activity-only-once-like-splash-screen
        SharedPreferences settings=getSharedPreferences("prefs",0);
        boolean firstRun=settings.getBoolean("firstRun",false);

        // If running for the first time...
        if(!firstRun)
        {
            SharedPreferences.Editor editor=settings.edit();
            editor.putBoolean("firstRun",true);
            editor.commit();
        }
        // For all other times...
        else
        {
            Intent intent=new Intent(this,SnackPostActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void onCookieClick(View view) {
        Intent intent = new Intent(this, SnackPostActivity.class);
        this.startActivity(intent);
        finish();
    }
}