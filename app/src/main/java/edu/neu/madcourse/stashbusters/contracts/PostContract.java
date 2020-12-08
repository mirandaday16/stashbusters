package edu.neu.madcourse.stashbusters.contracts;

import com.google.firebase.database.DatabaseReference;

public interface PostContract {
    interface MvpView {
        void setAuthorViewData(String username,
                               String profilePicUrl);
//
//        // for panel post
//        void setPostViewData(String title,
//                             String postPicUrl,
//                             String description,
//                             long createdDate);

        // for swap post

        void setNewLikeCount(long newLikeCount);
        void updateHeartIconDisplay(boolean status);
        boolean getCurrentUserLikedPostStatus();
        void setCurrentUserLikedPostStatus(boolean likeStatus);
    }

    interface Presenter {
        void loadAuthorDataToView();
        void loadPostDataToView();
        void onHeartIconClick(DatabaseReference postRef);
    }
}
