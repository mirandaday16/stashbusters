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


import edu.neu.madcourse.stashbusters.YourFeedActivity;

import edu.neu.madcourse.stashbusters.utils.Utils;

import edu.neu.madcourse.stashbusters.views.LoginActivity;
import edu.neu.madcourse.stashbusters.views.NewAccountActivity;
import edu.neu.madcourse.stashbusters.contracts.LoginContract;
import edu.neu.madcourse.stashbusters.views.PanelPostActivity;
import edu.neu.madcourse.stashbusters.views.PersonalProfileActivity;
import edu.neu.madcourse.stashbusters.views.PublicProfileActivity;
import edu.neu.madcourse.stashbusters.views.SnackPostActivity;
import edu.neu.madcourse.stashbusters.views.SwapPostActivity;

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

    @Override
    public void startWorldFeedActivity() {
        // TODO: When World Feed activity exists, change this function to go  to World Feed
        Intent intent = new Intent(mContext, YourFeedActivity.class);
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
                                startWorldFeedActivity();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                if (task.getException().toString().contains("FirebaseAuthInvalidUserException")) {
                                    Utils.showToast(mContext, "An account for this email address does not exist.");
                                }
                                else if (task.getException().toString().contains("FirebaseAuthInvalidCredentialsException")) {
                                    Utils.showToast(mContext, "Make sure your password is correct");
                                } else {
                                    Utils.showToast(mContext, "Authorization failed.");
                                }
                            }

                        }
                    });
        }
    }

    @Override
    public void checkIfFromNotification(Intent oldIntent){

        if (oldIntent.getExtras() != null){
            Intent intent;

            switch (oldIntent.getStringExtra("postType")){
                case "snack":
                    intent = new Intent(mContext, SnackPostActivity.class);
                    intent.putExtra("userId", oldIntent.getStringExtra("userId"));
                    intent.putExtra("postId", oldIntent.getStringExtra("postId"));
                    break;
                case "follow":
                    intent = new Intent(mContext, PublicProfileActivity.class);
                    intent.putExtra("userId", oldIntent.getStringExtra("senderId"));
                    break;
                case "commentPanel":
                case "likePanel":
                    intent = new Intent(mContext, PanelPostActivity.class);
                    intent.putExtra("userId", oldIntent.getStringExtra("userId"));
                    intent.putExtra("postId", oldIntent.getStringExtra("postId"));
                    break;
                case "commentSwap":
                case "likeSwap":
                    intent = new Intent(mContext, SwapPostActivity.class);
                    intent.putExtra("userId", oldIntent.getStringExtra("userId"));
                    intent.putExtra("postId", oldIntent.getStringExtra("postId"));
                    break;
                default:
                    intent = new Intent(mContext, LoginActivity.class);
                    break;
            }
            mContext.startActivity(intent);
            mView.callFinish();
        }

    }

}
