package edu.neu.madcourse.stashbusters.views;

import android.content.Context;
import android.content.Intent;
import android.view.View;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.contracts.PostContract;
import edu.neu.madcourse.stashbusters.presenters.PostPresenter;

public class PanelPostActivity extends PostActivity implements PostContract.MvpView {

    @Override
    public void setRefs() {
        authorUserRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(authorId);
        postRef = FirebaseDatabase.getInstance().getReference().child("panelPosts")
                .child(authorId).child(postId);

    }

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
        commentsSection = binding.commentRecyclerView;

        commentInput.setHint(R.string.advice_hint);
        swapSection.setVisibility(View.GONE);

        mPresenter.loadCommentDataToView(this);
    }

    @Override
    public void onUsernameClick(final Context context) {
        userView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check to see if the author user is the same as the current user
                FirebaseUser currentUser = mAuth.getCurrentUser();
                String currentUserId = currentUser.getUid();
                if (authorId.equals(currentUserId)) {
                    // If current user, take user to their personal profile
                    Intent intent = new Intent(context, PersonalProfileActivity.class);
                    context.startActivity(intent);
                } else {
                    // Send user to author user's public profile
                    Intent intent = new Intent(context, PublicProfileActivity.class);
                    intent.putExtra("userId", authorId);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onSwapButtonClick() {
        // No swap button for this activity
    }

    @Override
    public void setPresenter() {
        mPresenter = new PostPresenter(this, authorId, postId);
        mPresenter.loadAuthorDataToView();
        mPresenter.loadPostDataToView();
    }
}
