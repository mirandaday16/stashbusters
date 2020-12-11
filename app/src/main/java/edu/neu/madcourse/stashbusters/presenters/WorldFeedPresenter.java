package edu.neu.madcourse.stashbusters.presenters;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.neu.madcourse.stashbusters.contracts.WorldFeedContract;
import edu.neu.madcourse.stashbusters.model.Post;
import edu.neu.madcourse.stashbusters.model.StashPanelPost;
import edu.neu.madcourse.stashbusters.model.StashSwapPost;

public class WorldFeedPresenter implements WorldFeedContract.Presenter {

    private String TAG = WorldFeedPresenter.class.getSimpleName();
    private WorldFeedContract.MvpView mView;
    private Context mContext;


    private DatabaseReference mDatabase;
    private DatabaseReference postsRef;
    private DatabaseReference authorRef;

    private FirebaseAuth mAuth;


    private String userId;

    public WorldFeedPresenter(Context context) {
        this.mContext = context;
        this.mView = (WorldFeedContract.MvpView) context;

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userId = mAuth.getCurrentUser().getUid();

    }

    //This will be called when the user wants to filter bby stash panel posts

    public void loadPanelPosts() {

        //List for all of the posts
        final List<Post> posts = new ArrayList<>();

        postsRef = FirebaseDatabase.getInstance().getReference();

        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //This takes us into the panel posts
                DataSnapshot panelPosts = snapshot.child("panelPosts");

                //Iterate through each user
                for (DataSnapshot user : panelPosts.getChildren()){
                    //Iterate through each post and add to postslist
                    for (DataSnapshot userPost: user.getChildren()){
                        StashPanelPost post  =  (StashPanelPost) setPostData(userPost, "StashPanel");
                        posts.add(post);
                    }
                }
                Collections.reverse(posts);
                mView.setPosts(posts);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }
        });

    }

    //This will be called when the user wants to filter by swap posts
    public void loadSwapPosts() {
        //List for all of the posts
        final List<Post> posts = new ArrayList<>();

        postsRef = FirebaseDatabase.getInstance().getReference();

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot swapPosts = snapshot.child("swapPosts");

                //Iterate through each user
                for (DataSnapshot user : swapPosts.getChildren()){
                    //Iterate through each post and add to postslist
                    for (DataSnapshot userPost: user.getChildren()){
                        StashSwapPost post  = (StashSwapPost) setPostData(userPost, "StashSwap");
                        posts.add(post);

                    }
                }

                Collections.reverse(posts);
                mView.setPosts(posts);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }
        });

    }
    @Override
    public void loadPosts() {
        //List for all of the posts
        final List<Post> posts = new ArrayList<>();

        postsRef = FirebaseDatabase.getInstance().getReference();

        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //This takes us into the panel posts
                DataSnapshot panelPosts = snapshot.child("panelPosts");

                //Iterate through each user
                for (DataSnapshot user : panelPosts.getChildren()){
                    //Iterate through each post and add to postslist
                    for (DataSnapshot userPost: user.getChildren()){
                        StashPanelPost post  =  (StashPanelPost) setPostData(userPost, "StashPanel");
                        posts.add(post);
                    }
                }

                DataSnapshot swapPosts = snapshot.child("swapPosts");

                //Iterate through each user
                for (DataSnapshot user : swapPosts.getChildren()){
                    //Iterate through each post and add to postslist
                    for (DataSnapshot userPost: user.getChildren()){
                        StashSwapPost post  = (StashSwapPost) setPostData(userPost, "StashSwap");
                        posts.add(post);

                    }
                }

                Collections.reverse(posts);
                mView.setPosts(posts);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }
        });

    }

    protected Post setPostData (DataSnapshot postSnapShot, String postType){
        Post post;

        if (postType.equals("StashPanel")){
            post = new StashPanelPost();
        }else {
            post = new StashSwapPost();
        }
        long createdDate = (long) postSnapShot.child("createdDate").getValue();
        long likeCount = (long) postSnapShot.child("likeCount").getValue();


        post.setAuthorId(postSnapShot.child("authorId").getValue().toString());
        post.setPhotoUrl(postSnapShot.child("photoUrl").getValue().toString());
        post.setTitle(postSnapShot.child("title").getValue().toString());
        post.setId(postSnapShot.child("id").getValue().toString());
        post.setDescription((postSnapShot.child("description").getValue().toString()));

        post.setLikeCount(likeCount);
        post.setCreatedDate(createdDate);

        return post;
    }
}
