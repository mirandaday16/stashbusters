package edu.neu.madcourse.stashbusters.contracts;

import edu.neu.madcourse.stashbusters.CommentRVAdapter;

public interface SwapPostContract extends PostContract{
    interface MvpView {
        void setPostViewData(String title,
                             String postPicUrl,
                             String description,
                             long createdDate,
                             String material,
                             Boolean isAvailable);

        void setCommentAdapter(CommentRVAdapter commentsAdapter);
    }
}
