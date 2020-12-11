package edu.neu.madcourse.stashbusters.contracts;

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
        void showNoPostText(String likedOrMyPost);
        void updateFollowButton(String text);
    }

    interface Presenter {
        /**
         * Load user's data to show in UI.
         */
        void loadDataToView();

        /**
         * Get all user's posts data.
         */
        void getUserPostsData();
    }
}
