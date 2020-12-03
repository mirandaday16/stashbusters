//package edu.neu.madcourse.stashbusters;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//public class UserPostRecyclerAdapter extends RecyclerView.Adapter<UserPostRecyclerAdapter.ViewHolder>{
//
//    private static final String TAG = "UserPostRecyclerAdapter";
//
//    private ArrayList<String> mImageNames = new ArrayList<>();
//    private ArrayList<String> mImages = new ArrayList<>();
//    private ArrayList<String> mUserNames = new ArrayList<>();
//    private ArrayList<String> mHeadlines  = new ArrayList<>();
//    private ArrayList<String> mNumlikes = new ArrayList<>();
//    private Context mContext;
//
//
//    public UserPostRecyclerAdapter(ArrayList<String> mImages, ArrayList<String> mUserNames, ArrayList<String> mHeadlines, ArrayList<String> mNumlikes, Context mContext) {
//        this.mImages = mImages;
//        this.mUserNames = mUserNames;
//        this.mHeadlines = mHeadlines;
//        this.mNumlikes = mNumlikes;
//        this.mContext = mContext;
//    }
//
//    @NonNull
//    @Override
//    public UserPostRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_feed_list_item,parent,false);
//        UserPostRecyclerAdapter.ViewHolder holder = new UserPostRecyclerAdapter.ViewHolder(view);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull FeedRecyclerAdapter.ViewHolder holder, int position) {
//
//        //TODO come back here when u need onclick functionality for later on during implementation
//
//        Log.d(TAG, "onBindViewHolder called");
//
//        holder.headline.setText(mHeadlines.get(position));
//        holder.numLikes.setText(mNumlikes.get(position));
//        //TODO keep initializing these set texts - need to figure out what to do for Images
//        holder.username.setText(mUserNames.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return mImages.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//
//        ImageView image;
//        ImageView heart;
//        TextView username;
//        TextView numLikes;
//        TextView headline;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            image = itemView.findViewById(R.id.image);
//            heart = itemView.findViewById(R.id.heart);
//            username = itemView.findViewById(R.id.username);
//            numLikes = itemView.findViewById(R.id.numLikes);
//            headline = itemView.findViewById(R.id.headline);
//
//        }
//    }
//}