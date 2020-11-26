package edu.neu.madcourse.stashbusters.contracts;

/**
 * Defines the contract between the View {@link edu.neu.madcourse.stashbusters.views.ProfileActivity}
 * and the Presenter {@link edu.neu.madcourse.stashbusters.presenters.ProfilePresenter}.
 */
public interface ProfileContract {
    interface MvpView {
        void setViewData(String photoUrl, String username, String bio);
    }

    interface Presenter {
        void loadDataToView();
    }
}
