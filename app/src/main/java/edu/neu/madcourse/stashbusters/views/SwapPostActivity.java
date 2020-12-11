package edu.neu.madcourse.stashbusters.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.contracts.SwapPostContract;
import edu.neu.madcourse.stashbusters.model.Comment;
import edu.neu.madcourse.stashbusters.presenters.SwapPostPresenter;

/**
 * Handles UI for swap posts.
 */
public class SwapPostActivity extends PostActivity implements SwapPostContract.MvpView {

    @Override
    public void initViews() {
        userView = binding.user;
        userPic = binding.profilePic;
        usernameView = binding.username;
        titleView = binding.title;
        postPhoto = binding.photo;
        details = binding.details;
        timeStamp = binding.timeStamp;
        swapSection = binding.swapFor;
        swapButton = binding.swapButton;
        commentInput = binding.commentInput;
        submitButton = binding.postButton;
        likeCountView = binding.numLikes;
        heartIcon = binding.heart;
        more = binding.more;

        commentsSection = binding.commentRecyclerView;

        // Check whether post belongs to current user; if so, swap button should be visible
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();
        if (authorId.equals(currentUserId)) {
            swapButton.setVisibility(View.VISIBLE);
        } else {
            swapButton.setVisibility(View.GONE);
        }

        commentInput.setHint(R.string.swap_hint);

        mPresenter.loadCommentDataToView(this);
        initListeners();
    }

    @Override
    public void onSwapButtonClick() {
        swapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check to see if the button is visible
                if (swapButton.getVisibility() == View.VISIBLE) {
                    // Swap completion button is visible and can be used to mark swaps as complete
                    // Now, check to see if swap is already marked as complete:
                    if (swapButton.getText().equals(getResources().getString(R.string.mark_swap_as_complete))) {
                        // Swap is not yet completed; click marks it as complete
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("availability", false);
                        postRef.updateChildren(updates);
                        // Change button appearance
                        swapButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                        swapButton.setText(getResources().getString(R.string.mark_swap_as_available));
                    } else {
                        // Otherwise, swap is already marked as complete; click marks as incomplete
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("availability", true);
                        postRef.updateChildren(updates);
                        // Change button appearance
                        swapButton.setBackgroundColor(getResources().getColor(R.color.colorAccentDark));
                        swapButton.setText(getResources().getString(R.string.mark_swap_as_complete));
                    }
                }
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
            // Mark swap as complete - change text and color of swap section
            swapMaterial.setText(this.getString(R.string.swapped));
            swapSection.setBackgroundColor(getResources().getColor(R.color.colorSurfaceDark));
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
