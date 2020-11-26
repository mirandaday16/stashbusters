package edu.neu.madcourse.stashbusters.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import edu.neu.madcourse.stashbusters.contracts.ProfileContract;
import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.databinding.ProfileActivityBinding;
import edu.neu.madcourse.stashbusters.presenters.ProfilePresenter;

import com.squareup.picasso.Picasso;

/**
 * Responsible for the UI of a user's profile page and sending data to {@link ProfilePresenter}
 * when there are user interactions.
 */
public class ProfileActivity extends AppCompatActivity implements ProfileContract.MvpView {
    private static final String TAG = ProfileActivity.class.getSimpleName();

    // Set up ViewBinding for the layout
    private ProfileActivityBinding binding;
    private ProfilePresenter mPresenter;
    RecyclerView postsView;

    //user info
    private String userId;
    TextView username, followerCountView, bio;
    ImageView profilePic;
    Button myPostsButton, likedPostsButton, editProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "OnCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        // load userId in intent passed from Main Activity
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        Log.i(TAG, "Logged In as " + userId);

        // set up presenter + load data to view
        mPresenter = new ProfilePresenter(this, userId);
        mPresenter.loadDataToView();

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
        getSupportActionBar().hide();

        // Setting up UI elements
        username = binding.usernameDisplay;
        profilePic = binding.profilePicture;
        followerCountView = binding.followerCount;
        bio = binding.bio;
        myPostsButton = binding.myPosts;
        likedPostsButton = binding.likedPosts;
        postsView = binding.postViewArea;
        editProfileButton = binding.editProfile;

        // TODO: Set onClickListener for toolbar menu -- these should be moved to Presenter

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
    public void setViewData(String photoUrl, String inputUsername, String inputBio, String inputFollowerCount) {
        Picasso.get().load(photoUrl).into(profilePic);
        username.setText(inputUsername);
        bio.setText(inputBio);
        followerCountView.setText(inputFollowerCount + " followers");
    }

    @Override
    public void setEditProfileBtnVisibility(int type) {
        editProfileButton.setVisibility(type);
    }

    @Override
    public void setMyPostBtnVisibility(int type) {
        myPostsButton.setVisibility(type);
    }
}