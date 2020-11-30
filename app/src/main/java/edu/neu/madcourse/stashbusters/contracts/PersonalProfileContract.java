package edu.neu.madcourse.stashbusters.contracts;

import android.graphics.Bitmap;
import android.view.MenuItem;

import edu.neu.madcourse.stashbusters.presenters.PersonalProfilePresenter;
import edu.neu.madcourse.stashbusters.views.PersonalProfileActivity;

/**
 * Defines the contract between the View {@link PersonalProfileActivity}
 * and the Presenter {@link PersonalProfilePresenter}.
 * This is for a user's private profile.
 */
public interface PersonalProfileContract {
    interface MvpView {
        /**
         * Updated the UI with provided info.
         * @param photo user profile photo
         * @param username username
         * @param bio user's bio
         * @param followerCount user's follower count
         */
        void setViewData(String photo, String username, String bio, String followerCount);
        // TODO: set list of user's posts
    }

    interface Presenter {
        /**
         * Load data from database so view can update the UI.
         */
        void loadDataToView();
        void onEditProfileButtonClick(String userId);
        boolean onToolbarClick(MenuItem item);
        void onNewPostButtonClick();
        void onMyProfileButtonClick();
        void onSnackBustingButtonClick();
        // TODO: void displayUsersPosts();

    }
}
