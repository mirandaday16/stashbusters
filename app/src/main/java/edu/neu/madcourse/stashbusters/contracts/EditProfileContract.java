package edu.neu.madcourse.stashbusters.contracts;

import android.net.Uri;

/**
 * Defines the contract between the View {@link edu.neu.madcourse.stashbusters.views.EditProfileActivity}
 * and the Presenter {@link edu.neu.madcourse.stashbusters.presenters.EditProfilePresenter}.
 */
public interface EditProfileContract {

    interface MvpView {
        void redirectToPersonalProfile();
        void setBio(String bio);
        void setProfilePhoto(String photoUrl);
        void setViewData(String photo, String bio);
        void setNewPhotoUrl(String photoUrl);
        void selectImage();
    }

    interface Presenter {
        /**
         * Saves updated user data to Firebase.
         */
        void updateProfile(String photoUrl, String bio);

        /**
         * Loads existing user info to the view.
         */
        void loadProfile();
        void updateBio(String newBio);
        void updatePhoto(String newPhotoUrl);
        void onProfilePictureButtonClick();
        void uploadUserProfilePhotoToStorage(Uri photoUri);
    }
}
