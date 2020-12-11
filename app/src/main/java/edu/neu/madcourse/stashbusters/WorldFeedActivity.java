package edu.neu.madcourse.stashbusters;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String TAG = WorldFeedActivity.class.getSimpleName();

    private NavigationBarView navigationBarView;
    ContentActivityFeedBinding binding;
    WorldFeedPresenter mPresenter;
    private Spinner spinner;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity_feed);


        //Code to initialize the spinner (dropdown button) and create adapter
        spinner = findViewById(R.id.filter_button);
        spinner.setPrompt("Filter");
        String[] items = new String[]{"No Filter", "Panel Posts", "Swap Posts"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedItem = adapterView.getItemAtPosition(position).toString();
                if(selectedItem.equals("Panel Posts")){
                    mPresenter.loadPanelPosts();

                }
                else if(selectedItem.equals("Swap Posts")){

                    mPresenter.loadSwapPosts();
                }
                else if(selectedItem.equals("No Filter")){

                    mPresenter.loadPosts();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mPresenter = new WorldFeedPresenter(this);

        // Navigation bar setup:
        binding = ContentActivityFeedBinding.inflate(getLayoutInflater());
        navigationBarView = binding.navigationBar;
        navigationBarView.setSelected(NavigationBarButtons.WORLDFEED);

        ImageView MyFeedImg = (ImageView) findViewById(R.id.my_feed_img);
        MyFeedImg.setImageResource(R.drawable.world_icon);
        TextView MyFeedText = (TextView) findViewById(R.id.my_feed_title);
        MyFeedText.setText(R.string.world_feed);

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
            Log.i(TAG, "No post to show on World Feed.");
        }
    }

}
