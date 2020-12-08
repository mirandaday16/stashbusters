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
import java.util.List;

import edu.neu.madcourse.stashbusters.contracts.MyFeedContract;
import edu.neu.madcourse.stashbusters.contracts.SnackPostContract;
import edu.neu.madcourse.stashbusters.model.SnackBustPost;
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

    private boolean postsSet = false;

    private String userId; // owner of the profile

    public MyFeedPresenter(Context context) {
        this.mContext = context;
        this.mView = (MyFeedContract.MvpView) context;

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userId = mAuth.getCurrentUser().getUid();
    }


    public void loadSnackPosts() {
        postsRef = FirebaseDatabase.getInstance().getReference()
                .child("snackPosts");

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!postsSet) {

                    ArrayList posts = new ArrayList();

                    if (snapshot.getValue() == null) {
                        //mView.setNoPosts();
                    } else {

                        // For each user under the "snackPosts" node, retrieve all the child posts...
                        for (DataSnapshot dsp : snapshot.getChildren()) {
                            // For each child post, retrieve and append to the posts ArrayList
                            for (DataSnapshot child : dsp.getChildren()) {
                                SnackBustPost post = child.getValue(SnackBustPost.class);
                                posts.add(post);
                            }
                        }


                        Collections.reverse(posts);


                        //mView.setPostView(posts);
                        //postsSet = true;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }


//    public void loadSingleSnackPost(String userId, String postId) {
//        postsRef = FirebaseDatabase.getInstance().getReference()
//                .child("snackPosts").child(userId).child(postId);
//
//        postsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (!postsSet) {
//                    SnackBustPost post = snapshot.getValue(SnackBustPost.class);
//
//                    List<SnackBustPost> posts = new ArrayList();
//                    posts.add(post);
//
//                    //mView.setPostView(posts);
//                    //postsSet = true;
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//
//        });
//    }


//    public void loadAuthorData(String authorId) {
//        authorRef = FirebaseDatabase.getInstance().getReference()
//                .child("users").child(authorId);
//
//        authorRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User author = snapshot.getValue(User.class);
//
//                //mView.setNewCard(author);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }
}
