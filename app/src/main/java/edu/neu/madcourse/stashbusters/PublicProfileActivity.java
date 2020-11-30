package edu.neu.madcourse.stashbusters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import edu.neu.madcourse.stashbusters.databinding.PublicProfileActivityBinding;
import edu.neu.madcourse.stashbusters.views.PersonalProfileActivity;

public class PublicProfileActivity extends AppCompatActivity {

    // Set up ViewBinding for the layout
    private PublicProfileActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting up binding instance and view instances
        binding = PublicProfileActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        final TextView usernameView = binding.usernameDisplay;
        final ImageView profilePic = binding.profilePicture;
        final TextView followerCountView = binding.followerCount;
        final TextView bio = binding.bio;
        final Button followButton = binding.followButton;
        final RecyclerView userPostsFeed = binding.postViewArea;

        // Navigation bar buttons:
        final ImageButton myFeedButton = binding.myFeed;
        final ImageButton worldFeedButton = binding.worldFeed;
        final ImageButton newPostButton = binding.newPost;
        final ImageButton myProfileButton = binding.myProfile;
        final ImageButton snackBustingButton = binding.snackBusting;

        // Setting up onClickListener for Follow Button - should switch to "Followed" and a darker
        // color if the user is following this profile
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Change color/text depending on following status (get from Firebase)
                // TODO: When clicked, edit following status in Firebase (either add or remove user
                //  from following list)
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