package edu.neu.madcourse.stashbusters.contracts;

import android.content.Intent;
import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Defines the contract between the View {@link edu.neu.madcourse.stashbusters.views.NewPanelActivity}
 * and the Presenter {@link edu.neu.madcourse.stashbusters.presenters.NewPanelPresenter}.
 * This is for adding a new stash panel post.
 */
public interface NewPanelContract {
    interface MvpView {
        FirebaseAuth getmAuth();
//        void setImage(Bitmap imageBitmap);
//        void showToastMessage(String msg);
    }

    interface Presenter {
//        void takePhoto();
//        void onPhotoActivityResult(int requestCode, int resultCode, Intent data);
//        void createNewAccount();
//        void validateNewUser(String username, String password);
//        void startWorldFeedActivity();
    }
}
