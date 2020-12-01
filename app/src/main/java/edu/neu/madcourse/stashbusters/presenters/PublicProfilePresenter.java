package edu.neu.madcourse.stashbusters.presenters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.neu.madcourse.stashbusters.NewPostActivity;
import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.SnackBustingActivity;
import edu.neu.madcourse.stashbusters.contracts.PublicProfileContract;
import edu.neu.madcourse.stashbusters.views.PersonalProfileActivity;

public class PublicProfilePresenter implements PublicProfileContract.Presenter {
    private static final String TAG = PublicProfilePresenter.class.getSimpleName();

    private PublicProfileContract.MvpView mView;
    private Context mContext;
    // targetUserId is the owner of this profile
    private String targetUserId, currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference targetUserProfileRef;
    private DatabaseReference followRef;

    public PublicProfilePresenter(Context context, String targetUserId) {
        this.mContext = context;
        this.mView = (PublicProfileContract.MvpView) context;
        this.targetUserId = targetUserId;

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        targetUserProfileRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(targetUserId);
        followRef = FirebaseDatabase.getInstance().getReference().child("follows");
    }

    @Override
    public void loadDataToView() {
        //load data of target user (might not be current user)
        targetUserProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // default photo is mouse icon
                    String photoUrl = snapshot.child("photoUrl").getValue().toString();
                    String username = snapshot.child("username").getValue().toString();
                    String bio = snapshot.child("bio").getValue().toString();
                    String followerCount = snapshot.child("followerCount").getValue().toString();
                    mView.setViewData(photoUrl, username, bio, followerCount);

                    Log.i(TAG, "loadDataToView:success");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }
        });
    }

    @Override
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

    @Override
    public void onSnackBustingButtonClick() {
        startSnackBustingActivity();
    }

    @Override
    public void onMyProfileButtonClick() {
        startMyProfileActivity();
    }

    @Override
    public void onNewPostButtonClick() {
        startNewPostActivity();
    }

    // Starts New Post Activity
    private void startNewPostActivity() {
        Intent intent = new Intent(this.mContext, NewPostActivity.class);
        mContext.startActivity(intent);
    }

    // Restarts My Profile Activity (current activity, will just reload page)
    private void startMyProfileActivity() {
        Intent intent = new Intent(this.mContext, PersonalProfileActivity.class);
        mContext.startActivity(intent);
    }

    // Starts Snack Busting Activity
    private void startSnackBustingActivity() {
        Intent intent = new Intent(this.mContext, SnackBustingActivity.class);
        mContext.startActivity(intent);
    }
}
