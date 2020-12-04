package edu.neu.madcourse.stashbusters.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.model.SnackBustPost;
import edu.neu.madcourse.stashbusters.model.User;

public class SnackRVAdapter extends RecyclerView.Adapter<SnackRVAdapter.SnackViewHolder> {
    SnackBustPost post;
    User author;

    SnackRVAdapter(SnackBustPost snack, User author){
        this.post = snack;
        this.author = author;
    }

    static class SnackViewHolder extends RecyclerView.ViewHolder {
        private ImageView snackImage;
        private TextView questionText;
        private TextView choiceOne;
        private TextView choiceTwo;
        private TextView username;
        private ImageView profilePic;
        private Handler imageHandler = new Handler();

        SnackViewHolder (View itemView){
            super(itemView);
            snackImage = (ImageView) itemView.findViewById(R.id.snack_image);
            profilePic = (ImageView) itemView.findViewById(R.id.profile_pic);
            questionText = (TextView) itemView.findViewById(R.id.question_text);
            choiceOne = (TextView) itemView.findViewById(R.id.choice_one);
            choiceTwo = (TextView) itemView.findViewById(R.id.choice_two);
            username = (TextView) itemView.findViewById(R.id.username);
        }
    }

    public void nextSnack(SnackBustPost snack) {
        post = snack;
    }

    @NonNull
    @Override
    public SnackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_snack_busting, parent, false);
        return new SnackViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SnackViewHolder holder, final int position) {
        // Fill layout with snack bust post data
        holder.questionText.setText(post.getTitle());
        holder.choiceOne.setText(post.getChoiceList().get(0).getText());
        holder.choiceTwo.setText(post.getChoiceList().get(1).getText());
        holder.username.setText(author.getUsername());

        // Load in the snack image in a separate thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                setImageView(holder, post.getPhotoUrl(), holder.snackImage);
            }
        }).start();

        // Load in the user image in a separate thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                setImageView(holder, post.getPhotoUrl(), holder.profilePic);
            }
        }).start();
    }

    // Function to load in an image from an image URL.
    private void setImageView(final SnackViewHolder holder, String url, final ImageView iView) {
        Bitmap bitmap = null;
        try {
            InputStream in = new
                    URL(url)
                    .openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Bitmap finalBitmap = bitmap;
        holder.imageHandler.post(new Runnable() {
            @Override
            public void run() {
                iView.setImageBitmap(finalBitmap);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
