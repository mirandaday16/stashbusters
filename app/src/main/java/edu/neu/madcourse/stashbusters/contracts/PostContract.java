package edu.neu.madcourse.stashbusters.contracts;


import com.google.firebase.database.DatabaseReference;

import android.content.Context;

import edu.neu.madcourse.stashbusters.adapters.CommentRVAdapter;
import edu.neu.madcourse.stashbusters.model.Comment;


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

//        void setPostViewData(String title,
//                             String postPicUrl,
//                             String description,
//                             long createdDate);

        void setCommentAdapter(CommentRVAdapter commentsAdapter);

    }

    interface Presenter {
        void loadAuthorDataToView();

        void loadPostDataToView();

        void onHeartIconClick(DatabaseReference postRef);


        void loadCommentDataToView(Context context);

        void uploadComment(DatabaseReference postRef, Comment comment);
    }
}
