package edu.neu.madcourse.stashbusters.presenters;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.contracts.PublicProfileContract;

public class PublicProfilePresenter extends ProfilePresenter {
    private static final String TAG = PublicProfilePresenter.class.getSimpleName();

    private PublicProfileContract.MvpView mView;
    // targetUserId is the owner of this profile
    private String targetUserId, currentUserId;

    private FirebaseAuth mAuth;
    private DatabaseReference followRef;

    public PublicProfilePresenter(Context context, String targetUserId) {
        super(context, targetUserId);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        followRef = FirebaseDatabase.getInstance().getReference().child("follows");
    }

    public void onFollowButtonClick(String buttonText) {
        // if button text is following -- unfollow
        // else, text is follow -- follow
        if (buttonText.toLowerCase().equals("follow")) {
            Log.i(TAG, "onFollowButtonClick:Following");

            followUser();
            mView.updateFollowButton(mContext.getResources().getString(R.string.following_text));
        } else {
            Log.i(TAG, "onFollowButtonClick:Unfollowing");

            unfollowUser();
            mView.updateFollowButton(mContext.getResources().getString(R.string.follow_text));
        }
    }

    private void followUser() {
        // Follow collection contains a list of key which is user ID. Each key identifies a user's
        // record of followers and following

        // add target user to list of current user's following
        followRef.child(currentUserId).child("following").child(targetUserId).setValue(true);
        // add current user to list of target user's follower
        followRef.child(targetUserId).child("followers").child(currentUserId).setValue(true);
    }

    private void unfollowUser() {
        followRef.child(currentUserId).child("following").child(targetUserId).removeValue();
        followRef.child(targetUserId).child("followers").child(currentUserId).removeValue();
    }


}
