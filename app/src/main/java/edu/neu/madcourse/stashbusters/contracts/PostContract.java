package edu.neu.madcourse.stashbusters.contracts;

public interface PostContract {
    interface MvpView {
        void setAuthorViewData(String username,
                               String profilePicUrl);

        void setPostViewData(String title,
                             String postPicUrl,
                             String description,
                             long createdDate);
        boolean getCurrentUserLikedPostStatus();
        void setCurrentUserLikedPostStatus(boolean likeStatus);
    }

    interface Presenter {
        void loadAuthorDataToView();
        void loadPostDataToView();
        void onHeartIconClick();
    }
}
