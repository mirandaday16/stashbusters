package edu.neu.madcourse.stashbusters.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.contracts.NewPanelContract;
import edu.neu.madcourse.stashbusters.enums.MaterialType;
import edu.neu.madcourse.stashbusters.presenters.NewPanelPresenter;

/**
 * This class is responsible for the View of adding a new stash panel post.
 */
public class NewPanelActivity extends AppCompatActivity implements NewPanelContract.MvpView {
    private RadioGroup materialGroup;

    private NewPanelPresenter mPresenter;

    private int clickedMaterial;
    private ImageButton imageButton;
    private EditText title, description;

    private static final int RECORD_REQUEST_CODE = 101;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String currentPhotoPath;
    private Uri currentPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_panel);

        // Setting up radio buttons for material types.
        materialGroup = findViewById(R.id.materialGroup);
        setUpMaterialGroup();

        // Setting up presenter
        mPresenter = new NewPanelPresenter(this);

        // Getting layout views
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        title = (EditText) findViewById(R.id.questionInput);
        description = (EditText) findViewById(R.id.choiceOne);
        clickedMaterial = -1;
    }

    /**
     * Add a radio button for each material in the MaterialType enum and set up listener.
     */
    private void setUpMaterialGroup(){
        int i = 0;
        for (MaterialType mat : MaterialType.values()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(mat.getDbCode());
            radioButton.setId(i);
            materialGroup.addView(radioButton);
            i++;
        }

        // Set a listener for the radio button group.
        materialGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                NewPanelActivity.this.clickedMaterial = materialGroup.getCheckedRadioButtonId();
            }
        });
    }

    /**
     * Function to dictate what to do upon a click.
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButton:
                mPresenter.imageButton();
                break;
            case R.id.postButton:
                mPresenter.postButton(title.getText().toString(),
                        description.getText().toString(),
                        clickedMaterial, currentPhotoUri);
                break;
            default:
                break;
        }

    }

    /**
     * Function to open the camera app and set up the URI in which to save the photo.
     * Camera code based on: https://developer.android.com/training/camera/photobasics
     */
    @Override
    public void takePhoto() {

        // Check for permissions.
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        // If permission is granted, then open the camera app.
        if (permission == PackageManager.PERMISSION_GRANTED) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "edu.neu.madcourse.stashbusters.fileprovider",
                            photoFile);
                    currentPhotoUri = photoURI;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
        // If permission is not granted, then request permission.
        else {
            makePermissionRequest();
        }
    }

    /**
     * Creates a unique file path for the image to be saved to.
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Function to request camera permission
     */
    private void makePermissionRequest() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA},
                RECORD_REQUEST_CODE);
    }

    /**
     * Function to handle response to camera permission request.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == RECORD_REQUEST_CODE) {
            // If permission is not granted, then show the alert dialog.
            // Clicking okay takes the user back one activity.
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(this);

                errorDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });

                errorDialogBuilder.setTitle("Camera Permission Denied");
                errorDialogBuilder.setMessage("You are unable to make a post without uploading a photo.");

                errorDialogBuilder.create().show();
            }
            // If permission is granted, call the takePhoto() function.
            else {
                Toast.makeText(
                        this, "Camera permission granted.",
                        Toast.LENGTH_SHORT).show();
                takePhoto();
            }
        }
    }

    /**
     * Function to handle response to end of camera activity.
     * Sets the photo taken as the thumbnail in the imageButton.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), currentPhotoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageButton.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void finishActivity(){
        finish();
    }
}