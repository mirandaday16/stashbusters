package edu.neu.madcourse.stashbusters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import edu.neu.madcourse.stashbusters.model.Post;

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
    public void onBindViewHolder(@NonNull PostAdapter.ImageViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder called");
        final Post post = mPosts.get(position);

        holder.headline.setText(post.getTitle());
        Picasso.get().load(post.getPhotoUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView username, numLikes, headline;
        public ImageView image, heart;

        public ImageViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            heart = itemView.findViewById(R.id.heart);
            username = itemView.findViewById(R.id.username);
            numLikes = itemView.findViewById(R.id.numLikes);
            headline = itemView.findViewById(R.id.headline);
        }
    }
}
