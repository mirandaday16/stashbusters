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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;


import edu.neu.madcourse.stashbusters.WorldFeedActivity;

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
        Intent intent = new Intent(mContext, WorldFeedActivity.class);
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
                                userId = mAuth.getCurrentUser().getUid();
                                updateToken();
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

    /**
     * Upon successfully logging in, get the device's current token
     * and then update the user's token in Firebase.
     * The token is updated in case the user has uninstalled/re-installed the app
     * or if the current token in Firebase has expired.
     */
    private void updateToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Push the token to the user's data in Firebase.
                        FirebaseDatabase.getInstance().getReference()
                                .child("users").child(userId).child("deviceToken")
                                .setValue(token);

                        Log.d("Token:", token);
                    }
                });
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
