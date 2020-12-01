package edu.neu.madcourse.stashbusters.presenters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;

import com.google.firebase.auth.FirebaseAuth;

import edu.neu.madcourse.stashbusters.contracts.LoginContract;
import edu.neu.madcourse.stashbusters.contracts.NewPanelContract;

import static android.app.Activity.RESULT_OK;

public class NewPanelPresenter implements NewPanelContract.Presenter{
    private NewPanelContract.MvpView mView;
    private Context mContext;
    private FirebaseAuth mAuth;
    private String userId; // owner of the profile


//    static final int REQUEST_IMAGE_CAPTURE = 1;

    public NewPanelPresenter(Context context) {
        this.mContext = context;
        this.mView = (NewPanelContract.MvpView) context;
        mAuth = mView.getmAuth();
    }

//    @Override
//    public void takePhoto() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        try {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        } catch (ActivityNotFoundException e) {
//            // display error state to the user
//        }
//    }

//    @Override
//    public void onPhotoActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mView.setImage(imageBitmap);
//        }
//    }
}
