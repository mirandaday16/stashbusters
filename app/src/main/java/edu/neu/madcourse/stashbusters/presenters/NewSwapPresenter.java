package edu.neu.madcourse.stashbusters.presenters;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import edu.neu.madcourse.stashbusters.views.SwapPostActivity;
import edu.neu.madcourse.stashbusters.contracts.NewSwapContract;
import edu.neu.madcourse.stashbusters.enums.MaterialType;
import edu.neu.madcourse.stashbusters.model.StashPanelPost;
import edu.neu.madcourse.stashbusters.model.StashSwapPost;
import edu.neu.madcourse.stashbusters.views.NewSwapActivity;

import static edu.neu.madcourse.stashbusters.utils.Utils.showToast;

/**
 * This class is responsible for handling actions from the View and updating the UI as required.
 */
public class NewSwapPresenter implements NewSwapContract.Presenter{
    private static final String TAG = NewSwapActivity.class.getSimpleName();
    private NewSwapContract.MvpView mView;
    private Context mContext;

    private DatabaseReference mDatabase;
    private StorageReference storageRef;
    private DatabaseReference userPostsRef;
    private FirebaseAuth mAuth;

    private String userId; // owner of the profile

    public NewSwapPresenter(Context context) {
        this.mContext = context;
        this.mView = (NewSwapContract.MvpView) context;

        storageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userId = mAuth.getCurrentUser().getUid();
        userPostsRef = mDatabase.child("swapPosts").child(userId);
    }

    /**
     * Function that attempts to upload the post when the postButton is clicked.
     */
    @Override
    public void onPostButtonClick(String title, String description, int material, Uri uri, String desiredMaterial) {
        if (validateText(title)
                && validateText(description)
                && validateRadio(material)
                && validateUri(uri)
                && validateText(desiredMaterial)) {
            uploadPhotoToStorage(title, description, material, uri, desiredMaterial);
        } else {
            showToast(mContext, "Please fill all fields.");
        }
    }

    /**
     * Function that tells the View what to do when the imageButton is clicked.
     */
    @Override
    public void onImageButtonClick() {
        mView.takePhoto();
    }

    /**
     * Function to upload post photo Firebase storage and get the url.
     */
    private void uploadPhotoToStorage(final String title, final String description, final int material, Uri photoUri, final String desiredMaterial) {
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
                Log.i(TAG, "uploadPostPhotoToStorage:success");

                // Save url
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
                            String userPhotoUrl = task.getResult().toString();

                            uploadPost(title, description, material, userPhotoUrl, desiredMaterial);
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });
            }
        });
    }

    /**
     * Function to save post to Firebase.
     */
    private void uploadPost(final String title, final String description, int material, final String photoUrl, final String desiredMaterial) {
        DatabaseReference newUserPostRef = userPostsRef.push(); // push used to generate unique id
        StashSwapPost newPost = new StashSwapPost(title, description, photoUrl, desiredMaterial);
        MaterialType mat = MaterialType.getByInt(material);
        newPost.setMaterialType(mat);
        newUserPostRef.setValue(newPost);

        String postId = newUserPostRef.getKey();

        startStashSwapActivity(postId);
    }

    /**
     * Function that switches to SwapPostActivity.
     */
    public void startStashSwapActivity(String postId) {
        Intent intent = new Intent(mContext, SwapPostActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("postId", postId);
        mContext.startActivity(intent);
        mView.finishActivity();
    }

    /**
     * Helper function to check text is not empty.
     */
    private boolean validateText(String text) {
        if (text.equals("") || text == null) {
            return false;
        }
        return true;
    }

    /**
     * Helper function to check Uri is not empty.
     */
    private boolean validateUri(Uri uri) {
        return uri != null;
    }

    /**
     * Helper function to check int is not -1.
     */
    private boolean validateRadio(int number) {
        return number != -1;
    }

}
