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
    WorldFeedPresenter mPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity_feed);

        mPresenter = new WorldFeedPresenter(this);

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

}
