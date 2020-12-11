package edu.neu.madcourse.stashbusters.contracts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Defines the contract between the View {@link edu.neu.madcourse.stashbusters.views.NewPanelActivity}
 * and the Presenter {@link edu.neu.madcourse.stashbusters.presenters.NewPanelPresenter}.
 * This is for adding a new stash panel post.
 */
public interface NewPanelContract {
    interface MvpView {
        /**
         * Function to call finish()
         */
        void finishActivity();
    }

    interface Presenter {
        /**
         * Function that attempts to upload the post to Firebase when the postButton is clicked.
         */
        void onPostButtonClick(String title, String description, int material, Uri uri);
    }
}
