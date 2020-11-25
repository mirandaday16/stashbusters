package edu.neu.madcourse.stashbusters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.neu.madcourse.stashbusters.databinding.PersonalProfileActivityBinding;

public class PersonalProfileActivity extends AppCompatActivity {

    // Set up ViewBinding for the layout
    private PersonalProfileActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_profile_activity);

        // Hide Action Bar - this page uses a toolbar instead with a menu
        getSupportActionBar().hide();

        // Setting up binding instance and view instances
        binding = PersonalProfileActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        final Toolbar toolbar = binding.profilePageToolbar;
        final TextView username = binding.usernameDisplay;
        final ImageView profilePic = binding.profilePicture;
        final TextView followerCountView = binding.followerCount;
        final TextView bio = binding.bio;
        final Button myPostsButton = binding.myPosts;
        final Button likedPostsButton = binding.likedPosts;
        final RecyclerView postsView = binding.postViewArea;
        final BottomNavigationView navBar = binding.navigationBar;

        setupBottomNavigationView(navBar);

        // TODO: Set onClickListener for toolbar menu

        // Setting onClickListener for My Posts Button - switches RecyclerView to user's own posts
        myPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: get user's posts from Firebase and display in RecyclerView
            }
        });

        // Setting onClickListener for Liked Posts Button - switches RecyclerView to posts the user
        // has liked
        likedPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: get liked posts from Firebase and display in RecyclerView
            }
        });


        setContentView(view);
    }

    /*
    // Helper functions:
    */

    // Sets up Bottom Navigation Bar and its onClickListener
    private void setupBottomNavigationView(BottomNavigationView view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.palette_button:
                        // Open MyFeed
                        break;
                    case R.id.world_button:
                        // Open World Feed
                        break;
                    case R.id.pencil_button:
                        startNewPostActivity();
                        break;
                    case R.id.mouse_button:
                        // Nothing happens. The user is already here.
                        break;
                    case R.id.cookie_button:
                        startSnackBustingActivity();
                        break;
                }
                return true;
            }
        });
    }

    // Starts New Post Activity
    private void startNewPostActivity() {
        Intent intent = new Intent(this, NewPostActivity.class);
        startActivity(intent);
    }

    // Starts Snack Busting Activity
    private void startSnackBustingActivity() {
        Intent intent = new Intent(this, SnackBustingActivity.class);
        startActivity(intent);
    }
}