package edu.neu.madcourse.stashbusters.presenters;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.views.EditProfileActivity;
import edu.neu.madcourse.stashbusters.views.LoginActivity;

/**
 * Responsible for handling actions from the View and updating the UI as required.
 */
public class PersonalProfilePresenter extends ProfilePresenter {

    public PersonalProfilePresenter(Context context, String userId) {
        super(context, userId);
    }

    public boolean onToolbarClick(MenuItem item) {
        if (item.getItemId() == R.id.edit_profile_menu_item) {
            startEditProfileActivity();
        } else if (item.getItemId() == R.id.log_out_menu_item) {
            FirebaseAuth.getInstance().signOut();
            startLoginActivity();
        }
        return false;
    }

    // Starts Edit Profile Activity
    private void startEditProfileActivity() {
        Intent intent = new Intent(this.mContext, EditProfileActivity.class);
        mContext.startActivity(intent);
    }

    // Starts Login Activity
    private void startLoginActivity() {
        Intent intent = new Intent(this.mContext, LoginActivity.class);
        mContext.startActivity(intent);
    }
}
