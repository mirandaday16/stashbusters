package edu.neu.madcourse.stashbusters.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import edu.neu.madcourse.stashbusters.adapters.CommentRVAdapter;
import edu.neu.madcourse.stashbusters.contracts.PostContract;
import edu.neu.madcourse.stashbusters.databinding.ActivityPanelSwapPostBinding;
import edu.neu.madcourse.stashbusters.presenters.PostPresenter;
import edu.neu.madcourse.stashbusters.R;

/**
 * Abstract class that represents the post detail activity.
 * Extended by {@link SwapPostActivity} and {@link PanelPostActivity}
 */
public abstract class PostActivity extends AppCompatActivity implements PostContract.MvpView {
    public static final String LIKED_POSTS = "liked posts";
    public static final String MY_POSTS = "my posts";

    protected PostPresenter mPresenter;
    protected FirebaseAuth mAuth;
    protected String authorId, postId;
    protected DatabaseReference authorUserRef;
    protected DatabaseReference postRef;

    // Set up ViewBinding for the layout
    protected ActivityPanelSwapPostBinding binding;

    // Views
    protected ConstraintLayout userView;
    protected ImageView userPic;
    protected TextView usernameView;
    protected ImageView likedIcon;
    protected TextView titleView;
    protected ImageView postPhoto;
    protected TextView details;
    protected EditText commentInput;
    protected TextView timeStamp;
    protected TextView likeCountView;
    protected ImageView heartIcon;
    protected LinearLayout swapSection;
    protected Button submitButton;
    protected RecyclerView commentsSection;

    protected boolean currentUserLikedPost = false;

    // Attributes needed for displaying comments in recycler view.
    LinearLayoutManager layoutManager;

    // For updating ImageView in a separate thread.
    private Handler imageHandler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        authorId = intent.getStringExtra("userId");
        postId = intent.getStringExtra("postId");

        mAuth = FirebaseAuth.getInstance();

        setRefs();
        setPresenter();

        // Setting up binding instance and view instances
        binding = ActivityPanelSwapPostBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();

        initViews();
        initRecyclerView();
        setContentView(rootView);

    }

    public abstract void setRefs();

    public abstract void setPresenter();

    public abstract void initViews();


    @Override
    public boolean getCurrentUserLikedPostStatus() {
        return this.currentUserLikedPost;
    }

    public void initRecyclerView() {
        commentsSection = binding.commentRecyclerView;
        commentsSection.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        commentsSection.setLayoutManager(layoutManager);
    }

    @Override
    public void setCurrentUserLikedPostStatus(boolean likeStatus) {
        this.currentUserLikedPost = likeStatus;
    }

    @Override
    public void setAuthorViewData(String username, String profilePicUrl) {
        usernameView.setText(username);
        Picasso.get().load(profilePicUrl).into(userPic);
    }

    public void setNewLikeCount(long newLikeCount) {
        String likeCountText = String.format(getResources().getString(R.string.like_count), newLikeCount);
        likeCountView.setText(likeCountText);
    }

    public void updateHeartIconDisplay(boolean status) {
        if (status) {
            heartIcon.setImageResource(R.drawable.heart_icon_filled);
        } else {
            heartIcon.setImageResource(R.drawable.heart_icon_empty);
        }
    }

    @Override
    public void setCommentAdapter(CommentRVAdapter commentsAdapter) {
        commentsSection.setAdapter(commentsAdapter);
    }
}
