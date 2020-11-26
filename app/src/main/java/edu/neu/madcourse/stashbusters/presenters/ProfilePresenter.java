package edu.neu.madcourse.stashbusters.presenters;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.neu.madcourse.stashbusters.contracts.ProfileContract;

/**
 * Responsible for handling actions from the View and updating the UI as required.
 */
public class ProfilePresenter implements ProfileContract.Presenter {
    private static final String TAG = ProfilePresenter.class.getSimpleName();

    private ProfileContract.MvpView mView;
    private Context mContext;
    private String userId; // owner of the profile
    private String currentUserId;

    private FirebaseAuth mAuth;
    private DatabaseReference userProfileRef;

    public ProfilePresenter(Context context, String userId) {
        this.mView = (ProfileContract.MvpView) context;
        this.mContext = context;
        this.userId = userId;

        mAuth = FirebaseAuth.getInstance();
//        currentUserId = mAuth.getCurrentUser().getUid();

        // check if current user is owner of this profile, if yes, display edit btn
//        if (!currentUserId.equals(userId)) {
//            // TODO: hide display edit profile btn
              // mView.setEditProfileBtnVisibility(View.INVISIBLE);
//        }

        userProfileRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
    }

    @Override
    public void loadDataToView() {
        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // only execute if user exists
                if (snapshot.exists()) {
                    String photoUrl = snapshot.child("photoUrl").getValue().toString();
                    String username = snapshot.child("username").getValue().toString();
                    String bio = snapshot.child("bio").getValue().toString();
                    String followerCount = snapshot.child("followerCount").getValue().toString();
                    mView.setViewData(photoUrl, username, bio, followerCount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
