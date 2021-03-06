package edu.neu.madcourse.stashbusters.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import edu.neu.madcourse.stashbusters.adapters.PostAdapter;
import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.contracts.ProfileContract;
import edu.neu.madcourse.stashbusters.databinding.PublicProfileActivityBinding;
import edu.neu.madcourse.stashbusters.presenters.PublicProfilePresenter;

public class PublicProfileActivity extends AppCompatActivity implements ProfileContract.MvpView {
    private static final String TAG = PublicProfileActivity.class.getSimpleName();

    // Set up ViewBinding for the layout
    private PublicProfileActivityBinding binding;
    private ImageView profilePic;
    private TextView usernameView, followerCountView, bio, profileTitle;
    private Button followButton;
    private RecyclerView postListRecyclerView;
    private NavigationBarView navigationBarView;

    private PublicProfilePresenter mPresenter;
    private FirebaseAuth mAuth;
    private DatabaseReference followRef;
    private String targetUserId, currentUserId; //this profile's owner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "OnCreated();");

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        targetUserId = intent.getStringExtra("userId");

        mPresenter = new PublicProfilePresenter(this, targetUserId);
        mPresenter.loadDataToView();

        followRef = FirebaseDatabase.getInstance().getReference().child("follows");

        // Setting up binding instance and view instances
        binding = PublicProfileActivityBinding.inflate(getLayoutInflater());

        initViews();
        initRecyclerView();
        initListeners();
        checkFollowingState();

        setContentView(binding.getRoot());
        mPresenter.getUserPostsData();
    }

    private void initViews() {
        usernameView = binding.usernameDisplay;
        profilePic = binding.profilePicture;
        followerCountView = binding.followerCount;
        bio = binding.bio;
        followButton = binding.followButton;
        profileTitle = binding.profileTitle;

        // Navigation bar setup:
        navigationBarView = binding.navigationBar;
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
        // Setting up onClickListener for Follow Button - should switch to "Followed" and a darker
        // color if the user is following this profile
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buttonText = followButton.getText().toString();
                mPresenter.onFollowButtonClick(buttonText);
            }
        });
    }

    @Override
    public void setViewData(String photoUrl, String inputUsername, String inputBio, String inputFollowerCount) {
        Picasso.get().load(photoUrl).into(profilePic);
        usernameView.setText(inputUsername);
        bio.setText(inputBio);
        followerCountView.setText(getString(R.string.follower_count, inputFollowerCount));
        profileTitle.setText(new StringBuilder().append(inputUsername).append(getString(R.string.profile_owner)).toString());
    }

    /**
     * Check whether current user is following target user. If so, update button text accordingly.
     */
    private void checkFollowingState() {
        DatabaseReference currentUserFollowingRef = followRef
                .child(currentUserId)
                .child("following");

        currentUserFollowingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(targetUserId)) {
                    // already following, change button text
                    updateFollowButton("following");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }
        });
    }

    public void updateFollowButton(String text) {
        String following = this.getResources().getString(R.string.following_text);
        String follow = this.getResources().getString(R.string.follow_text);
        if (text.equals(follow)) {
            // update button to reflect unfollowing a user
            followButton.setText(follow);
            followButton.setBackgroundColor(ContextCompat.getColor(this,
                    R.color.colorPrimary));
        } else {
            // update button to reflect following a user
            followButton.setText(following);
            followButton.setBackgroundColor(Color.GRAY);
        }

    }

    @Override
    public void setPostListAdapter(PostAdapter adapter) {
        postListRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showNoPostText(String inputMsg) {
        // do nothing in public profile
    }

    /**
     * If this activity was opened from a notification,
     * set back stack so back button goes to World Feed.
     */
    @Override
    public void onBackPressed() {
        Intent thisIntent = getIntent();
        if (thisIntent.getExtras()!= null) {
            if (thisIntent.getExtras().containsKey("LAUNCHED_BY_NOTIFICATION")){
                Intent intent = new Intent(this, WorldFeedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}