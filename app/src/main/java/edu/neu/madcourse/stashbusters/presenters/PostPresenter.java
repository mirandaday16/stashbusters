package edu.neu.madcourse.stashbusters.presenters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.data.DataBuffer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.neu.madcourse.stashbusters.CommentRVAdapter;
import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.contracts.PostContract;
import edu.neu.madcourse.stashbusters.model.Comment;
import edu.neu.madcourse.stashbusters.model.StashPanelPost;

import static edu.neu.madcourse.stashbusters.utils.Utils.showToast;

/**
 * Abstract class that handles logic for post details.
 * Extended by {@link PanelPostPresenter} and {@link SwapPostPresenter}
 */
public abstract class PostPresenter implements PostContract.Presenter {
    private static final String TAG = PostPresenter.class.getSimpleName();

    protected Context mContext;
    protected PostContract.MvpView mView;
    protected String authorId, postId, currentUserId;
    protected FirebaseAuth mAuth;
    protected DatabaseReference userLikesRef, postRef;
    protected DatabaseReference authorUserRef;
    protected boolean foundPostInDB = false;

    // comments
    protected DatabaseReference commentsRef;
    protected List<Comment> commentsList;
    protected CommentRVAdapter commentsAdapter;

    public PostPresenter(Context context, String authorId, String postId) {
        this.mContext = context;
        this.authorId = authorId;
        this.postId = postId;
        this.mView = (PostContract.MvpView) context;

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        authorUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(authorId);

        userLikesRef = FirebaseDatabase.getInstance().getReference().child("userLikes");

        // todo: initialize these two in subclasses

    }

    public void setPostRef(DatabaseReference postRef) {
        this.postRef = postRef;
        setupCommentAdapter();
    }

    public void setCommentRef(DatabaseReference commentRef) {
        this.commentsRef = commentRef;
    }

    private void setupCommentAdapter() {
        commentsList = new ArrayList<>();
        commentsAdapter = new CommentRVAdapter(mContext, commentsList, postRef);
    }

    @Override
    public void loadAuthorDataToView() {
        authorUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("username").getValue().toString();
                    String profilePicUrl = snapshot.child("photoUrl").getValue().toString();
                    mView.setAuthorViewData(username, profilePicUrl);

                    Log.i(TAG, "loadAuthorDataToView:success");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }
        });
    }

    public abstract void loadPostDataToView();

    /**
     * At the beginning when the post first opened, check current user's like status with the post.
     * If user liked the post, update heart icon accordingly and set state of the like status.
     */
    public void checkLikeStatus() {
        userLikesRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // check against post id
//                    boolean foundPost = false;
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        System.out.println("postSnapshot " + postSnapshot);
                        if (postSnapshot.getKey().equals(postId)) {
                            foundPostInDB = true;
                            mView.updateHeartIconDisplay(true);
                            mView.setCurrentUserLikedPostStatus(true);
                            return;
                        }
                    }

                    if (!foundPostInDB) {
                        mView.updateHeartIconDisplay(false);
                        mView.setCurrentUserLikedPostStatus(false);
                    }
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
    public void onHeartIconClick(DatabaseReference postRef) {
        // check current like status, if already liked, unlike post + remove from DB.
        // else, like post and add to DB
        boolean likeStatus = mView.getCurrentUserLikedPostStatus();
        System.out.println("LIKE STATUS " + likeStatus);

        if (likeStatus) {
            System.out.println("UNLIKING POST");

            // already liked, clicking heart icon again to unlike post
            unlikePost(postRef);
        } else {
            System.out.println("LIKING POST");
            // not liked yet, clicking heart icon to like post
            likePost(postRef);
        }


        // call view to update to filled heart
    }

    private void likePost(final DatabaseReference postRef) {
        // increment count + call view to reflect
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long currLikeCount = (long) snapshot.child("likeCount").getValue();
                    updatePost(postRef, currLikeCount + 1);
                    mView.setNewLikeCount(currLikeCount + 1);
                    mView.updateHeartIconDisplay(true);
                    mView.setCurrentUserLikedPostStatus(true);
                    // add this to myLikes
                    userLikesRef.child(currentUserId).child(postId).setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());

            }
        });
    }

    private void unlikePost(final DatabaseReference postRef) {
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    long currLikeCount = (long) snapshot.child("likeCount").getValue();
                    updatePost(postRef, currLikeCount - 1);
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

    private void updatePost(DatabaseReference postRef, long newLikeCount) {
        postRef.child("likeCount").setValue(newLikeCount);
    }

    public void loadCommentDataToView(Context context) {
        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    commentsList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        // Each snapshot is a comment
                        Comment comment = new Comment();

                        long createdDate = (long) dataSnapshot.child("createdDate").getValue();

                        comment.setAuthorId(dataSnapshot.child("authorId").getValue().toString());
                        comment.setCreatedDate(createdDate);
                        comment.setText(dataSnapshot.child("text").getValue().toString());
                        commentsList.add(comment);
                    }
                    commentsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }
        });

        Log.i(TAG, "getPostCommentsData:success");
        mView.setCommentAdapter(commentsAdapter);
    }

    /**
     * Function that attempts to upload the post when the postButton is clicked.
     */
    @Override
    public void uploadComment(DatabaseReference postRef, Comment comment) {
        // Stores comments in a separate "comments" node in Firebase
        DatabaseReference commentNodeRef = postRef.child("comments");
        DatabaseReference newCommentRef = commentNodeRef.push(); // push used to generate unique id
        newCommentRef.setValue(comment);
    }
}
