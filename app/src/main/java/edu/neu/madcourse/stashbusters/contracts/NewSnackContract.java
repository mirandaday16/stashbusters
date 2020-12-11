package edu.neu.madcourse.stashbusters.contracts;

import android.net.Uri;

/**
 * Defines the contract between the View {@link edu.neu.madcourse.stashbusters.views.NewSnackActivity}
 * and the Presenter {@link edu.neu.madcourse.stashbusters.presenters.NewSnackPresenter}.
 * This is for adding a new stash panel post.
 */
public interface NewSnackContract {
    interface MvpView {
        /**
         * Function to call finish()
         */
        void finishActivity();
    }

    interface Presenter {
        void onPostButtonClick(String question, String choice_one, String choice_two, Uri uri);
    }
}
