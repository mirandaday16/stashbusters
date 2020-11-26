package edu.neu.madcourse.stashbusters.contracts;

/**
 * Defines the contract between the View {@link edu.neu.madcourse.stashbusters.views.ProfileActivity}
 * and the Presenter {@link edu.neu.madcourse.stashbusters.presenters.ProfilePresenter}.
 */
public interface ProfileContract {
    interface MvpView {
        /**
         * Updated the UI with provided info.
         * @param photoUrl URL of user profile photo
         * @param username username
         * @param bio user's bio
         * @param followerCount user's follower count
         */
        void setViewData(String photoUrl, String username, String bio, String followerCount);

        void setEditProfileBtnVisibility(int type);
        void setMyPostBtnVisibility(int type);
        // TODO: set list of user's posts
    }

    interface Presenter {
        /**
         * Load data from database so view can update the UI.
         */
        void loadDataToView();

        // TODO: void displayUsersPosts();

    }
}
