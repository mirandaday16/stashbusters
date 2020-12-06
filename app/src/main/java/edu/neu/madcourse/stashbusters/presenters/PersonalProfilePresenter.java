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
import java.util.List;

import edu.neu.madcourse.stashbusters.PostAdapter;
import edu.neu.madcourse.stashbusters.model.Post;
import edu.neu.madcourse.stashbusters.model.StashPanelPost;
import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.contracts.PersonalProfileContract;
import edu.neu.madcourse.stashbusters.model.StashSwapPost;
import edu.neu.madcourse.stashbusters.views.EditProfileActivity;
import edu.neu.madcourse.stashbusters.views.LoginActivity;

/**
 * Responsible for handling actions from the View and updating the UI as required.
 */
public class PersonalProfilePresenter implements PersonalProfileContract.Presenter {
    private static final String TAG = PersonalProfilePresenter.class.getSimpleName();

    private PersonalProfileContract.MvpView mView;
    private Context mContext;
    private String userId; // owner of the profile

    private List<Post> postList;
    private PostAdapter postAdapter;

    private FirebaseAuth mAuth;
    private DatabaseReference userProfileRef;
    private DatabaseReference postsRef;

    public PersonalProfilePresenter(Context context, String userId) {
        this.mView = (PersonalProfileContract.MvpView) context;
        this.mContext = context;
        this.userId = userId;

        mAuth = FirebaseAuth.getInstance();

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(mContext, postList);

        userProfileRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        postsRef = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    public void loadDataToView() {
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
    public void onEditProfileButtonClick(String userId) {
        Intent intent = new Intent(mContext, EditProfileActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public boolean onToolbarClick(MenuItem item) {
        if (item.getItemId() == R.id.edit_profile_menu_item) {
            startEditProfileActivity();
        } else if (item.getItemId() == R.id.log_out_menu_item) {
            FirebaseAuth.getInstance().signOut();
            startLoginActivity();
        }
        return false;
    }

    @Override
    public void getUserPostsData() {
        // Panel posts
        DatabaseReference panelPosts = postsRef.child("panelPosts").child(userId);
        // Swap Posts
        DatabaseReference swapPosts = postsRef.child("swapPosts").child(userId);

        panelPosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    postList.clear();
                    System.out.println("Count " + snapshot.getChildrenCount());

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        StashPanelPost post = dataSnapshot.getValue(StashPanelPost.class);
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
                        StashSwapPost post = dataSnapshot.getValue(StashSwapPost.class);
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
