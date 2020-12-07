package edu.neu.madcourse.stashbusters.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.neu.madcourse.stashbusters.R;

public class SnackIntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack_intro);
    }

    public void onCookieClick(View view) {
        Intent intent = new Intent(this, SnackPostActivity.class);
        this.startActivity(intent);
        finish();
    }
}