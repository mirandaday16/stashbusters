package edu.neu.madcourse.stashbusters.presenters;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.neu.madcourse.stashbusters.contracts.MyFeedContract;
import edu.neu.madcourse.stashbusters.contracts.SnackPostContract;
import edu.neu.madcourse.stashbusters.model.Post;
import edu.neu.madcourse.stashbusters.model.SnackBustPost;
import edu.neu.madcourse.stashbusters.model.StashPanelPost;
import edu.neu.madcourse.stashbusters.model.StashSwapPost;
import edu.neu.madcourse.stashbusters.model.User;

/**
 * This class is responsible for handling actions from the View and updating the UI as required.
 */
public class MyFeedPresenter implements MyFeedContract.Presenter{
    private static final String TAG = MyFeedPresenter.class.getSimpleName();
    private MyFeedContract.MvpView mView;
    private Context mContext;

    private DatabaseReference mDatabase;
    private DatabaseReference postsRef;
    private DatabaseReference authorRef;
    private FirebaseAuth mAuth;

    private String userId; // owner of the profile

    public MyFeedPresenter(Context context) {
        this.mContext = context;
        this.mView = (MyFeedContract.MvpView) context;

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userId = mAuth.getCurrentUser().getUid();
    }


    @Override
    public void loadPosts() {
        postsRef = FirebaseDatabase.getInstance().getReference();

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // List to store all of the posts.
                List<Post> posts = new ArrayList();

                // Get list of current user's following.
                DataSnapshot followingSnapshot = snapshot.child("follows").child(userId).child("following");
                HashMap<String, Object> following = (HashMap) followingSnapshot.getValue();

                if (following != null) {
                    // For each user in the following list.
                    for (String key : following.keySet()) {

                        // Get all of their panel posts.
                        DataSnapshot panelPosts = snapshot.child("panelPosts").child(key);
                        for (DataSnapshot child : panelPosts.getChildren()) {
                            StashPanelPost post = (StashPanelPost) setPostData(child, "StashPanel");
                            posts.add(post);
                        }

                        // Get all of their swap posts.
                        DataSnapshot swapPosts = snapshot.child("swapPosts").child(key);
                        for (DataSnapshot child : swapPosts.getChildren()) {
                            StashSwapPost post = (StashSwapPost) setPostData(child, "StashSwap");
                            posts.add(post);
                        }
                    }

                    // Put the posts in descending order by date.
                    Collections.reverse(posts);
                }
                mView.setPosts(posts);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    protected Post setPostData(DataSnapshot postSnapShot, String postType){
        Post post;
        if (postType.equals("StashPanel")) {
            post = new StashPanelPost();
        } else {
            post = new StashSwapPost();
        }

        long createdDate = (long) postSnapShot.child("createdDate").getValue();
        long likeCount = (long) postSnapShot.child("likeCount").getValue();

        post.setAuthorId(postSnapShot.child("authorId").getValue().toString());
        post.setPhotoUrl(postSnapShot.child("photoUrl").getValue().toString());
        post.setTitle(postSnapShot.child("title").getValue().toString());
        post.setId(postSnapShot.child("id").getValue().toString());
        post.setDescription(postSnapShot.child("description").getValue().toString());
        post.setDescription(postSnapShot.child("likeCount").getValue().toString());
        post.setLikeCount(likeCount);
        post.setCreatedDate(createdDate);

        return post;
    }
}
