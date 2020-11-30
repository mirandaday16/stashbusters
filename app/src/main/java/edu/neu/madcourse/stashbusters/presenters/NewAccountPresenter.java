package edu.neu.madcourse.stashbusters.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import edu.neu.madcourse.stashbusters.GalleryImagePicker;
import edu.neu.madcourse.stashbusters.NewAccountActivity;
import edu.neu.madcourse.stashbusters.contracts.NewAccountContract;
import edu.neu.madcourse.stashbusters.model.User;
import edu.neu.madcourse.stashbusters.utils.Utils;
import edu.neu.madcourse.stashbusters.views.PersonalProfileActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * This class is responsible for handling actions from the View and updating the UI as required.
 */
public class NewAccountPresenter implements NewAccountContract.Presenter {
    private static final String TAG = NewAccountActivity.class.getSimpleName();
    private String sharedUsername = "username";

    private NewAccountContract.MvpView mView;
    private Context mContext;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference storageRef;
    private DatabaseReference usersRef;
    private SharedPreferences prefs;
    private String profilePicUrl = ""; // TODO: might want to set default url here

    public NewAccountPresenter(Context context) {
        this.mContext = context;
        this.mView = (NewAccountContract.MvpView) context;

        storageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        usersRef = mDatabase.child("users");

        // Shared Preferences for saving login cred
        prefs = this.mContext.getSharedPreferences(sharedUsername, MODE_PRIVATE);
    }

    @Override
    public void onProfilePictureButtonClick() {
        GalleryImagePicker profilePicPicker = new GalleryImagePicker(1);
        mView.selectImage();
    }

    @Override
    public void onSaveButtonClick() {

    }

    @Override
    public boolean validateUsername(String username) {
        final ArrayList<String> invalidCharacters = new ArrayList<String>();
        invalidCharacters.add(".");
        invalidCharacters.add("#");
        invalidCharacters.add("$");
        invalidCharacters.add("[");
        invalidCharacters.add("]");

        for (String character : invalidCharacters) {
            if (username.contains(character)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean validatePassword(String password) {
        if (password.length() > 16 || password.length() < 8) {
            return false;
        }
        return true;
    };

    @Override
    public void registerUser(final String emailAddress,
                             final String username,
                             String password,
                             final String profilePicUrl,
                             final String bio,
                             final String deviceToken) {
        mAuth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener((Activity) mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, create user object and node in Firebase DB
                            User user = new User(emailAddress, username, bio
                            , profilePicUrl, deviceToken);

                            onAuthSuccess(task.getResult().getUser(), user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Utils.showToast(mContext, "Authentication failed.");
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser fireBaseUser, User userData) {
        final String username = userData.getUsername();
        final String userId = fireBaseUser.getUid();
        usersRef.child(userId).setValue(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "createUserWithEmail:success");

                        // save prefs
                        SharedPreferences.Editor preferencesEditor = prefs.edit();
                        preferencesEditor.putString("username", username);
                        preferencesEditor.apply();

                        // go to World Feed Activity
                        startWorldFeedActivity(userId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Data could not be saved: " + e);
                    }
                });
    }

    @Override
    public void updateDeviceToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // get new FCM registration token
                        String deviceToken = task.getResult();
                        mView.setDeviceToken(deviceToken);
                        Log.d(TAG, "Fetched device token successfully: " + deviceToken);
                    }
                });
    }

    public void uploadUserProfilePhotoToStorage(Uri photoUri) {
        final StorageReference ref = storageRef.child("images/" + photoUri.getLastPathSegment());
        final UploadTask uploadTask = ref.putFile(photoUri);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i(TAG, "uploadUserProfilePhotoToStorage:success");

                // download and save to url
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri userPhotoUri = task.getResult();
                            // Pass this info back to the View
                            mView.setProfilePicUrl(userPhotoUri.toString());
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });
            }
        });
    }

    // Starts World Feed Activity
    private void startWorldFeedActivity(String userId) {
        // TODO: When World Feed activity exists, change this function to go  to World Feed
        Intent intent = new Intent(mContext, PersonalProfileActivity.class);
        intent.putExtra("userId", userId);
        mContext.startActivity(intent);
    }
}
