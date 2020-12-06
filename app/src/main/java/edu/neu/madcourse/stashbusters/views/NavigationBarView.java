package edu.neu.madcourse.stashbusters.views;

// A navigation bar that navigates between the 5 major activities of the app

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.YourFeedActivity;
import edu.neu.madcourse.stashbusters.enums.NavigationBarButtons;

// A navigation bar that navigates between the 5 major activities of the app
public class NavigationBarView extends FrameLayout {

    private ImageButton myFeedButton;
    private  ImageButton worldFeedButton;
    private  ImageButton newPostButton;
    private  ImageButton myProfileButton;
    private  ImageButton snackBustButton;

    public NavigationBarView(@NonNull final Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.navigation_bar, this);
        this.myFeedButton = findViewById(R.id.myFeedButton);
        this.worldFeedButton = findViewById(R.id.worldFeedButton);
        this.newPostButton = findViewById(R.id.newPostButton);
        this.myProfileButton = findViewById(R.id.myProfileButton);
        this.snackBustButton = findViewById(R.id.snackBustingButton);

        // Set onClickListeners for all 5 buttons
        myFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, YourFeedActivity.class);
                context.startActivity(intent);
            }
        });

        worldFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send user to World Feed Activity
            }
        });

        newPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewPostActivity.class);
                context.startActivity(intent);
            }
        });

        myProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PersonalProfileActivity.class);
                context.startActivity(intent);
            }
        });

        snackBustButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SnackPostActivity.class);
                context.startActivity(intent);
            }
        });

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
