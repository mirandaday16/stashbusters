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

import edu.neu.madcourse.stashbusters.contracts.PanelPostContract;
import edu.neu.madcourse.stashbusters.contracts.PostContract;
import edu.neu.madcourse.stashbusters.views.PanelPostActivity;

/**
 * Handles logic for {@link edu.neu.madcourse.stashbusters.views.PanelPostActivity}
 */
public class PanelPostPresenter extends PostPresenter {
    private static final String TAG = PanelPostPresenter.class.getSimpleName();

    private Context mContext;
    private PanelPostContract.MvpView mView;
    private String authorId, postId;
    private FirebaseAuth mAuth;
    private DatabaseReference postRef;
    private DatabaseReference authorUserRef;

    public PanelPostPresenter(Context context, String authorId, String postId) {
        super(context, authorId, postId);
        this.mView = (PanelPostContract.MvpView) context;

        mAuth = FirebaseAuth.getInstance();

        postRef = FirebaseDatabase.getInstance().getReference()
                .child("panelPosts").child(authorId).child(postId);
        authorUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(authorId);

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
                    long likeCount = (long) snapshot.child("likeCount").getValue();
                    mView.setPostViewData(title, postPicUrl, description, createdDate, likeCount);

                    Log.i(TAG, "loadPostDataToView:success");
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){
                Log.e(TAG, error.toString());
            }

        });
    }
}
