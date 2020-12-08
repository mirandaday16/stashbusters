package edu.neu.madcourse.stashbusters.contracts;

import android.view.MenuItem;

import edu.neu.madcourse.stashbusters.adapters.PostAdapter;

public interface ProfileContract {
    interface MvpView {
        /**
         * Updated the UI with provided info.
         * @param photo user profile photo
         * @param username username
         * @param bio user's bio
         * @param followerCount user's follower count
         */
        void setViewData(String photo, String username, String bio, String followerCount);
        void setPostListAdapter(PostAdapter adapter);
    }

    interface Presenter {
        /**
         * Load data from database so view can update the UI.
         */
        void loadDataToView();
        void getUserPostsData();

    }
}
