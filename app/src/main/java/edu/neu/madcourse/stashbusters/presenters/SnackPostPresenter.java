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
import java.util.HashMap;
import java.util.List;

import edu.neu.madcourse.stashbusters.contracts.SnackPostContract;
import edu.neu.madcourse.stashbusters.model.SnackBustChoice;
import edu.neu.madcourse.stashbusters.model.SnackBustPost;
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
//        postsRef = FirebaseDatabase.getInstance().getReference()
//                .child("snackPosts");
//
//        postsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // This code grabs the history map from the DB and then initializes a history adapter
//                // in order to put this info into the actual screen
//                // The method lower down the class called getReceiptHistory is the same
//                // except it grabs the received messages to display.
//
//
//                HashMap historyMap = (HashMap) snapshot.getValue();
//                List posts = (List) historyMap.get("sent");
//
//                mView.setPostView(posts);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//
//        });

        SnackBustChoice choice_one = new SnackBustChoice("smiley mouth");
        SnackBustChoice choice_two = new SnackBustChoice("-_- mouth");
        List<SnackBustChoice> choices = new ArrayList<>();
        choices.add(choice_one);
        choices.add(choice_two);
        SnackBustPost post = new SnackBustPost("What kind of mouth?",
                "https://i.ibb.co/cQg5V0d/test-photo-plush.png", choices);
        List<SnackBustPost> posts = new ArrayList<>();
        posts.add(post);
        choice_one = new SnackBustChoice("yes, change it");
        choice_two = new SnackBustChoice("no, keep as is");
        choices = new ArrayList<>();
        choices.add(choice_one);
        choices.add(choice_two);
        post = new SnackBustPost("Leather handle?",
                "https://i.ibb.co/P6mt3d5/test-laptop-case.png", choices);
        posts.add(post);

        mView.setPostView(posts);
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
}
