package edu.neu.madcourse.stashbusters.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import edu.neu.madcourse.stashbusters.contracts.EditProfileContract;
import edu.neu.madcourse.stashbusters.views.PersonalProfileActivity;

public class EditProfilePresenter implements EditProfileContract.Presenter {
    private static final String TAG = PersonalProfilePresenter.class.getSimpleName();

    private EditProfileContract.MvpView mView;
    private Context mContext;

    private String existingPhotoUrl, existingBio;

    private String userId;
    private FirebaseAuth mAuth;
    private StorageReference storageRef;
    private DatabaseReference userProfileRef;

    public EditProfilePresenter(Context context) {
        this.mView = (EditProfileContract.MvpView) context;
        this.mContext = context;

        storageRef = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        userProfileRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
    }

    @Override
    public void onProfilePictureButtonClick() {
        mView.selectImage();
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
                            String newPhotoUploadedUrl = task.getResult().toString();
                            // Pass this info back to the View
                            mView.setNewPhotoUrl(newPhotoUploadedUrl);
                            mView.setProfilePhoto(newPhotoUploadedUrl);
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });
            }
        });
    }

    @Override
    public void loadProfile() {
        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    existingPhotoUrl = snapshot.child("photoUrl").getValue().toString();
                    existingBio = snapshot.child("bio").getValue().toString();
                    mView.setViewData(existingPhotoUrl, existingBio);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }
        });
    }

    @Override
    public void updateProfile(String newPhotoUrl, String newBio) {
        // only update both fields if new photo url is updated (not empty) and new bio is
        // changed (not the same as current existing bio)
        if (!newPhotoUrl.equals("") && !newBio.equals(existingBio)) {
            // save data to DB
            Map<String, Object> updates = new HashMap<>();
            updates.put("bio", newBio);
            updates.put("photoUrl", newPhotoUrl);

            userProfileRef.updateChildren(updates);
        } else if (!newBio.equals(existingBio)) {
            // user updates bio
            updateBio(newBio);
        } else if (!newPhotoUrl.equals("")) {
            // user updates photo
            updatePhoto(newPhotoUrl);
        }

        // nothing changed -- return to user profile
        Log.i(TAG, "updateProfile:done - redirecting to user profile");
        mView.redirectToPersonalProfile();
    }

    @Override
    public void updateBio(String newBio) {
        userProfileRef.child("bio").setValue(newBio);
    }


    @Override
    public void updatePhoto(String newPhotoUrl) {
        userProfileRef.child("photoUrl").setValue(newPhotoUrl);
    }
}
