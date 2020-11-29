package edu.neu.madcourse.stashbusters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import edu.neu.madcourse.stashbusters.databinding.NewAccountActivityBinding;
import edu.neu.madcourse.stashbusters.model.User;
import edu.neu.madcourse.stashbusters.views.PersonalProfileActivity;


public class NewAccountActivity extends AppCompatActivity {
    private static final String TAG = NewAccountActivity.class.getSimpleName();
    private static int REQUEST_CODE = 1;
    private String deviceToken;

    private DatabaseReference mDatabase;
    private StorageReference storageRef;
    private DatabaseReference usersRef;
    private SharedPreferences prefs;
    private String sharedUsername = "username";

    // Set up ViewBinding for the layout
    private NewAccountActivityBinding binding;
    private Bitmap profilePicFile;
    private Bitmap photoBitmap;
    private Uri photoUri;
    private String profilePicUrl = ""; // TODO: might want to set default url here

    // Set up a FirebaseAuth object to save the new account
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_account_activity);

        // Setting up binding instance and view instances
        binding = NewAccountActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        final EditText emailField = binding.emailInput;
        final EditText usernameField = binding.usernameInput;
        final EditText passwordField = binding.passwordInput;
        final ImageButton profilePicButton = binding.imageButton;
        final EditText bioField = binding.bioInput;
        final Button saveButton = binding.saveButton;

        storageRef = FirebaseStorage.getInstance().getReference();

        // Initializing auth object for Firebase account creation
        mAuth = FirebaseAuth.getInstance();

        // Shared Preferences for saving login cred
        prefs = getSharedPreferences(sharedUsername, MODE_PRIVATE);
        // Initialize DB and users refs
        mDatabase = FirebaseDatabase.getInstance().getReference();
        usersRef = mDatabase.child("users");

        // update device token with user's device token
        updateDeviceToken();
        Log.d(TAG, "Device token: " + this.deviceToken);

        // Setting onClickListener for Profile Picture Button - opens gallery for user to
        // choose a profile picture
        // TODO: this should be separated to its presenter
        profilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GalleryImagePicker profilePicPicker = new GalleryImagePicker(1);
                selectImage();
                profilePicFile = profilePicPicker.getBitmap();

                profilePicButton.setImageBitmap(profilePicFile);
            }


        });

        // Setting onClickListener for Save Button - gathers information entered in form and saves
        // in Firebase
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailAddress = emailField.getText().toString();
                String password = passwordField.getText().toString();
                final String username = usernameField.getText().toString();
                final String bio = bioField.getText().toString();

                // Check username validity -- certain special characters not allowed in Firebase
                final ArrayList<String> invalidCharacters = new ArrayList<String>();
                invalidCharacters.add(".");
                invalidCharacters.add("#");
                invalidCharacters.add("$");
                invalidCharacters.add("[");
                invalidCharacters.add("]");
                for (String character : invalidCharacters) {
                    if (username.contains(character)) {
                        Toast.makeText(NewAccountActivity.this, "Please choose a username without special characters.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // Check password validity
                if (password.length() > 16 || password.length() < 8) {
                    Toast.makeText(NewAccountActivity.this,
                            "Please choose a password between 8-16 characters.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(emailAddress, password)
                            .addOnCompleteListener(NewAccountActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, create user object and node in Firebase DB
                                        Log.d(TAG, "createUserWithEmail:success");
                                        User user = new User(username, deviceToken);
                                        user.setEmailAddress(emailAddress);
                                        user.setProfilePicture(profilePicUrl);
                                        System.out.println("in creating user " + user.getProfilePic());
                                        user.setBio(bio);

                                        usersRef.child(username).setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "User saved to DB successfully.");

                                                        // save prefs
                                                        SharedPreferences.Editor preferencesEditor = prefs.edit();
                                                        preferencesEditor.putString("username", username);
                                                        preferencesEditor.apply();

                                                        // go to World Feed Activity
                                                        startWorldFeedActivity(username);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Data could not be saved: " + e);
                                                    }
                                                });
//                                        onSaveNewAccount(user, username, profilePicFile, bio);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(NewAccountActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        setContentView(view);

    }

     /*
    // Helper functions:
    */

    // User can select an image from their device gallery to use a a profile picture
    private void selectImage() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        photoBitmap = selectedImage;
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        System.out.println("Selected image URL " + selectedImage);
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                System.out.println("Photo path " + picturePath);

//                                photoUri = fileToUri(picturePath);
                                photoUri = selectedImage;
                                System.out.println("Photo path toString" + photoUri.toString());

                                // TODO: What a mess -- upload to DB
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
                                        System.out.println("SUCCESSFULY UPLOAD PHOTO TO DB");

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
                                                    profilePicUrl = userPhotoUri.toString();

                                                    System.out.println("profilePicUrl Updated " + profilePicUrl);
                                                } else {
                                                    // Handle failures
                                                    // ...
                                                }
                                            }
                                        });
                                    }
                                });

                                photoBitmap = BitmapFactory.decodeFile(picturePath);
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }


    // Stores profile picture, email, and bio in Firebase under user's node
    private void onSaveNewAccount(User user, final String username, final String profilePic, final String bio) {
        if (user != null) {
            user.setUsername(username);
            user.setProfilePicture(profilePic);
            user.setBio(bio);
        }
    }

    // Starts World Feed Activity
    private void startWorldFeedActivity(String username) {
        // TODO: When World Feed activity exists, change this function to go  to World Feed
        Intent intent = new Intent(this, PersonalProfileActivity.class);
        intent.putExtra("userId", username);
        startActivity(intent);
    }

    // Gets device token from current device
    private void updateDeviceToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // get new FCM registration token
                        NewAccountActivity.this.deviceToken = task.getResult();
                        Log.d(TAG, "Fetched device token successfully: " + NewAccountActivity.this.deviceToken);
                    }
                });
    }

}