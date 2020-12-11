package edu.neu.madcourse.stashbusters.presenters;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.contracts.PublicProfileContract;
import edu.neu.madcourse.stashbusters.utils.Utils;

public class PublicProfilePresenter extends ProfilePresenter {
    private static final String TAG = PublicProfilePresenter.class.getSimpleName();

    // targetUserId is the owner of this profile
    private String targetUserId, currentUserId;

    private FirebaseAuth mAuth;
    private DatabaseReference followRef, userRef;

    public PublicProfilePresenter(Context context, String targetUserId) {
        super(context, targetUserId);
        this.targetUserId = targetUserId;

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        followRef = FirebaseDatabase.getInstance().getReference().child("follows");
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
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

        updateUserCount("follow");

        // Send notification to followed user.
        Utils.startNotification("follow", currentUserId, targetUserId, currentUserId, "");
    }

    private void unfollowUser() {
        followRef.child(currentUserId).child("following").child(targetUserId).removeValue();
        followRef.child(targetUserId).child("followers").child(currentUserId).removeValue();

        updateUserCount("unfollow");
    }

    private void updateUserCount(final String followStatus) {
        final DatabaseReference targetUserRef = userRef.child(targetUserId);
        targetUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long currentFollowCount = (long) snapshot.child("followerCount").getValue();
                if (followStatus.equals("unfollow")){
                    targetUserRef.child("followerCount").setValue(currentFollowCount - 1);
                } else {
                    targetUserRef.child("followerCount").setValue(currentFollowCount + 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }
        });
    }
}
