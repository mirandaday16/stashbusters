package edu.neu.madcourse.stashbusters.contracts;

public interface PublicProfileContract {
    interface MvpView {
        void setViewData(String photoUrl,
                         String inputUsername,
                         String inputBio,
                         String inputFollowerCount);
    }

    interface Presenter {
        void onSnackBustingButtonClick();
        void onMyProfileButtonClick();
        void onNewPostButtonClick();

        /**
         * Load current user's data from Firebase to view.
         */
        void loadDataToView();
    }
}
