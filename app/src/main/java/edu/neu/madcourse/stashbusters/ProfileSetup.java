package edu.neu.madcourse.stashbusters;

/**
 * Specific to user profile.
 */
public interface ProfileSetup {
    /**
     * Setup user profile view
     */
    interface View {
        void setViewData(String photoUrl, String username, String bio);
    }

    interface Presenter {
        void loadDataToView();
    }
}
