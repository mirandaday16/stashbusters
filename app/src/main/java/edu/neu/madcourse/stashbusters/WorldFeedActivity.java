package edu.neu.madcourse.stashbusters;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.stashbusters.adapters.PostAdapter;
import edu.neu.madcourse.stashbusters.contracts.WorldFeedContract;
import edu.neu.madcourse.stashbusters.databinding.ContentActivityFeedBinding;
import edu.neu.madcourse.stashbusters.enums.NavigationBarButtons;
import edu.neu.madcourse.stashbusters.model.Post;
import edu.neu.madcourse.stashbusters.presenters.WorldFeedPresenter;
import edu.neu.madcourse.stashbusters.views.NavigationBarView;

public class WorldFeedActivity extends AppCompatActivity implements WorldFeedContract.MvpView {
    private NavigationBarView navigationBarView;
    ContentActivityFeedBinding binding;
    private static final String TAG = "Feed activfity ";

    private List<Post> posts;
    WorldFeedPresenter mPresenter;
    private RecyclerView postListRecyclerView;

    private PostAdapter postAdapter;


    //vars
//    private ArrayList<String> mImageNames = new ArrayList<>();
//    private ArrayList<String> mImages = new ArrayList<>();
//    private ArrayList<String> mUserNames = new ArrayList<>();
//    private ArrayList<String> mHeadlines  = new ArrayList<>();
//    private ArrayList<String> mNumlikes = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity_feed);

        mPresenter = new WorldFeedPresenter(this);

        Log.d(TAG, "On createL started ");
        // Navigation bar setup:
        binding = ContentActivityFeedBinding.inflate(getLayoutInflater());
        navigationBarView = binding.navigationBar;
        navigationBarView.setSelected(NavigationBarButtons.WORLDFEED);

        //Get posts
        mPresenter.loadPosts();
    }


    @Override
    public void setPosts(List<Post> posts) {
        if (posts != null && posts.size() > 0) {

            RecyclerView recyclerView = findViewById(R.id.recycler_view_feed);
            PostAdapter adapter = new PostAdapter(this,posts);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        else {
            //No posts to show
            System.out.println("  there are no posts right now");
        }
    }

//    private void initUserNames() {
//        Log.d(TAG, "Usernames created ");
//        mUserNames.add("Fergus");
//        mUserNames.add("Miranda ");
//        mUserNames.add("Clara ");
//        mUserNames.add("Zee ");
//
//    }
//    private void initHeadlines() {
//        Log.d(TAG, "Headlines created " );
//        mHeadlines.add("Testing post 1");
//        mHeadlines.add("Testing post 2");
//        mHeadlines.add("Testing post 3");
//        mHeadlines.add("Testing post 4");
//
//    }
//    private void initNumlikes() {
//        Log.d(TAG, "Numlikes created");
//        mNumlikes.add("43");
//        mNumlikes.add("28");
//        mNumlikes.add("25773");
//        mNumlikes.add("274");
//    }
//
//    private void initImages() {
//        mImages.add("Pic of string");
//        mImages.add("Pic of painting");
//        mImages.add("Pic of pen");
//        mImages.add("Pic of crayons");
//    }

//    private void initRecylcerView() {
//        Log.d(TAG, "Recycler created " );
//        RecyclerView recyclerView = findViewById((R.id.recycler_view_feed));
//        FeedRecyclerAdapter adapter = new PostAdapter(this,posts);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//
//    }

}
