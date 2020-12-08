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

import edu.neu.madcourse.stashbusters.adapters.PostAdapter;
import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.contracts.PublicProfileContract;
import edu.neu.madcourse.stashbusters.model.Comment;
import edu.neu.madcourse.stashbusters.model.Post;
import edu.neu.madcourse.stashbusters.model.StashPanelPost;
import edu.neu.madcourse.stashbusters.model.StashSwapPost;

public class PublicProfilePresenter implements PublicProfileContract.Presenter {
    private static final String TAG = PublicProfilePresenter.class.getSimpleName();

    private PublicProfileContract.MvpView mView;
    private Context mContext;
    // targetUserId is the owner of this profile
    private String targetUserId, currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference targetUserProfileRef;
    private DatabaseReference followRef;

    private List<Post> postList;
    private PostAdapter postAdapter;
    private DatabaseReference postsRef;

    public PublicProfilePresenter(Context context, String targetUserId) {
        this.mContext = context;
        this.mView = (PublicProfileContract.MvpView) context;
        this.targetUserId = targetUserId;

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(mContext, postList);
        postsRef = FirebaseDatabase.getInstance().getReference();

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
    public void getUserPostsData() {
        // Panel posts
        DatabaseReference panelPosts = postsRef.child("panelPosts").child(targetUserId);
        // Swap Posts
        DatabaseReference swapPosts = postsRef.child("swapPosts").child(targetUserId);

        panelPosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    postList.clear();

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

    private List<Comment> getPostCommentsList(DataSnapshot postSnapShot) {
        List<Comment> postComments = new ArrayList<>();
        DataSnapshot commentsSnapshot = postSnapShot.child("comments");

        for (DataSnapshot singleComment : commentsSnapshot.getChildren()) {
            Comment comment = singleComment.getValue(Comment.class);
            postComments.add(comment);
        }

        return postComments;
    }

    private Post setPostData(DataSnapshot postSnapShot, String postType){
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


}
