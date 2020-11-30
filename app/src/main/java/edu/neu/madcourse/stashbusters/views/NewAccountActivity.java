package edu.neu.madcourse.stashbusters.views;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import edu.neu.madcourse.stashbusters.contracts.NewAccountContract;
import edu.neu.madcourse.stashbusters.databinding.NewAccountActivityBinding;
import edu.neu.madcourse.stashbusters.presenters.NewAccountPresenter;
import edu.neu.madcourse.stashbusters.utils.Utils;

/**
 * This class is responsible for the View of signing up activity.
 */
public class NewAccountActivity extends AppCompatActivity implements NewAccountContract.MvpView{
    private static final String TAG = NewAccountActivity.class.getSimpleName();
    private static int REQUEST_CODE = 1;
    private String deviceToken;

    private String sharedUsername = "username";

    // Set up ViewBinding for the layout
    private NewAccountActivityBinding binding;
    private EditText emailField, usernameField, passwordField, bioField;
    private ImageButton profilePicButton;
    private Button saveButton;
    private Uri photoUri;
    private String profilePicUrl = ""; // TODO: might want to set default url here

    private NewAccountPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting up binding instance and view instances
        binding = NewAccountActivityBinding.inflate(getLayoutInflater());

        mPresenter = new NewAccountPresenter(this);
        // update device token with user's device token
        mPresenter.updateDeviceToken();

        initViews();
        initListeners();

        setContentView(binding.getRoot());

    }

    /* Helper Functions */
    // Initialize UI elements
    private void initViews() {
        emailField = binding.emailInput;
        usernameField = binding.usernameInput;
        passwordField = binding.passwordInput;
        profilePicButton = binding.imageButton;
        bioField = binding.bioInput;
        saveButton = binding.saveButton;
    }

    // Initialize all listeners
    private void initListeners() {
        // Setting onClickListener for Profile Picture Button - opens gallery for user to
        // choose a profile picture
        profilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onProfilePictureButtonClick();
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
                if (!mPresenter.validateUsername(username)) {
                    Utils.showToast(NewAccountActivity.this, "Please choose a username without special characters.");
                    return;
                }

                if (!mPresenter.validatePassword(password)) {
                    Utils.showToast(NewAccountActivity.this, "Please choose a password between 8-16 characters.");
                    return;
                }

                mPresenter.registerUser(emailAddress, username, password, profilePicUrl,
                        bio, deviceToken);
            }
        });
    }



    @Override
    public void selectImage() {
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
                        //TODO: What's this?
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        Bitmap photoBitmap = selectedImage;
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri photoUri = data.getData();
                        mPresenter.uploadUserProfilePhotoToStorage(photoUri);
                    }
                    break;
            }
        }
    }

    @Override
    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public void setProfilePicUrl(String url) {
        this.profilePicUrl = url;
    }
}