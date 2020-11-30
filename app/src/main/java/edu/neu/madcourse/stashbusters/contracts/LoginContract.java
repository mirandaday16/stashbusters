package edu.neu.madcourse.stashbusters.contracts;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

import edu.neu.madcourse.stashbusters.presenters.PersonalProfilePresenter;
import edu.neu.madcourse.stashbusters.views.PersonalProfileActivity;

/**
 * Defines the contract between the View {@link edu.neu.madcourse.stashbusters.views.LoginActivity}
 * and the Presenter {@link edu.neu.madcourse.stashbusters.presenters.LoginPresenter}.
 * This is for a user's private profile.
 */
public interface LoginContract {
    interface MvpView {
        FirebaseAuth getmAuth();
        void showToastMessage(String msg);
    }

    interface Presenter {
        void createNewAccount();
        void validateNewUser(String username, String password);
        void startWorldFeedActivity();
    }
}
