package edu.neu.madcourse.stashbusters.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.model.Post;
import edu.neu.madcourse.stashbusters.model.StashSwapPost;
import edu.neu.madcourse.stashbusters.views.PanelPostActivity;
import edu.neu.madcourse.stashbusters.views.PostActivity;
import edu.neu.madcourse.stashbusters.views.SwapPostActivity;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ImageViewHolder> {
    private static final String TAG = PostAdapter.class.getSimpleName();
    private Context mContext;
    private List<Post> mPosts;

    public PostAdapter(Context context, List<Post> posts) {
        this.mContext = context;
        this.mPosts = posts;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_feed_list_item, parent, false);
        return new PostAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostAdapter.ImageViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder called");
        final Post post = mPosts.get(position);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String usernameStr = snapshot.child("username").getValue().toString();
                    // TODO: get like count
                    holder.headline.setText(post.getTitle());
                    holder.username.setText(usernameStr);
                    Picasso.get().load(post.getPhotoUrl()).into(holder.image);

                    holder.setPostType(post.getPostType());
                    holder.setPostId(post.getId());
                    holder.setAuthorId(post.getAuthorId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView username, numLikes, headline;
        public ImageView image, heart;
        private String postType;
        private String postId;
        private String authorId;

        public ImageViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            heart = itemView.findViewById(R.id.heart);
            username = itemView.findViewById(R.id.username);
            numLikes = itemView.findViewById(R.id.numLikes);
            headline = itemView.findViewById(R.id.headline);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // get post type and open the correct activity
            Intent intent;
            if (this.postType.equals("StashSwapPost")) {
                intent = new Intent(mContext, SwapPostActivity.class);

            } else {
                intent = new Intent(mContext, PanelPostActivity.class);
            }
            intent.putExtra("postId", this.postId);
            intent.putExtra("userId", this.authorId);
            mContext.startActivity(intent);

        }

        public void setPostType(String postType) {
            this.postType = postType;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public void setAuthorId(String authorId) {
            this.authorId = authorId;
        }
    }
}