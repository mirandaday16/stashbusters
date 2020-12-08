package edu.neu.madcourse.stashbusters.contracts;

public interface SwapPostContract extends PostContract{
    interface MvpView {
        void setPostViewData(String title,
                             String postPicUrl,
                             String description,
                             long createdDate,
                             String material,
                             Boolean isAvailable,
                             long likeCount);
    }
}
