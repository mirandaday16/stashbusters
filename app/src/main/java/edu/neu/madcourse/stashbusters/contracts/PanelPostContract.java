package edu.neu.madcourse.stashbusters.contracts;

public interface PanelPostContract extends PostContract{
    interface MvpView extends PostContract.MvpView {
        void setPostViewData(String title,
                             String postPicUrl,
                             String description,
                             long createdDate,
                             long likeCount);
    }
}
