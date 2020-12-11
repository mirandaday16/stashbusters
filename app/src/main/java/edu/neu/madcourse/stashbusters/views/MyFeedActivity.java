package edu.neu.madcourse.stashbusters.views;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.adapters.PostAdapter;
import edu.neu.madcourse.stashbusters.contracts.MyFeedContract;
import edu.neu.madcourse.stashbusters.databinding.ContentActivityFeedBinding;
import edu.neu.madcourse.stashbusters.enums.NavigationBarButtons;
import edu.neu.madcourse.stashbusters.model.Post;
import edu.neu.madcourse.stashbusters.presenters.MyFeedPresenter;

public class MyFeedActivity extends AppCompatActivity implements MyFeedContract.MvpView {
    private NavigationBarView navigationBarView;
    ContentActivityFeedBinding binding;

    MyFeedPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity_feed);

        binding = ContentActivityFeedBinding.inflate(getLayoutInflater());

        // Remove filter button from My Feed
        Spinner filterButton = binding.filterButton;
        filterButton.setVisibility(View.GONE);

        mPresenter = new MyFeedPresenter(this);

        // Set up navigation bar
        navigationBarView = binding.navigationBar;
        navigationBarView.setSelected(NavigationBarButtons.MYFEED);

        setContentView(binding.getRoot());

        // Get posts from Firebase
        mPresenter.loadPosts();

    }

    @Override
    public void setPosts(List<Post> posts) {
        if (posts != null && posts.size() > 0) {
            // recycler view for posts
            RecyclerView recyclerView = findViewById((R.id.recycler_view_feed));
            PostAdapter adapter = new PostAdapter(this, posts);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            // SHOW THAT THERE ARE NO POSTS
            ImageView noPostsImage = (ImageView) findViewById(R.id.noPostsImage);
            TextView noPostsText = (TextView) findViewById(R.id.noPostsText);
            noPostsImage.setImageResource(R.drawable.palette_icon);
            noPostsText.setText(R.string.no_posts_feed);
        }
    }

}
