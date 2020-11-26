package edu.neu.madcourse.stashbusters.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import edu.neu.madcourse.stashbusters.ProfileSetup;
import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.databinding.ProfileActivityBinding;
import edu.neu.madcourse.stashbusters.presenters.ProfilePresenter;

import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity implements ProfileSetup.View {

    // Set up ViewBinding for the layout
    private ProfileActivityBinding binding;
    private ProfilePresenter presenter;
    RecyclerView postsView;

    //user info
    private String userId;
    TextView username, followerCountView, bio;
    ImageView profilePic;
    Button myPostsButton, likedPostsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        // load userId in intent passed from Main Activity
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        // set up presenter + load data to view
        presenter = new ProfilePresenter(this, userId);
        presenter.loadDataToView();

        binding = ProfileActivityBinding.inflate(getLayoutInflater());
        final Toolbar toolbar = binding.profilePageToolbar;
        View view = binding.getRoot();

        initViews(binding);

        setContentView(view);
    }

    /**
     * Initialize the view and set up all UI elements.
     */
    private void initViews(ProfileActivityBinding binding) {
        // Setting up UI elements
        username = binding.usernameDisplay;
        profilePic = binding.profilePicture;
        followerCountView = binding.followerCount;
        bio = binding.bio;
        myPostsButton = binding.myPosts;
        likedPostsButton = binding.likedPosts;
        postsView = binding.postViewArea;

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
    }

    @Override
    public void setViewData(String photoUrl, String inputUsername, String inputBio) {
        Picasso.get().load(photoUrl).into(profilePic);
        username.setText(inputUsername);
        bio.setText(inputBio);
    }
}