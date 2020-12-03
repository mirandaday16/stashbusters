package edu.neu.madcourse.stashbusters.views;

import com.google.firebase.database.FirebaseDatabase;

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
    public void setPresenter() {
        mPresenter = new PostPresenter(this, authorId, postId);
        mPresenter.loadAuthorDataToView();
        mPresenter.loadPostDataToView();
    }
}
