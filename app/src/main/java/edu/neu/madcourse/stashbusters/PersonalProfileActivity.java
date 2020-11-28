package edu.neu.madcourse.stashbusters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

        // Navigation bar buttons:
        final ImageButton myFeedButton = binding.myFeed;
        final ImageButton worldFeedButton = binding.worldFeed;
        final ImageButton newPostButton = binding.newPost;
        final ImageButton myProfileButton = binding.myProfile;
        final ImageButton snackBustingButton = binding.snackBusting;

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

        // Setting onClickListener for navigation bar buttons
        myFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: start My Feed Activity; for now, will do nothing
            }
        });

        worldFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: start World Feed Activity; for now, will do nothing
            }
        });

        newPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewPostActivity();
            }
        });

        myProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMyProfileActivity();
            }
        });

        snackBustingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSnackBustingActivity();
            }
        });


        setContentView(view);
    }

    /*
    // Helper functions:
    */

    // Starts New Post Activity
    private void startNewPostActivity() {
        Intent intent = new Intent(this, NewPostActivity.class);
        startActivity(intent);
    }

    // Restarts My Profile Activity (current activity, will just reload page)
    private void startMyProfileActivity() {
        Intent intent = new Intent(this, PersonalProfileActivity.class);
        startActivity(intent);
    }

    // Starts Snack Busting Activity
    private void startSnackBustingActivity() {
        Intent intent = new Intent(this, SnackBustingActivity.class);
        startActivity(intent);
    }
}