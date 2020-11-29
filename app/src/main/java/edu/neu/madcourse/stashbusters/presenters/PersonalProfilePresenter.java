package edu.neu.madcourse.stashbusters.presenters;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.neu.madcourse.stashbusters.EditAccountActivity;
import edu.neu.madcourse.stashbusters.NewPostActivity;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.SnackBustingActivity;
import edu.neu.madcourse.stashbusters.contracts.PersonalProfileContract;
import edu.neu.madcourse.stashbusters.views.PersonalProfileActivity;

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

    public PersonalProfilePresenter(Context context, String userId) {
        // TODO: When user gets here, must be private profile
        this.mView = (PersonalProfileContract.MvpView) context;
        this.mContext = context;
        this.userId = userId;

        mAuth = FirebaseAuth.getInstance();

        userProfileRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
    }

    @Override
    public void loadDataToView() {
        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // only execute if user exists
                if (snapshot.exists()) {
//                    String photoUrl = snapshot.child("profilePic").getValue().toString();
                    // TODO: review
                    String photoUrl = "https://icon-library.com/images/default-profile-icon/default-profile-icon-16.jpg";
                    Object photo = snapshot.child("profilePic").getValue();
                    if (photo != null) {
                        photoUrl = snapshot.child("profilePic").getValue().toString();
                    }
                    String username = snapshot.child("username").getValue().toString();
                    String bio = snapshot.child("bio").getValue().toString();
//                    String followerCount = snapshot.child("followerCount").getValue().toString();
                    String followerCount = "10";
                    mView.setViewData(photoUrl, username, bio, followerCount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onEditProfileButtonClick(String userId) {
        // TODO: display edit profile page

    }

    @Override
    public boolean onToolbarClick(MenuItem item) {
        if (item.getItemId() == R.id.edit_profile_menu_item) {
            startEditProfileActivity();
        } else if (item.getItemId() == R.id.log_out_menu_item) {
            FirebaseAuth.getInstance().signOut();
        }
        return false;
    }

    @Override
    public void onNewPostButtonClick() {
        startNewPostActivity();
    }

    @Override
    public void onMyProfileButtonClick() {
        startMyProfileActivity();
    }

    @Override
    public void onSnackBustingButtonClick() {
        startSnackBustingActivity();
    }

    // Starts New Post Activity
    private void startNewPostActivity() {
        Intent intent = new Intent(this.mContext, NewPostActivity.class);
        mContext.startActivity(intent);
    }

    // Restarts My Profile Activity (current activity, will just reload page)
    private void startMyProfileActivity() {
        Intent intent = new Intent(this.mContext, PersonalProfileActivity.class);
        mContext.startActivity(intent);
    }

    // Starts Edit Profile Activity
    private void startEditProfileActivity() {
        Intent intent = new Intent(this.mContext, EditAccountActivity.class);
        mContext.startActivity(intent);
    }

    // Starts Snack Busting Activity
    private void startSnackBustingActivity() {
        Intent intent = new Intent(this.mContext, SnackBustingActivity.class);
        mContext.startActivity(intent);
    }
}
