package edu.neu.madcourse.stashbusters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.FirebaseApp;

import edu.neu.madcourse.stashbusters.views.NewPostActivity;
import edu.neu.madcourse.stashbusters.views.PanelPostActivity;
import edu.neu.madcourse.stashbusters.views.SnackPostActivity;
import edu.neu.madcourse.stashbusters.views.SwapPostActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
    }


    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.new_post_button:
                intent = new Intent(this, NewPostActivity.class);
                startActivity(intent);
                break;
            case R.id.snack_busting_button:
                intent = new Intent(this, SnackPostActivity.class);
                startActivity(intent);
                break;
            case R.id.stash_panel_button:
                intent = new Intent(this, PanelPostActivity.class);
                startActivity(intent);
                break;
            case R.id.stash_swap_button:
                intent = new Intent(this, SwapPostActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}