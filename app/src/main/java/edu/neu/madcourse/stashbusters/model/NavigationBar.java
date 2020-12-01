package edu.neu.madcourse.stashbusters.model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import edu.neu.madcourse.stashbusters.SnackBustingActivity;
import edu.neu.madcourse.stashbusters.views.NewPostActivity;
import edu.neu.madcourse.stashbusters.views.PersonalProfileActivity;

public class NavigationBar {

    public NavigationBar (final Context context, ImageButton myFeedButton,
                          ImageButton worldFeedButton, ImageButton newPostButton,
                          ImageButton myProfileButton, ImageButton snackBustButton) {

        // Set onClickListeners for all 5 buttons
        myFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send user to MyFeed Activity
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
                Intent intent = new Intent(context, SnackBustingActivity.class);
                context.startActivity(intent);
            }
        });

    }



}
