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

import edu.neu.madcourse.stashbusters.contracts.SwapPostContract;

/**
 * Handles logic for {@link edu.neu.madcourse.stashbusters.views.SwapPostActivity}
 */
public class SwapPostPresenter extends PostPresenter {
    private static final String TAG = SwapPostPresenter.class.getSimpleName();

    private SwapPostContract.MvpView mView;
    private Context mContext;

    private String authorId, postId, currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference postRef, userLikesRef;
    private DatabaseReference authorUserRef;
    private DatabaseReference commentsRef;

    public SwapPostPresenter(Context context, String authorId, String postId) {
        super(context, authorId, postId);
        this.mContext = context;
        this.mView = (SwapPostContract.MvpView) context;
        this.authorId = authorId;
        this.postId = postId;

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        postRef = FirebaseDatabase.getInstance().getReference()
                .child("swapPosts").child(authorId).child(postId);
        authorUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(authorId);
        userLikesRef = FirebaseDatabase.getInstance().getReference().child("userLikes");

        // comment setup
        commentsRef = FirebaseDatabase.getInstance().getReference()
                .child("swapPosts").child(authorId).child(postId).child("comments");
        super.setPostRef(postRef);
        super.setCommentRef(commentsRef);
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
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }

        });
    }
}
