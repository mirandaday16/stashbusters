package edu.neu.madcourse.stashbusters.presenters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.data.DataBuffer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.neu.madcourse.stashbusters.CommentRVAdapter;
import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.contracts.PostContract;
import edu.neu.madcourse.stashbusters.model.Comment;
import edu.neu.madcourse.stashbusters.model.StashPanelPost;

import static edu.neu.madcourse.stashbusters.utils.Utils.showToast;

public class PostPresenter implements PostContract.Presenter {
    private static final String TAG = PostPresenter.class.getSimpleName();

    private PostContract.MvpView mView;
    private Context mContext;

    private String authorId, postId;
    private FirebaseAuth mAuth;
    private DatabaseReference postRef;
    private DatabaseReference authorUserRef;
    private DatabaseReference commentsRef;

    private List<Comment> commentsList;
    protected CommentRVAdapter commentsAdapter;

    public PostPresenter(Context context, String authorId, String postId) {
        this.mContext = context;
        this.mView = (PostContract.MvpView) context;
        this.authorId = authorId;
        this.postId = postId;

        mAuth = FirebaseAuth.getInstance();

        postRef = FirebaseDatabase.getInstance().getReference()
                .child("panelPosts").child(authorId).child(postId);
        authorUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(authorId);
        commentsRef = FirebaseDatabase.getInstance().getReference()
                .child("panelPosts").child(authorId).child(postId).child("comments");

        commentsList = new ArrayList<>();
        commentsAdapter = new CommentRVAdapter(commentsList, postRef);
    }

    @Override
    public void loadAuthorDataToView() {
        authorUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("username").getValue().toString();
                    String profilePicUrl = snapshot.child("photoUrl").getValue().toString();
                    mView.setAuthorViewData(username, profilePicUrl);

                    Log.i(TAG, "loadAuthorDataToView:success");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }
        });
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
                    mView.setPostViewData(title, postPicUrl, description, createdDate);

                    Log.i(TAG, "loadPostDataToView:success");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }

        });
    }

    @Override
    public void loadCommentDataToView(Context context) {
        // Get all comments for this post from Firebase
        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    commentsList.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        // Each snapshot is a comment
                        Comment comment = new Comment();

                        long createdDate = (long) dataSnapshot.child("createdDate").getValue();

                        comment.setAuthorId(dataSnapshot.child("authorId").getValue().toString());
                        comment.setCreatedDate(createdDate);
                        comment.setText(dataSnapshot.child("text").getValue().toString());
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

    /**
     * Function that attempts to upload the post when the postButton is clicked.
     */
    @Override
    public void uploadComment(Comment comment) {
        // Stores comments in a separate "comments" node in Firebase
        DatabaseReference commentNodeRef = postRef.child("comments");
        DatabaseReference newCommentRef = commentNodeRef.push(); // push used to generate unique id
        newCommentRef.setValue(comment);
    }
}
