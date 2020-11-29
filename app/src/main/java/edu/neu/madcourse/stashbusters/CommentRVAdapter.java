package edu.neu.madcourse.stashbusters;

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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.neu.madcourse.stashbusters.model.Comment;
import edu.neu.madcourse.stashbusters.model.User;

public class CommentRVAdapter extends RecyclerView.Adapter<CommentRVAdapter.CommentViewHolder> {
    private List<Comment> comments;
    private List<User> commenters;

    CommentRVAdapter(List<Comment> comments, List<User> commenters) {
        this.comments = comments;
        this.commenters = commenters;
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView user_pic;
        TextView username;
        TextView comment;
        TextView time;
        private Handler imageHandler = new Handler();

        CommentViewHolder (View itemView) {
            super(itemView);

            user_pic = (ImageView) itemView.findViewById(R.id.comment_user_pic);
            username = (TextView) itemView.findViewById(R.id.comment_user);
            comment = (TextView) itemView.findViewById(R.id.comment);
            time = (TextView) itemView.findViewById(R.id.comment_time_stamp);
        }
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_comment, parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentViewHolder holder, final int position) {
        holder.comment.setText(comments.get(position).getText());
        holder.username.setText(commenters.get(position).getUsername());

        Timestamp time = new Timestamp(comments.get(position).getCreatedDate());
        Date date = new Date(time.getTime());
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        String dateString = df.format(date);
        holder.time.setText(dateString);

        // Load in the image in a separate thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                setImageView(holder, commenters.get(position).getPhotoUrl());
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    // Function to load in an image from an image URL.
    private void setImageView(final CommentRVAdapter.CommentViewHolder holder, String url) {
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
                holder.user_pic.setImageBitmap(finalBitmap);
            }
        });
    }
}
