package edu.neu.madcourse.stashbusters.contracts;
import edu.neu.madcourse.stashbusters.adapters.CommentRVAdapter;

public interface SwapPostContract extends PostContract{
    interface MvpView extends PostContract.MvpView {
        void setPostViewData(String title,
                             String postPicUrl,
                             String description,
                             long createdDate,
                             String material,
                             Boolean isAvailable,
                             long likeCount);
        void setCommentAdapter(CommentRVAdapter commentsAdapter);
    }
}
