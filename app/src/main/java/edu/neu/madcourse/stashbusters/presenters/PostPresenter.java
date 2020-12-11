package edu.neu.madcourse.stashbusters.presenters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.neu.madcourse.stashbusters.adapters.CommentRVAdapter;
import edu.neu.madcourse.stashbusters.contracts.PostContract;
import edu.neu.madcourse.stashbusters.model.Comment;
import edu.neu.madcourse.stashbusters.model.Post;
import edu.neu.madcourse.stashbusters.model.StashPanelPost;
import edu.neu.madcourse.stashbusters.model.StashSwapPost;
import edu.neu.madcourse.stashbusters.utils.Utils;
import edu.neu.madcourse.stashbusters.views.PersonalProfileActivity;

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
    protected DatabaseReference userLikesRef, postRef, allPostsRef;
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
        allPostsRef = FirebaseDatabase.getInstance().getReference().child("allPosts");
        userLikesRef = FirebaseDatabase.getInstance().getReference().child("userLikes");
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
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

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
    public void onHeartIconClick(DatabaseReference postRef, String notifType, String authorId, String postId) {
        // check current like status, if already liked, unlike post + remove from DB.
        // else, like post and add to DB
        boolean likeStatus = mView.getCurrentUserLikedPostStatus();

        if (likeStatus) {
            // already liked, clicking heart icon again to unlike post
            unlikePost(postRef);
            // update on allPosts too
            unlikePost(allPostsRef.child(postId));
        } else {
            // not liked yet, clicking heart icon to like post
            likePost(postRef);
            likePost(allPostsRef.child(postId));

            // Send notification to post author
            startCommentNotification(notifType, authorId, postId);
        }
    }

    @Override
    public void deletePost() {
        allPostsRef.child(postId).removeValue();
        postRef.removeValue();

        // remove from a user's likes
        userLikesRef.child(currentUserId).child(postId).removeValue();

        Intent intent = new Intent(mContext, PersonalProfileActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void editPost(EditText editText) {
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("description", editText.getText().toString());

        postRef.updateChildren(updates);
        allPostsRef.child(postId).updateChildren(updates);
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

    @Override
    public void startCommentNotification(final String notifType, final String authorId, final String postId) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference();

        final ValueEventListener tokenListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    // Get postType and imageURL
                    final String postType = (String) dataSnapshot.child("allPosts").child(postId).child("postType").getValue();
                    final String imgURL = (String) dataSnapshot.child("allPosts").child(postId).child("photoUrl").getValue();

                    // Use util to start notification
                    if (notifType.equals("comment") && postType.equals("StashPanelPost")) {
                        Utils.startNotification("commentPanel", currentUserId, authorId, postId, imgURL);
                    } else if (notifType.equals("comment") && postType.equals("StashSwapPost")) {
                        Utils.startNotification("commentSwap", currentUserId, authorId, postId, imgURL);
                    } else if (notifType.equals("like") && postType.equals("StashPanelPost")) {
                        Utils.startNotification("likePanel", currentUserId, authorId, postId, imgURL);
                    } else {
                        Utils.startNotification("likeSwap", currentUserId, authorId, postId, imgURL);
                    }
                }
                catch (Exception ignored) {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        ref.addListenerForSingleValueEvent(tokenListener);
    }
}
