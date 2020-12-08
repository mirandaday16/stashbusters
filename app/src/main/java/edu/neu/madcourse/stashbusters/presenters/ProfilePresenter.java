package edu.neu.madcourse.stashbusters.presenters;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.stashbusters.adapters.PostAdapter;
import edu.neu.madcourse.stashbusters.contracts.ProfileContract;
import edu.neu.madcourse.stashbusters.model.Comment;
import edu.neu.madcourse.stashbusters.model.Post;
import edu.neu.madcourse.stashbusters.model.StashPanelPost;
import edu.neu.madcourse.stashbusters.model.StashSwapPost;

/**
 * Abtract class that handles some share functionalities between public and private user profile.
 */
public abstract class ProfilePresenter implements ProfileContract.Presenter{
    private static final String TAG = ProfilePresenter.class.getSimpleName();

    protected Context mContext;
    protected String userId;

    protected ProfileContract.MvpView mView;

    protected List<Post> postList;
    protected PostAdapter postAdapter;
    protected DatabaseReference postsRef;

    protected DatabaseReference userProfileRef;

    public ProfilePresenter(Context context, String userId) {
        this.mContext = context;
        this.userId = userId;
        this.mView = (ProfileContract.MvpView) context;


        // setup recyclerview and adapter
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(mContext, postList);
        postsRef = FirebaseDatabase.getInstance().getReference();

        userProfileRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId);
    }

    @Override
    public void loadDataToView() {
        //load data of target user (might not be current user)
        userProfileRef.addValueEventListener(new ValueEventListener() {
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
    public void getUserPostsData() {
        // Panel posts
        DatabaseReference panelPosts = postsRef.child("panelPosts").child(userId);
        // Swap Posts
        DatabaseReference swapPosts = postsRef.child("swapPosts").child(userId);
        postList.clear();

        panelPosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        // each datasnapshot is a post
                        List<Comment> postComments = getPostCommentsList(snapshot);
                        StashPanelPost post = (StashPanelPost) setPostData(dataSnapshot, "StashPanel");
                        post.setComments(postComments);

                        postList.add(post);
                    }
                    postAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){
                Log.e(TAG, error.toString());
            }

        });

        swapPosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        List<Comment> postComments = getPostCommentsList(snapshot);
                        StashSwapPost post = (StashSwapPost) setPostData(dataSnapshot, "StashSwap");
                        post.setComments(postComments);
                        postList.add(post);
                    }
                    postAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){
                Log.e(TAG, error.toString());
            }

        });

        Log.i(TAG, "getUserPostsData:success");
        mView.setPostListAdapter(postAdapter);
    }

    protected List<Comment> getPostCommentsList(DataSnapshot postSnapShot) {
        List<Comment> postComments = new ArrayList<>();
        DataSnapshot commentsSnapshot = postSnapShot.child("comments");

        for (DataSnapshot singleComment : commentsSnapshot.getChildren()) {
            Comment comment = singleComment.getValue(Comment.class);
            postComments.add(comment);
        }

        return postComments;
    }

    protected Post setPostData(DataSnapshot postSnapShot, String postType){
        Post post;
        if (postType.equals("StashPanel")) {
            post = new StashPanelPost();
        } else {
            post = new StashSwapPost();
        }

        long createdDate = (long) postSnapShot.child("createdDate").getValue();

        post.setAuthorId(postSnapShot.child("authorId").getValue().toString());
        post.setPhotoUrl(postSnapShot.child("photoUrl").getValue().toString());
        post.setTitle(postSnapShot.child("title").getValue().toString());
        post.setId(postSnapShot.child("id").getValue().toString());
        post.setDescription(postSnapShot.child("description").getValue().toString());
        post.setCreatedDate(createdDate);

        return post;
    }
}
