package edu.neu.madcourse.stashbusters.contracts;

/**
 * Defines the contract between the View {@link edu.neu.madcourse.stashbusters.views.EditProfileActivity}
 * and the Presenter {@link edu.neu.madcourse.stashbusters.presenters.EditProfilePresenter}.
 */
public interface EditProfileContract {

    interface MvpView {

        void setBio(String bio);
        void setProfilePhoto(String photoUrl);

    }

    interface Presenter {
        void updateProfile();
    }
}
