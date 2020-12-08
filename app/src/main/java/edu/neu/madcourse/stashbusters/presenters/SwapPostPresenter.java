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

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.stashbusters.contracts.SwapPostContract;

public class SwapPostPresenter extends PostPresenter {
    private static final String TAG = SwapPostPresenter.class.getSimpleName();

    private SwapPostContract.MvpView mView;
    private Context mContext;

    private String authorId, postId, currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference postRef, userLikesRef;
    private DatabaseReference authorUserRef;

    public SwapPostPresenter(Context context, String authorId, String postId) {
        super(context, authorId, postId);
        this.mView = (SwapPostContract.MvpView) context;
        this.postId = postId;

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        postRef = FirebaseDatabase.getInstance().getReference()
                .child("swapPosts").child(authorId).child(postId);
        authorUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(authorId);
        userLikesRef = FirebaseDatabase.getInstance().getReference().child("userLikes");
    }


    @Override
    public void loadPostDataToView() {
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Load in post info
                    String title = snapshot.child("title").getValue().toString();
                    String postPicUrl = snapshot.child("photoUrl").getValue().toString();
                    String description = snapshot.child("description").getValue().toString();
                    long createdDate = (long) snapshot.child("createdDate").getValue();
                    String material = snapshot.child("material").getValue().toString();
                    Boolean isAvailable = (Boolean) snapshot.child("availability").getValue();
                    long likeCount = (long) snapshot.child("likeCount").getValue();
                    mView.setPostViewData(title,
                                    postPicUrl,
                                    description,
                                    createdDate,
                                    material,
                                    isAvailable,
                                    likeCount);

                    Log.i(TAG, "loadPostDataToView:success");
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){
                Log.e(TAG, error.toString());
            }

        });
    }

    /**
     * At the beginning when the post first opened, check current user's like status with the post.
     * If user liked the post, update heart icon accordingly and set state of the like status.
     */
    public void checkLikeStatus() {
        userLikesRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mView.updateHeartIconDisplay(true);
                    mView.setCurrentUserLikedPostStatus(true);
                } else {
                    // have not liked
                    mView.updateHeartIconDisplay(false);
                    mView.setCurrentUserLikedPostStatus(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onHeartIconClick() {
        // check current like status, if already liked, unlike post + remove from DB.
        // else, like post and add to DB
        boolean likeStatus = mView.getCurrentUserLikedPostStatus();
        System.out.println("LIKE STATUS " + likeStatus);

        if (likeStatus) {
            System.out.println("UNLIKING POST");

            // already liked, clicking heart icon again to unlike post
            unlikePost();
        } else {
            System.out.println("LIKING POST");
            // not liked yet, clicking heart icon to like post
            likePost();
        }


        // call view to update to filled heart
    }



    private void likePost() {
        // increment count + call view to reflect
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long currLikeCount = (long) snapshot.child("likeCount").getValue();
                    updatePost(currLikeCount + 1);
                    mView.setNewLikeCount(currLikeCount + 1);
                    mView.updateHeartIconDisplay(true);
                    mView.setCurrentUserLikedPostStatus(true);
                    // add this to myLikes
                    userLikesRef.child(currentUserId).child(postId).setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void unlikePost() {
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long currLikeCount = (long) snapshot.child("likeCount").getValue();
                    updatePost(currLikeCount - 1);
                    mView.setNewLikeCount(currLikeCount - 1);
                    mView.updateHeartIconDisplay(false);
                    mView.setCurrentUserLikedPostStatus(false);
                    // remove from userLikes
                    userLikesRef.child(currentUserId).child(postId).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updatePost(long newLikeCount) {
        postRef.child("likeCount").setValue(newLikeCount);
    }
}
