package edu.neu.madcourse.stashbusters.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.databinding.ActivityNewPostBinding;
import edu.neu.madcourse.stashbusters.enums.NavigationBarButtons;

public class NewPostActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up ViewBinding for the layout
        edu.neu.madcourse.stashbusters.databinding.ActivityNewPostBinding binding =
                ActivityNewPostBinding.inflate(getLayoutInflater());

        // Navigation bar setup:
        NavigationBarView navigationBarView = binding.navigationBar;
        navigationBarView.setSelected(NavigationBarButtons.NEWPOST);

        setContentView(binding.getRoot());
    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.new_panel:
            case R.id.panel_button:
                intent = new Intent(this, NewPanelActivity.class);
                startActivity(intent);
                break;
            case R.id.new_swap:
            case R.id.swap_button:
                intent = new Intent(this, NewSwapActivity.class);
                startActivity(intent);
                break;

            case R.id.new_snack:
            case R.id.snack_button:
                intent = new Intent(this, NewSnackActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}