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

import edu.neu.madcourse.stashbusters.CommentRVAdapter;
import edu.neu.madcourse.stashbusters.contracts.SwapPostContract;
import edu.neu.madcourse.stashbusters.model.Comment;

public class SwapPostPresenter extends PostPresenter implements SwapPostContract.Presenter {
    private static final String TAG = SwapPostPresenter.class.getSimpleName();

    private SwapPostContract.MvpView mView;
    private Context mContext;

    private String authorId, postId;
    private FirebaseAuth mAuth;
    private DatabaseReference postRef;
    private DatabaseReference authorUserRef;
    private DatabaseReference commentsRef;

    private List<Comment> commentsList;


    public SwapPostPresenter(Context context, String authorId, String postId) {
        super(context, authorId, postId);
        this.mView = (SwapPostContract.MvpView) context;
        this.postId = postId;

        mAuth = FirebaseAuth.getInstance();

        postRef = FirebaseDatabase.getInstance().getReference()
                .child("swapPosts").child(authorId).child(postId);
        authorUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(authorId);
        commentsRef = FirebaseDatabase.getInstance().getReference()
                .child("swapPosts").child(authorId).child(postId).child("comments");

        commentsList = new ArrayList<>();
        commentsAdapter = new CommentRVAdapter(commentsList, postRef);

    }


    @Override
    public void loadPostDataToView() {
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Load in post info
                    String title = snapshot.child("title").getValue().toString();
                    String postPicUrl = snapshot.child("photoUrl").getValue().toString();
                    String description = snapshot.child("description").getValue().toString();
                    long createdDate = (long) snapshot.child("createdDate").getValue();
                    String material = snapshot.child("material").getValue().toString();
                    Boolean isAvailable = (Boolean) snapshot.child("availability").getValue();
                    mView.setPostViewData(title, postPicUrl, description, createdDate, material, isAvailable);

                    Log.i(TAG, "loadPostDataToView:success");
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){
                Log.e(TAG, error.toString());
            }

        });
    }

    @Override
    public void loadCommentDataToView(Context context) {
        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    commentsList.clear();
                    System.out.println("Comments count: " + snapshot.getChildrenCount());

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Comment comment = dataSnapshot.getValue(Comment.class);
                        commentsList.add(comment);
                    }
                    commentsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }
        });

        Log.i(TAG, "getPostCommentsData:success");
        mView.setCommentAdapter(commentsAdapter);
    }

    @Override
    public void uploadComment(Comment comment) {
        // Stores comments in a separate "comments" node in Firebase
        DatabaseReference commentNodeRef = postRef.child("comments");
        DatabaseReference newCommentRef = commentNodeRef.push(); // push used to generate unique id
        newCommentRef.setValue(comment);
    }
}
