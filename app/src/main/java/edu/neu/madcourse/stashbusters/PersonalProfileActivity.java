package edu.neu.madcourse.stashbusters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import edu.neu.madcourse.stashbusters.databinding.PersonalProfileActivityBinding;

public class PersonalProfileActivity extends AppCompatActivity {

    // Set up ViewBinding for the layout
    private PersonalProfileActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_profile_activity);

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
}