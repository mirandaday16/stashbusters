package edu.neu.madcourse.stashbusters.views;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.contracts.SwapPostContract;
import edu.neu.madcourse.stashbusters.model.Comment;
import edu.neu.madcourse.stashbusters.presenters.SwapPostPresenter;

/**
 * Handles UI for swap posts.
 */
public class SwapPostActivity extends PostActivity implements SwapPostContract.MvpView {
    protected SwapPostPresenter mPresenter;

    @Override
    public void initViews() {
        userView = binding.user;
        userPic = binding.profilePic;
        usernameView = binding.username;
        likedIcon = binding.liked;
        titleView = binding.title;
        postPhoto = binding.photo;
        details = binding.details;
        timeStamp = binding.timeStamp;
        swapSection = binding.swapFor;
        commentInput = binding.commentInput;
        submitButton = binding.postButton;
        likeCountView = binding.numLikes;
        heartIcon = binding.heart;

        commentsSection = binding.commentRecyclerView;

        commentInput.setHint(R.string.swap_hint);

        mPresenter.loadCommentDataToView(this);
        initListeners();
    }

    public void initListeners() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new Comment object
                String commentText = commentInput.getText().toString();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                Comment comment = new Comment(commentText);
                comment.setAuthorId(currentUser.getUid());
                mPresenter.startCommentNotification("commentSwap", authorId, postId);
                // Check that the user has entered a comment in the EditText field
                if (commentText != null) {
                    mPresenter.uploadComment(postRef, comment);
                    // Reset comment field and update RecyclerView so user can see their comment
                    commentInput.setText("");
                    // TODO: Hide soft keyboard and update RecyclerView
                }
            }
        });

        userView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check to see if the author user is the same as the current user
                FirebaseUser currentUser = mAuth.getCurrentUser();
                String currentUserId = currentUser.getUid();
                if (authorId.equals(currentUserId)) {
                    // If current user, take user to their personal profile
                    Intent intent = new Intent(SwapPostActivity.this, PersonalProfileActivity.class);
                    startActivity(intent);
                } else {
                    // Send user to author user's public profile
                    Intent intent = new Intent(SwapPostActivity.this, PublicProfileActivity.class);
                    intent.putExtra("userId", authorId);
                    startActivity(intent);
                }
            }
        });

        heartIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mPresenter.onHeartIconClick(postRef, "likeSwap", authorId, postId);
            }
        });
    }

    @Override
    public void setRefs() {
        authorUserRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(authorId);
        postRef = FirebaseDatabase.getInstance().getReference().child("swapPosts")
                .child(authorId).child(postId);

//        super.initListeners(postRef);
    }

    @Override
    public void setPresenter() {
        mPresenter = new SwapPostPresenter(this, authorId, postId);
        mPresenter.loadAuthorDataToView();
        mPresenter.loadPostDataToView();
    }

    @Override
    public void setPostViewData(String title, String postPicUrl, String description,
                                long createdDate, String material, Boolean isAvailable,
                                long likeCount) {
        titleView.setText(title);
        Picasso.get().load(postPicUrl).into(postPhoto);
        details.setText(description);

        // Swap Section
        TextView swapMaterial = binding.swapItem;
        if (isAvailable) {
            swapMaterial.setText(material);
        } else {
            swapMaterial.setText(this.getString(R.string.swapped));
        }

        // Format time stamp
        Date date = new Date(createdDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
        String dateText = dateFormat.format(date);
        timeStamp.setText(dateText);

        setNewLikeCount(likeCount);

        // set heart state
        mPresenter.checkLikeStatus();
    }

}
