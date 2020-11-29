package edu.neu.madcourse.stashbusters.presenters;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.stashbusters.contracts.EditProfileContract;

public class EditProfilePresenter implements EditProfileContract.Presenter {
    private static final String TAG = PersonalProfilePresenter.class.getSimpleName();

    private EditProfileContract.MvpView mView;
    private Context mContext;

    private String userId;
    private FirebaseAuth mAuth;
    private DatabaseReference userProfileRef;

    public EditProfilePresenter(Context context, String userId) {
        this.mView = (EditProfileContract.MvpView) context;
        this.mContext = context;
        this.userId = userId;

        mAuth = FirebaseAuth.getInstance();
        userProfileRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
    }

    @Override
    public void updateProfile() {

    }
}
