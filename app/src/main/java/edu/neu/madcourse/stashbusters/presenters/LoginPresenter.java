package edu.neu.madcourse.stashbusters.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.neu.madcourse.stashbusters.views.NewAccountActivity;
import edu.neu.madcourse.stashbusters.contracts.LoginContract;
import edu.neu.madcourse.stashbusters.views.PersonalProfileActivity;

public class LoginPresenter implements LoginContract.Presenter {
    private static final String TAG = NewAccountActivity.class.getSimpleName();

    private LoginContract.MvpView mView;
    private Context mContext;
    private FirebaseAuth mAuth;
    private String userId; // owner of the profile

    public LoginPresenter(Context context) {
        this.mContext = context;
        this.mView = (LoginContract.MvpView) context;
        mAuth = mView.getmAuth();
    }

    @Override
    public void createNewAccount() {
        Intent intent = new Intent(mContext, NewAccountActivity.class);
        mContext.startActivity(intent);
    }

    // Starts World Feed Activity
    public void startWorldFeedActivity(String userId) {
        // TODO: When World Feed activity exists, change this function to go  to World Feed
        Intent intent = new Intent(mContext, PersonalProfileActivity.class);
        intent.putExtra("userId", userId);
        mContext.startActivity(intent);
    }

    @Override
    public void validateNewUser(String username, String password) {
        if (username.matches("") || password.matches("")) {
            String msg = "Please enter a username and password";
            mView.showToastMessage(msg);
        } else {
            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener((Activity) mView, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, send user to World Feed
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                startWorldFeedActivity(user.getUid()); // TODO: "user" needs to be passed to this somehow
                            } else {
                                // If sign in fails, display a toast message to the user
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                mView.showToastMessage("Authentication failed.");
                            }

                        }
                    });
        }
    }

}
