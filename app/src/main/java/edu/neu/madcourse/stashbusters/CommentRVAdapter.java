package edu.neu.madcourse.stashbusters;

import android.content.Context;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.neu.madcourse.stashbusters.model.Comment;

public class CommentRVAdapter extends RecyclerView.Adapter<CommentRVAdapter.CommentViewHolder> {
    private static final String TAG = CommentRVAdapter.class.getSimpleName();
    private List<Comment> comments;
    private DatabaseReference postRef;
    private List<DatabaseReference> usersRefs;

    public CommentRVAdapter(List<Comment> comments, DatabaseReference postRef) {
        this.comments = comments;
        this.postRef = postRef;
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        LinearLayout user_info_area;
        ImageView user_pic;
        TextView username;
        TextView comment;
        TextView time;

        CommentViewHolder (View itemView) {
            super(itemView);

            user_info_area = (LinearLayout) itemView.findViewById(R.id.user_info_area);
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
        Log.i(TAG, "onBindViewHolder called");
        final Comment comment = comments.get(position);
        String authorId = comment.getAuthorId();
        final DatabaseReference authorUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(authorId);
        final String authorUsername = authorUserRef.child("username").child(authorId).toString();
        DatabaseReference commentsListRef = postRef.child("comments");

        commentsListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = authorUsername;
                holder.username.setText(authorUsername);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Load in comment and user info from Firebase
        holder.comment.setText(comments.get(position).getText());

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
                String userPicUrl = usersRefs.get(position).child("photoUrl").toString();
                Picasso.get().load(userPicUrl).into(holder.user_pic);
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public List<DatabaseReference> getUsersList(List<Comment> commentList) {
        ArrayList<DatabaseReference> usersRefs = new ArrayList<>();
        DatabaseReference usersNodeRef = FirebaseDatabase.getInstance().getReference().child("users");
        for (Comment comment : commentList) {
            String userId = comment.getAuthorId();
            DatabaseReference firebaseUserRef = usersNodeRef.child(userId);
            usersRefs.add(firebaseUserRef);
        }

        return usersRefs;
    }
}
