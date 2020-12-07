package edu.neu.madcourse.stashbusters;

import android.content.Context;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import edu.neu.madcourse.stashbusters.views.PersonalProfileActivity;
import edu.neu.madcourse.stashbusters.views.PublicProfileActivity;

public class CommentRVAdapter extends RecyclerView.Adapter<CommentRVAdapter.CommentViewHolder> {
    private static final String TAG = CommentRVAdapter.class.getSimpleName();
    private Context context;
    private List<Comment> comments;
    private DatabaseReference postRef;
    private FirebaseAuth mAuth;

    public CommentRVAdapter(Context context, List<Comment> comments, DatabaseReference postRef) {
        this.comments = comments;
        this.context = context;
        this.postRef = postRef;
        this.mAuth = FirebaseAuth.getInstance();

    }

    public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout user_info_area;
        ImageView user_pic;
        TextView username;
        TextView comment;
        TextView time;
        String authorId;

        CommentViewHolder (View itemView) {
            super(itemView);

            user_info_area = (LinearLayout) itemView.findViewById(R.id.user_info_area);
            user_pic = (ImageView) itemView.findViewById(R.id.comment_user_pic);
            username = (TextView) itemView.findViewById(R.id.comment_user);
            comment = (TextView) itemView.findViewById(R.id.comment);
            time = (TextView) itemView.findViewById(R.id.comment_time_stamp);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            String currentUserId = currentUser.getUid();

            Intent intent;

            if (authorId.equals(currentUserId)) {
                // Comment author is the same as current user; go to Personal Profile
                intent = new Intent(context, PersonalProfileActivity.class);
            } else {
                // different user; send to their public profile
                intent = new Intent(context, PublicProfileActivity.class);
                intent.putExtra("userId", authorId);
            }
                context.startActivity(intent);
        }

        public void setAuthorId(String authorId) {
            this.authorId = authorId;
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
        final String authorId = comment.getAuthorId();
        DatabaseReference commentsListRef = postRef.child("comments");

        commentsListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseReference authorUsernameRef = FirebaseDatabase.getInstance().getReference().child("users").child(authorId);
                authorUsernameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String username = snapshot.child("username").getValue().toString();
                        final String profilePicUrl = snapshot.child("photoUrl").getValue().toString();
                        holder.username.setText(username);
                        holder.setAuthorId(authorId);
                        Picasso.get().load(profilePicUrl).into(holder.user_pic);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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


    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

}
