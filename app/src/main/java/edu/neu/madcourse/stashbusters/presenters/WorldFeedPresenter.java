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
    @Override
    public void loadPosts() {
        //List for all of the posts
        final List<Post> posts = new ArrayList<>();

        postsRef = FirebaseDatabase.getInstance().getReference();

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                //This takes us into the panel posts
                DataSnapshot panelPosts = snapshot.child("panelPosts");

                //Iterate through each user
                for (DataSnapshot user : panelPosts.getChildren()){

                    //Iterate through each post and add to postslist
                    for (DataSnapshot userPost: user.getChildren()){
                        //We know for sure that userPost contains all the correct info
                        //Somewhere between it getting sent to setPostData and coming back, it gets transformed
                        Log.d(TAG, userPost.toString() + " supposedly what  Ineed");
                        StashPanelPost post  =  (StashPanelPost) setPostData(userPost, "StashPanel");
                        posts.add(post);
                        Log.d(TAG, post.toString() + " is this a problem also?");
                    }
                }
                DataSnapshot swapPosts = snapshot.child("swapPosts");

                //Iterate through each user
                for (DataSnapshot user : panelPosts.getChildren()){
                    Log.d(TAG,user.toString() + " THis is the current user");

                    //Iterate through each post and add to postslist
                    for (DataSnapshot userPost: user.getChildren()){
                        Log.d(TAG, userPost.toString() + "this is what ur supposed to be adding ");

                        StashSwapPost post  = (StashSwapPost) setPostData(userPost, "StashSwap");
                        Log.d(TAG, " does it even get here");
                        posts.add(post);
                        Log.d(TAG, posts.toString() + " this is what the posts should look like");

                    }
                }


// could it be something with implementation of
                Collections.reverse(posts);
                mView.setPosts(posts);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
        Log.d(TAG, post.toString() + " at the start this is what");
        long createdDate = (long) postSnapShot.child("createdDate").getValue();

        Log.d(TAG, postSnapShot.child("authorId").getValue().toString() + " This is the current author ID");

        post.setAuthorId(postSnapShot.child("authorId").getValue().toString());


        Log.d(TAG, postSnapShot.child("photoUrl").getValue().toString() + " This is the current photo url");
        post.setPhotoUrl(postSnapShot.child("photoUrl").getValue().toString());
        Log.d(TAG, post.photoUrl + " and this is the value after equals");
        Log.d(TAG, postSnapShot.child("title").getValue().toString() + " title currently");
        post.setTitle(postSnapShot.child("title").getValue().toString());
        post.setId(postSnapShot.child("id").getValue().toString());
        post.setDescription((postSnapShot.child("description").getValue().toString()));

        post.setCreatedDate(createdDate);


        //Sending the post is just sending the post object we actually need the info contained within it

        Log.d(TAG, post.photoUrl + " just before it gets sent");

        return post;
    }
}
