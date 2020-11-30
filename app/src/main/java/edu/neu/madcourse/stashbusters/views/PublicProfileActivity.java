package edu.neu.madcourse.stashbusters.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import edu.neu.madcourse.stashbusters.contracts.PublicProfileContract;
import edu.neu.madcourse.stashbusters.databinding.PublicProfileActivityBinding;
import edu.neu.madcourse.stashbusters.presenters.PublicProfilePresenter;

public class PublicProfileActivity extends AppCompatActivity implements PublicProfileContract.MvpView {
    private static final String TAG = PublicProfileActivity.class.getSimpleName();

    // Set up ViewBinding for the layout
    private PublicProfileActivityBinding binding;
    private ImageView profilePic;
    private TextView usernameView, followerCountView, bio;
    private Button followButton;
    private RecyclerView userPostsFeed;
    //nav
    private ImageButton myFeedButton, worldFeedButton, newPostButton, myProfileButton, snackBustingButton;

    private PublicProfilePresenter mPresenter;
    private String targetUserId; //this profile's owner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        targetUserId = intent.getStringExtra("targetUserId");

        mPresenter = new PublicProfilePresenter(this, targetUserId);
        mPresenter.loadDataToView();

        // Setting up binding instance and view instances
        binding = PublicProfileActivityBinding.inflate(getLayoutInflater());

        initViews();
        initListeners();

        setContentView(binding.getRoot());
    }

    private void initViews() {
        usernameView = binding.usernameDisplay;
        profilePic = binding.profilePicture;
        followerCountView = binding.followerCount;
        bio = binding.bio;
        followButton = binding.followButton;
        userPostsFeed = binding.postViewArea;

        // Navigation bar buttons:
        myFeedButton = binding.myFeed;
        worldFeedButton = binding.worldFeed;
        newPostButton = binding.newPost;
        myProfileButton = binding.myProfile;
        snackBustingButton = binding.snackBusting;
    }

    private void initListeners() {
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
        usernameView.setText(inputUsername);
        bio.setText(inputBio);
        followerCountView.setText(inputFollowerCount + " followers");
    }
}