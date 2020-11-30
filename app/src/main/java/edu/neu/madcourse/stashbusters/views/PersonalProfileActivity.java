package edu.neu.madcourse.stashbusters.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import edu.neu.madcourse.stashbusters.contracts.PersonalProfileContract;
import edu.neu.madcourse.stashbusters.databinding.PersonalProfileActivityBinding;
import edu.neu.madcourse.stashbusters.presenters.PersonalProfilePresenter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

/**
 * Responsible for the UI of a user's profile page and sending data to {@link PersonalProfilePresenter}
 * when there are user interactions.
 */
public class PersonalProfileActivity extends AppCompatActivity implements PersonalProfileContract.MvpView {
    private static final String TAG = PersonalProfileActivity.class.getSimpleName();

    // Set up ViewBinding for the layout
    private PersonalProfileActivityBinding binding;
    private PersonalProfilePresenter mPresenter;
    RecyclerView postsView;
    TextView username, followerCountView, bio;
    ImageView profilePic;
    ImageButton myFeedButton, worldFeedButton, newPostButton, myProfileButton, snackBustingButton;
    Button myPostsButton, likedPostsButton, editProfileButton;
    Toolbar toolbar;

    private FirebaseAuth mAuth;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initAuthentication();

        binding = PersonalProfileActivityBinding.inflate(getLayoutInflater());
        initViews(binding);
        initListeners();

        // set up presenter + load data to view
        mPresenter = new PersonalProfilePresenter(this, userId);
        mPresenter.loadDataToView();

        setContentView(binding.getRoot());
    }

    private void initAuthentication() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            // raise error
            Log.e(TAG, "initAuthentication:FAILED - User not authenticated");
        } else {
            userId = user.getUid();
        }
    }

    /**
     * Initialize the view and set up all UI elements.
     */
    private void initViews(PersonalProfileActivityBinding binding) {
        toolbar = binding.profilePageToolbar;

        // Setting up UI elements
        username = binding.usernameDisplay;
        profilePic = binding.profilePicture;
        followerCountView = binding.followerCount;
        bio = binding.bio;
        myPostsButton = binding.myPosts;
        likedPostsButton = binding.likedPosts;
        postsView = binding.postViewArea;
        editProfileButton = binding.editProfile;

        // TODO: might want to separate toolbar out to be reused
        // Navigation bar buttons
        myFeedButton = binding.myFeed;
        worldFeedButton = binding.worldFeed;
        newPostButton = binding.newPost;
        myProfileButton = binding.myProfile;
        snackBustingButton = binding.snackBusting;
    }

    private void initListeners() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return mPresenter.onToolbarClick(item);
            }
        });

        // Setting onClickListener for My Posts Button - switches RecyclerView to user's own posts
        myPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: get user's posts from Firebase and display in RecyclerView
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onEditProfileButtonClick(userId);
            }
        });
        // TODO: Set onClickListener for toolbar menu -- these should be moved to Presenter

        // Setting onClickListener for My Posts Button - switches RecyclerView to user's own posts
        myPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send to presenter
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
                mPresenter.onNewPostButtonClick();
            }
        });

        myProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onMyProfileButtonClick();
            }
        });

        snackBustingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onSnackBustingButtonClick();
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
}