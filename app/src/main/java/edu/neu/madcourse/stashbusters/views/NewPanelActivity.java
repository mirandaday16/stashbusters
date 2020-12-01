package edu.neu.madcourse.stashbusters.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.contracts.NewPanelContract;
import edu.neu.madcourse.stashbusters.enums.MaterialType;
import edu.neu.madcourse.stashbusters.presenters.NewPanelPresenter;


public class NewPanelActivity extends AppCompatActivity implements NewPanelContract.MvpView {
    private RadioGroup materialGroup;

    private FirebaseAuth mAuth;
    private NewPanelPresenter mPresenter;
    private int clickedMaterial;
    private ImageButton imageButton;
    private static final int RECORD_REQUEST_CODE = 101;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_panel);

        // Setting up radio buttons for material types.
        materialGroup = findViewById(R.id.materialGroup);
        setUpMaterialGroup();

        // Setting up Firebase
        mAuth = FirebaseAuth.getInstance();

        // Setting up presenter
        mPresenter = new NewPanelPresenter(this);

        // Getting layout views
        imageButton = (ImageButton) findViewById(R.id.imageButton);

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

    @Override
    public FirebaseAuth getmAuth() {
        return mAuth;
    }


    private void setImage(Bitmap imageBitmap) {
        imageButton.setImageBitmap(imageBitmap);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButton:
                takePhoto();
                break;
            case R.id.postButton:
                break;
            default:
                break;
        }

    }

    /**
     * Function to open the camera app.
     */
    private void takePhoto() {

        // Check for permissions.
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        // If permission is granted, then open the camera app.
        if (permission == PackageManager.PERMISSION_GRANTED) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (ActivityNotFoundException e) {
                // display error state to the user
            }
        }
        // If permission is not granted, then request permission.
        else {
            makePermissionRequest();
        }
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

    private void onPhotoActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            setImage(imageBitmap);
        }
    }
}