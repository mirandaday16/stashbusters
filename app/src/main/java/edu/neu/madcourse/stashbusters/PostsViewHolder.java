package edu.neu.madcourse.stashbusters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class PostsViewHolder extends RecyclerView.ViewHolder{
    private View mView;
    private String currentUserID;
    private TextView username, headline;
    private ImageView postImage;

    public PostsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.mView = itemView;

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        postImage = mView.findViewById(R.id.user_post_image);
        headline = mView.findViewById(R.id.user_post_headline);
        username = mView.findViewById(R.id.user_post_username);
    }

    public void setPostPhoto(String photoUrl) {
        Picasso.get().load(photoUrl).into(postImage);
    }

    public void setHeadline(String inputHeadline) {
        headline.setText(inputHeadline);
    }

    public void setUsername(String inputUsername) {
        username.setText(inputUsername);
    }
}
