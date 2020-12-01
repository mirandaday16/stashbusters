package edu.neu.madcourse.stashbusters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.feed_test:
                intent = new Intent(this,YourFeedActivity.class);
                startActivity(intent);
                break;
            case R.id.new_post_button:
                intent = new Intent(this, NewPostActivity.class);
                startActivity(intent);
                break;
            case R.id.snack_busting_button:
                intent = new Intent(this, SnackBustingActivity.class);
                startActivity(intent);
                break;
            case R.id.stash_panel_button:
                intent = new Intent(this, StashPanelActivity.class);
                startActivity(intent);
                break;
            case R.id.stash_swap_button:
                intent = new Intent(this, StashSwapActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}