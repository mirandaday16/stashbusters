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

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.stashbusters.SnackBustingActivity;
import edu.neu.madcourse.stashbusters.contracts.NewSnackContract;
import edu.neu.madcourse.stashbusters.model.SnackBustChoice;
import edu.neu.madcourse.stashbusters.model.SnackBustPost;
import edu.neu.madcourse.stashbusters.views.NewSnackActivity;

import static edu.neu.madcourse.stashbusters.utils.Utils.showToast;

/**
 * This class is responsible for handling actions from the View and updating the UI as required.
 */
public class NewSnackPresenter implements NewSnackContract.Presenter{
    private static final String TAG = NewSnackActivity.class.getSimpleName();
    private NewSnackContract.MvpView mView;
    private Context mContext;

    private DatabaseReference mDatabase;
    private StorageReference storageRef;
    private DatabaseReference userPostsRef;
    private FirebaseAuth mAuth;

    private String userId; // owner of the profile

    public NewSnackPresenter(Context context) {
        this.mContext = context;
        this.mView = (NewSnackContract.MvpView) context;

        storageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userId = mAuth.getCurrentUser().getUid();
        userPostsRef = mDatabase.child("snackPosts").child(userId);
    }

    /**
     * Function that attempts to upload the post when the postButton is clicked.
     */
    @Override
    public void onPostButtonClick(String question, String choice_one, String choice_two, Uri uri) {
        if (validateText(question)
                && validateText(choice_one)
                && validateText(choice_two)
                && validateUri(uri)) {
            uploadPhotoToStorage(question, choice_one, choice_two, uri);
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
    private void uploadPhotoToStorage(final String question, final String choice_one, final String choice_two, Uri photoUri) {
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

                            uploadPost(question, choice_one, choice_two, userPhotoUrl);
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
    private void uploadPost(final String question, final String choice_one, final String choice_two, String photoUrl) {
        SnackBustChoice one = new SnackBustChoice(choice_one);
        SnackBustChoice two = new SnackBustChoice(choice_two);
        List<SnackBustChoice> choices = new ArrayList<>();
        choices.add(one);
        choices.add(two);
        SnackBustPost post = new SnackBustPost(question, photoUrl, choices);

        DatabaseReference newUserPostRef = userPostsRef.push(); // push used to generate unique id
        post.setId(newUserPostRef.getKey());
        newUserPostRef.setValue(post);

        String postId = newUserPostRef.getKey();

        startStashBustingActivity(postId);
    }

    /**
     * Function that switches to SnackBustingActivity.
     */
    public void startStashBustingActivity(String postId) {
        Intent intent = new Intent(mContext, SnackBustingActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("postId", postId);
        mContext.startActivity(intent);
        mView.finishActivity();
    }

    /**
     * Helper function to check text is not empty.
     */
    private boolean validateText(String text) {
        return !text.equals("") && text != null;
    }

    /**
     * Helper function to check Uri is not empty.
     */
    private boolean validateUri(Uri uri) {
        return uri != null;
    }

}
