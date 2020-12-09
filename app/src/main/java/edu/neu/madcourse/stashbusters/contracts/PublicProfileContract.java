package edu.neu.madcourse.stashbusters.contracts;

public interface PublicProfileContract extends ProfileContract {
    interface MvpView extends ProfileContract.MvpView{
        void updateFollowButton(String text);
    }
}
