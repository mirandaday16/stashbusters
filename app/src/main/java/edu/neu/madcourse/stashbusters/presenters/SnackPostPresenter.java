package edu.neu.madcourse.stashbusters.presenters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import edu.neu.madcourse.stashbusters.contracts.SnackPostContract;
import edu.neu.madcourse.stashbusters.model.SnackBustChoice;
import edu.neu.madcourse.stashbusters.model.SnackBustPost;
import edu.neu.madcourse.stashbusters.model.User;
import edu.neu.madcourse.stashbusters.views.SnackPostActivity;

/**
 * This class is responsible for handling actions from the View and updating the UI as required.
 */
public class SnackPostPresenter implements SnackPostContract.Presenter{
    private static final String TAG = SnackPostPresenter.class.getSimpleName();
    private SnackPostContract.MvpView mView;
    private Context mContext;

    private DatabaseReference mDatabase;
    private DatabaseReference postsRef;
    private DatabaseReference authorRef;
    private FirebaseAuth mAuth;

    private String userId; // owner of the profile

    public SnackPostPresenter(Context context) {
        this.mContext = context;
        this.mView = (SnackPostContract.MvpView) context;

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userId = mAuth.getCurrentUser().getUid();
    }

    @Override
    public void loadSnackPosts() {
        postsRef = FirebaseDatabase.getInstance().getReference()
                .child("snackPosts");

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList posts = new ArrayList();

                // For each user under the "snackPosts" node, retrieve all the child posts...
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    // For each child post, retrieve and append to the posts ArrayList
                    for (DataSnapshot child: dsp.getChildren()){
                        SnackBustPost post = child.getValue(SnackBustPost.class);
                        posts.add(post);
                    }
                }

                Collections.reverse(posts);


                mView.setPostView(posts);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    @Override
    public void loadSingleSnackPost(String userId, String postId) {
        postsRef = FirebaseDatabase.getInstance().getReference()
                .child("snackPosts").child(userId).child(postId);

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SnackBustPost post = snapshot.getValue(SnackBustPost.class);

                List<SnackBustPost> posts = new ArrayList();
                posts.add(post);

                mView.setPostView(posts);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    @Override
    public void loadAuthorData(String authorId) {
        authorRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(authorId);

        authorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User author = snapshot.getValue(User.class);

                mView.setNewCard(author);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }
}
