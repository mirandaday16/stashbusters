package edu.neu.madcourse.stashbusters.views;

import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.stashbusters.contracts.PostContract;

public class PanelPostActivity extends PostActivity implements PostContract.MvpView {

    @Override
    public void setRefs() {
        authorUserRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(authorId);
        postRef = FirebaseDatabase.getInstance().getReference().child("panelPosts")
                .child(authorId).child(postId);

    }
}
