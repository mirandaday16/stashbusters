package edu.neu.madcourse.stashbusters.presenters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import edu.neu.madcourse.stashbusters.PostsViewHolder;
import edu.neu.madcourse.stashbusters.model.Post;
import edu.neu.madcourse.stashbusters.model.StashPanelPost;
import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.contracts.PersonalProfileContract;
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

    private FirebaseAuth mAuth;
    private DatabaseReference userProfileRef;
    private DatabaseReference postsRef; //TODO: panel vs swap

    public PersonalProfilePresenter(Context context, String userId) {
        this.mView = (PersonalProfileContract.MvpView) context;
        this.mContext = context;
        this.userId = userId;

        mAuth = FirebaseAuth.getInstance();

        userProfileRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        postsRef = FirebaseDatabase.getInstance().getReference().child("panelPosts");
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
        // query this user's posts data in DB
        Query getPosts = postsRef.child(userId);
        FirebaseRecyclerOptions<StashPanelPost> options = new FirebaseRecyclerOptions.Builder<StashPanelPost>()
            .setQuery(getPosts, StashPanelPost.class)
            .build();

        FirebaseRecyclerAdapter<StashPanelPost, PostsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<StashPanelPost, PostsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostsViewHolder holder, int position, @NonNull StashPanelPost model) {
//                final String postId = getRef(position).getKey();
                holder.setHeadline(model.getTitle());
                holder.setUsername(model.getAuthorId());
                holder.setPostPhoto(model.getPhotoUrl());
            }

            @NonNull
            @Override
            public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.content_feed_list_item, parent, false);
                return new PostsViewHolder(view);
            };
        };
        mView.setPostListAdapter(firebaseRecyclerAdapter);
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
