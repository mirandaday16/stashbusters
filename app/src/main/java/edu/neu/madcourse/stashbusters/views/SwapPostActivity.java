package edu.neu.madcourse.stashbusters.views;

import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.contracts.SwapPostContract;
import edu.neu.madcourse.stashbusters.presenters.SwapPostPresenter;

public class SwapPostActivity extends PostActivity implements SwapPostContract.MvpView {

    @Override
    public void initViews() {
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

        commentInput.setHint(R.string.advice_hint);
    }

    @Override
    public void setRefs() {
        authorUserRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(authorId);
        postRef = FirebaseDatabase.getInstance().getReference().child("swapPosts")
                .child(authorId).child(postId);

    }

    @Override
    public void setPresenter() {
        mPresenter = new SwapPostPresenter(this, authorId, postId);
        mPresenter.loadAuthorDataToView();
        mPresenter.loadPostDataToView();
    }

    @Override
    public void setPostViewData(String title, String postPicUrl, String description,
                                long createdDate, String material, Boolean isAvailable) {
        titleView.setText(title);
        Picasso.get().load(postPicUrl).into(postPhoto);
        details.setText(description);

        // Swap Section
        TextView swapMaterial = binding.swapItem;
        if (isAvailable) {
            swapMaterial.setText(material);
        }
        else {
            swapMaterial.setText(this.getString(R.string.swapped));
        }

        // Format time stamp
        Date date = new Date(createdDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
        String dateText = dateFormat.format(date);
        timeStamp.setText(dateText);

    }
}
