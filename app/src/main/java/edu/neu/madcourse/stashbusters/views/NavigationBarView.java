package edu.neu.madcourse.stashbusters.views;

// A navigation bar that navigates between the 5 major activities of the app

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.enums.NavigationBarButtons;

// A navigation bar that navigates between the 5 major activities of the app
public class NavigationBarView extends FrameLayout {

    private ImageButton myFeedButton;
    private  ImageButton worldFeedButton;
    private  ImageButton newPostButton;
    private  ImageButton myProfileButton;
    private  ImageButton snackBustButton;

    public NavigationBarView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.navigation_bar, this);
        this.myFeedButton = findViewById(R.id.myFeedButton);
        this.worldFeedButton = findViewById(R.id.worldFeedButton);
        this.newPostButton = findViewById(R.id.newPostButton);
        this.myProfileButton = findViewById(R.id.myProfileButton);
        this.snackBustButton = findViewById(R.id.snackBustingButton);

    }

    public ImageButton getMyFeedButton() {
        return myFeedButton;
    }

    public ImageButton getWorldFeedButton() {
        return worldFeedButton;
    }

    public ImageButton getNewPostButton() {
        return newPostButton;
    }

    public ImageButton getMyProfileButton() {
        return myProfileButton;
    }

    public ImageButton getSnackBustButton() {
        return snackBustButton;
    }

    public void setSelected(Enum<NavigationBarButtons> activityName) {
        int selectedColor = getResources().getColor(R.color.colorSecondary);
        if (activityName == NavigationBarButtons.MYFEED) {
            myFeedButton.setBackgroundColor(selectedColor);
        }
        else if (activityName == NavigationBarButtons.WORLDFEED) {
            worldFeedButton.setBackgroundColor(selectedColor);
        }
        else if (activityName == NavigationBarButtons.NEWPOST) {
            newPostButton.setBackgroundColor(selectedColor);
        }
        else if (activityName == NavigationBarButtons.MYPROFILE) {
            myProfileButton.setBackgroundColor(selectedColor);
        }
        else if (activityName == NavigationBarButtons.SNACKBUST) {
            snackBustButton.setBackgroundColor(selectedColor);
        }
    }
}
