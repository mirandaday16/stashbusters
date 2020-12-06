package edu.neu.madcourse.stashbusters.contracts;

import edu.neu.madcourse.stashbusters.adapters.PostAdapter;

public interface PublicProfileContract {
    interface MvpView {
        void setViewData(String photoUrl,
                         String inputUsername,
                         String inputBio,
                         String inputFollowerCount);

        /**
         *
         */
        void updateFollowButton(String text);
        void setPostListAdapter(PostAdapter adapter);
    }

    interface Presenter {
        /**
         * Handles logic for when follow button is clicked. If not following, add following/follower
         * relationship to database. Else, remove the relationship to reflect unfollowing.
         * @param buttonText button text
         */
        void onFollowButtonClick(String buttonText);

        /**
         * Load current user's data from Firebase to view.
         */
        void loadDataToView();

        void getUserPostsData();
    }
}
