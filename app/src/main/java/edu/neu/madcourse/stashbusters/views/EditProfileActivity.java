package edu.neu.madcourse.stashbusters.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import edu.neu.madcourse.stashbusters.contracts.EditProfileContract;
import edu.neu.madcourse.stashbusters.databinding.EditAccountActivityBinding;
import edu.neu.madcourse.stashbusters.presenters.EditProfilePresenter;

public class EditProfileActivity extends AppCompatActivity implements EditProfileContract.MvpView {
    private static final String TAG = PersonalProfileActivity.class.getSimpleName();
    private static int REQUEST_CODE = 1;

    // Set up ViewBinding for the layout
    private EditAccountActivityBinding binding;
    private EditProfileContract.Presenter mPresenter;
    private String currentUserId, newPhotoUrl;
    private EditText bioEditText;
    private Button saveButton;
    private ImageView imageButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        currentUserId = intent.getStringExtra("userId");

        binding = EditAccountActivityBinding.inflate(getLayoutInflater());
        mPresenter = new EditProfilePresenter(this);

        bioEditText = binding.bioInput;
        imageButton = binding.imageButton;
        saveButton = binding.saveButton;
        newPhotoUrl = "";

        initListeners();
        initContent();
        setContentView(binding.getRoot());
    }

    private void initListeners() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onProfilePictureButtonClick();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String newBio = bioEditText.getText().toString();
                mPresenter.updateProfile(newPhotoUrl, newBio);
            }
        });
    }

    // load existing data to view
    private void initContent() {
        mPresenter.loadProfile();
    }

    @Override
    public void setViewData(String photo, String bio) {
        setProfilePhoto(photo);
        setBio(bio);
    }

    @Override
    public void setBio(String bio) {
        bioEditText.setText(bio);
    }

    @Override
    public void setProfilePhoto(String photoUrl) {
        Picasso.get().load(photoUrl).into(imageButton);
    }

    public void setNewPhotoUrl(String newPhotoUrl) {
        this.newPhotoUrl = newPhotoUrl;
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
            if (requestCode == 1) {
                if (resultCode == RESULT_OK && data != null) {
                    Uri photoUri = data.getData();
                    mPresenter.uploadUserProfilePhotoToStorage(photoUri);
                }
            }
        }
    }

    public void redirectToPersonalProfile() {
        Intent intent = new Intent(this, PersonalProfileActivity.class);
        intent.putExtra("userId", currentUserId);
        finish();
        startActivity(intent);
    }
}
