package edu.neu.madcourse.stashbusters.contracts;

import android.content.Context;
import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.stashbusters.CommentRVAdapter;
import edu.neu.madcourse.stashbusters.model.Comment;

public interface PostContract {
    interface MvpView {
        void setAuthorViewData(String username,
                               String profilePicUrl);

        void setPostViewData(String title,
                             String postPicUrl,
                             String description,
                             long createdDate);

        void setCommentAdapter(CommentRVAdapter commentsAdapter);
    }

    interface Presenter {
        void loadAuthorDataToView();
        void loadPostDataToView();
        void loadCommentDataToView(Context context);
        void uploadComment(Comment comment);

    }
}
