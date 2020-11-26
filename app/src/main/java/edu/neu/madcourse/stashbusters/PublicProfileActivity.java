package edu.neu.madcourse.stashbusters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import edu.neu.madcourse.stashbusters.databinding.PublicProfileActivityBinding;

public class PublicProfileActivity extends AppCompatActivity {

    // Set up ViewBinding for the layout
    private PublicProfileActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_profile_activity);

        // Setting up binding instance and view instances
        binding = PublicProfileActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        final TextView usernameView = binding.usernameDisplay;
        final ImageView profilePic = binding.profilePicture;
        final TextView followerCountView = binding.followerCount;
        final TextView bio = binding.bio;
        final Button followButton = binding.followButton;
        final RecyclerView userPostsFeed = binding.postViewArea;

        // Setting up onClickListener for Follow Button - should switch to "Followed" and a darker
        // color if the user is following this profile
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Change color/text depending on following status (get from Firebase)
                // TODO: When clicked, edit following status in Firebase (either add or remove user
                //  from following list)
            }
        });

        setContentView(view);
    }
}