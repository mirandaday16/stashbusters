package edu.neu.madcourse.stashbusters.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.DataRemovalRequest;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import edu.neu.madcourse.stashbusters.utils.Utils;
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
        String authorId = post.getAuthorId();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(authorId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue().toString();
                holder.username.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.headline.setText(post.getTitle());
        Picasso.get().load(post.getPhotoUrl()).into(holder.image);

        String postType = post.getPostType();
        String postId = post.getId();

        holder.setPostType(postType);
        holder.setPostId(postId);
        holder.setAuthorId(authorId);

        if ("StashSwapPost".equals(holder.postType)) {
            holder.postCardBg.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryLight));
            holder.postTypeImage.setImageResource(R.drawable.swap_icon);
        } else {
            holder.postCardBg.setBackgroundColor(mContext.getResources().getColor(R.color.colorSecondaryLight));
            holder.postTypeImage.setImageResource(R.drawable.lightbulb_icon);
        }

        String likeCountText = post.getLikeCount() + " Likes";
        holder.numLikes.setText(likeCountText);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    /**
     * Image View holder
     */
    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView username, numLikes, headline;
        public ImageView image, heart, postTypeImage;
        public LinearLayout postCardBg;
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
            postCardBg = itemView.findViewById(R.id.post_card_bg);
            postTypeImage = itemView.findViewById(R.id.post_type_image);

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
