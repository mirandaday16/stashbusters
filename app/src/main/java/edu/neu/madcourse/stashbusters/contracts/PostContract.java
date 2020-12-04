package edu.neu.madcourse.stashbusters.contracts;

import android.net.Uri;

import edu.neu.madcourse.stashbusters.model.Comment;

public interface PostContract {
    interface MvpView {
        void setAuthorViewData(String username,
                               String profilePicUrl);

        void setPostViewData(String title,
                             String postPicUrl,
                             String description,
                             long createdDate);
    }

    interface Presenter {
        void loadAuthorDataToView();
        void loadPostDataToView();
        void uploadComment(Comment comment);

    }
}
