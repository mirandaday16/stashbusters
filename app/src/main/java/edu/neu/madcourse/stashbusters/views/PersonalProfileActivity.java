package edu.neu.madcourse.stashbusters.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import edu.neu.madcourse.stashbusters.FeedRecyclerAdapter;
import edu.neu.madcourse.stashbusters.PostAdapter;
import edu.neu.madcourse.stashbusters.PostsViewHolder;
import edu.neu.madcourse.stashbusters.contracts.PersonalProfileContract;
import edu.neu.madcourse.stashbusters.databinding.PersonalProfileActivityBinding;
import edu.neu.madcourse.stashbusters.model.Post;
import edu.neu.madcourse.stashbusters.model.StashPanelPost;
import edu.neu.madcourse.stashbusters.enums.NavigationBarButtons;
import edu.neu.madcourse.stashbusters.presenters.PersonalProfilePresenter;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for the UI of a user's profile page and sending data to {@link PersonalProfilePresenter}
 * when there are user interactions.
 */
public class PersonalProfileActivity extends AppCompatActivity implements PersonalProfileContract.MvpView {
    private static final String TAG = PersonalProfileActivity.class.getSimpleName();

    // Set up UI elements
    private PersonalProfileActivityBinding binding;
    TextView username, followerCountView, bio;
    ImageView profilePic;
    private NavigationBarView navigationBarView;
    Button myPostsButton, likedPostsButton;
    Toolbar toolbar;
    RecyclerView postListRecyclerView;

    private PersonalProfilePresenter mPresenter;
    private FirebaseAuth mAuth;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initAuthentication();

        binding = PersonalProfileActivityBinding.inflate(getLayoutInflater());
        initViews();
        initRecyclerView();
        initListeners();

        // set up presenter + load data to view
        mPresenter = new PersonalProfilePresenter(this, userId);
        mPresenter.loadDataToView();

        setContentView(binding.getRoot());

        mPresenter.getUserPostsData();
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
    private void initViews() {
        toolbar = binding.profilePageToolbar;

        // Setting up UI elements
        username = binding.usernameDisplay;
        profilePic = binding.profilePicture;
        followerCountView = binding.followerCount;
        bio = binding.bio;
        myPostsButton = binding.myPosts;
        likedPostsButton = binding.likedPosts;

        // Navigation bar setup:
        navigationBarView = binding.navigationBar;
        navigationBarView.setSelected(NavigationBarButtons.MYPROFILE);

    }

    private void initRecyclerView(){
        // recycler view for posts
        postListRecyclerView = binding.postViewArea;
        postListRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postListRecyclerView.setLayoutManager(linearLayoutManager);
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
                //
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
    public void setPostListAdapter(PostAdapter adapter) {
        postListRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setViewData(String photoUrl, String inputUsername, String inputBio, String inputFollowerCount) {
        Picasso.get().load(photoUrl).into(profilePic);
        username.setText(inputUsername);
        bio.setText(inputBio);
        followerCountView.setText(inputFollowerCount + " followers");
    }
}