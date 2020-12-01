package edu.neu.madcourse.stashbusters.views;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.stashbusters.contracts.PostContract;
import edu.neu.madcourse.stashbusters.databinding.ActivityPanelSwapPostBinding;
import edu.neu.madcourse.stashbusters.presenters.PostPresenter;

public class PanelPostActivity extends PostActivity implements PostContract.MvpView {
    private static final String TAG = PanelPostActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        authorId = ""; // TODO: Figure out how to get author ID and post ID info here!!
        postId = "";

        authorUserRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child("authorId");
        postRef = FirebaseDatabase.getInstance().getReference().child("panelPosts")
                .child("authorId").child("postId");

        mPresenter = new PostPresenter(this, authorId, postId);
        mPresenter.loadAuthorDataToView();
        mPresenter.loadPostDataToView();


        // Setting up binding instance and view instances
        binding = ActivityPanelSwapPostBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        swapSection.setVisibility(View.GONE);

        initViews();
        initListeners();

        setContentView(rootView);
    }
}
