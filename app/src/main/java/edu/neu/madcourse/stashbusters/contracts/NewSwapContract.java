package edu.neu.madcourse.stashbusters.contracts;

import android.net.Uri;

/**
 * Defines the contract between the View {@link edu.neu.madcourse.stashbusters.views.NewSwapActivity}
 * and the Presenter {@link edu.neu.madcourse.stashbusters.presenters.NewSwapPresenter}.
 * This is for adding a new stash panel post.
 */
public interface NewSwapContract {
    interface MvpView {
        void finishActivity();
    }

    interface Presenter {
        /**
         * Function that attempts to upload the post to Firebase when the postButton is clicked.
         */
        void onPostButtonClick(String title, String description, int material, Uri uri, String desiredMaterial);
    }
}
