package edu.neu.madcourse.stashbusters.contracts;

import android.net.Uri;

import edu.neu.madcourse.stashbusters.views.NewAccountActivity;

/**
 * Defines the contract between the View {@link NewAccountActivity}
 * and the Presenter {@link edu.neu.madcourse.stashbusters.presenters.LoginPresenter}.
 * This is for a user's private profile.
 */
public interface NewAccountContract {
    interface MvpView {
        /**
         * User can select an image from their device gallery to use a a profile picture
         */
        void selectImage();
        void setDeviceToken(String deviceToken);
        void setProfilePicUrl(String url);
        void setProfilePhoto(String url);
    }

    interface Presenter {
        /**
         * Updates user's device token.
         */
        void updateDeviceToken();

        /**
         * Uploads the given photo uri to Firebase storage.
         * TODO: Currently, for the purpose of the project, bucket is open for all access.
         * This should be updated to be more secure.
         * @param photoUri
         */
        void uploadUserProfilePhotoToStorage(Uri photoUri);

        void onProfilePictureButtonClick();
        boolean validateUsername(String username);
        boolean validatePassword(String password);
        void registerUser(final String emailAddress,
                          final String username,
                          String password,
                          final String profilePicUrl,
                          final String bio,
                          final String deviceToken);
    }
}
