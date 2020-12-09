package edu.neu.madcourse.stashbusters.views;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.neu.madcourse.stashbusters.FeedRecyclerAdapter;
import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.contracts.MyFeedContract;
import edu.neu.madcourse.stashbusters.databinding.ContentActivityFeedBinding;
import edu.neu.madcourse.stashbusters.enums.NavigationBarButtons;
import edu.neu.madcourse.stashbusters.model.Post;
import edu.neu.madcourse.stashbusters.presenters.MyFeedPresenter;

public class MyFeedActivity extends AppCompatActivity implements MyFeedContract.MvpView {
    private NavigationBarView navigationBarView;
    ContentActivityFeedBinding binding;

    private List<Post> posts;
    MyFeedPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity_feed);

        mPresenter = new MyFeedPresenter(this);

        // Set up navigation bar
        binding = ContentActivityFeedBinding.inflate(getLayoutInflater());
        navigationBarView = binding.navigationBar;
        navigationBarView.setSelected(NavigationBarButtons.MYFEED);

        mPresenter.loadPosts();


        //initRecyclerView();

    }

//    private void initRecyclerView() {
//        RecyclerView recyclerView = findViewById((R.id.recycler_view_feed));
//        FeedRecyclerAdapter adapter = new FeedRecyclerAdapter(mImages,mUserNames,mHeadlines,mNumlikes,this);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//    }

}
