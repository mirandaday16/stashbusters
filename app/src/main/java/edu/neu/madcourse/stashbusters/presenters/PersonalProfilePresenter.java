package edu.neu.madcourse.stashbusters.presenters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

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
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.adapters.PostAdapter;
import edu.neu.madcourse.stashbusters.model.Comment;
import edu.neu.madcourse.stashbusters.model.Post;
import edu.neu.madcourse.stashbusters.model.StashPanelPost;
import edu.neu.madcourse.stashbusters.model.StashSwapPost;
import edu.neu.madcourse.stashbusters.views.EditProfileActivity;
import edu.neu.madcourse.stashbusters.views.LoginActivity;
import edu.neu.madcourse.stashbusters.views.PanelPostActivity;
import edu.neu.madcourse.stashbusters.views.PersonalProfileActivity;
import edu.neu.madcourse.stashbusters.views.SwapPostActivity;

/**
 * Responsible for handling actions from the View and updating the UI as required.
 */
public class PersonalProfilePresenter extends ProfilePresenter {
    private static final String TAG = PersonalProfilePresenter.class.getSimpleName();

    private DatabaseReference likedPostsRef, mDatabase;
    private List<Post> likedPostList;
    private PostAdapter likedPostAdapter;

    public PersonalProfilePresenter(Context context, String userId) {
        super(context, userId);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        likedPostsRef = mDatabase.child("userLikes");

        likedPostList = new ArrayList<>();
        likedPostAdapter = new PostAdapter(mContext, likedPostList);
    }

    public boolean onToolbarClick(MenuItem item) {
        if (item.getItemId() == R.id.edit_profile_menu_item) {
            startEditProfileActivity();
        } else if (item.getItemId() == R.id.log_out_menu_item) {
            FirebaseAuth.getInstance().signOut();
            startLoginActivity();
        }
        return false;
    }

    public void getUserLikedPosts() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likedPostList.clear();

                // get userLikes
                DataSnapshot likePostsSnapshot = snapshot.child("userLikes").child(userId);

                // in here, I have a list of postIds of posts that user liked
                // query all posts to display
                Map<String, Object> likeMapping = (HashMap) likePostsSnapshot.getValue();
                System.out.println("likeMapping" + likeMapping);

                for (String key : likeMapping.keySet()) {
                    System.out.println("Current key i.e. post id " + key);
                    DataSnapshot postSnapshot = snapshot.child("allPosts").child(key);
                    List<Comment> postComments = getPostCommentsList(postSnapshot);

                    String postType = postSnapshot.child("postType").getValue().toString();
                    if (postType.equals("StashPanelPost")) {
                        StashPanelPost post = (StashPanelPost) setPostData(postSnapshot, "StashPanel");
                        post.setComments(postComments);
                        likedPostList.add(post);
                    } else {
                        StashSwapPost post = (StashSwapPost) setPostData(postSnapshot, "StashSwap");
                        post.setComments(postComments);
                        likedPostList.add(post);
                    }
                }

                Collections.reverse(likedPostList);
                likedPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){
                Log.e(TAG, error.toString());
            }

        });

        Log.i(TAG, "getUserLikedPostsData:success");
        mView.setPostListAdapter(likedPostAdapter);
    }

    // Starts Edit Profile Activity
    private void startEditProfileActivity() {
        Intent intent = new Intent(this.mContext, EditProfileActivity.class);
        mContext.startActivity(intent);
    }

    // Starts Login Activity
    private void startLoginActivity() {
        Intent intent = new Intent(this.mContext, LoginActivity.class);
        mContext.startActivity(intent);
    }
}
