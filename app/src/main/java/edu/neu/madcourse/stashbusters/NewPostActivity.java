package edu.neu.madcourse.stashbusters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.new_panel:
            case R.id.panel_button:
                intent = new Intent(this, NewPanelActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}