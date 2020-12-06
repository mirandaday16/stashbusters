package edu.neu.madcourse.stashbusters;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.neu.madcourse.stashbusters.databinding.ContentActivityFeedBinding;
import edu.neu.madcourse.stashbusters.enums.NavigationBarButtons;
import edu.neu.madcourse.stashbusters.views.NavigationBarView;

public class YourFeedActivity extends AppCompatActivity {

    private static final String TAG = "Feed activfity ";
    private ContentActivityFeedBinding binding;
    private NavigationBarView navigationBarView;

    //vars
    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mUserNames = new ArrayList<>();
    private ArrayList<String> mHeadlines  = new ArrayList<>();
    private ArrayList<String> mNumlikes = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity_feed);

        Log.d(TAG, "On createL started ");
        initUserNames();
        initHeadlines();
        initNumlikes();
        initImages();
        initRecylcerView();

        binding = ContentActivityFeedBinding.inflate(getLayoutInflater());
        // Navigation bar setup:
        navigationBarView = binding.navigationBar;
        navigationBarView.setSelected(NavigationBarButtons.MYPROFILE);
    }

    private void initUserNames() {
        Log.d(TAG, "Usernames created ");
        mUserNames.add("Fergus");
        mUserNames.add("Miranda ");
        mUserNames.add("Clara ");
        mUserNames.add("Zee ");

    }
    private void initHeadlines() {
        Log.d(TAG, "Headlines created " );
        mHeadlines.add("Testing post 1");
        mHeadlines.add("Testing post 2");
        mHeadlines.add("Testing post 3");
        mHeadlines.add("Testing post 4");

    }
    private void initNumlikes() {
        Log.d(TAG, "Numlikes created");
        mNumlikes.add("43");
        mNumlikes.add("28");
        mNumlikes.add("25773");
        mNumlikes.add("274");
    }

    private void initImages() {
        mImages.add("Pic of string");
        mImages.add("Pic of painting");
        mImages.add("Pic of pen");
        mImages.add("Pic of crayons");
    }

    private void initRecylcerView() {
        Log.d(TAG, "Recycler created " );
        RecyclerView recyclerView = findViewById((R.id.recycler_view_feed));
        FeedRecyclerAdapter adapter = new FeedRecyclerAdapter(mImages,mUserNames,mHeadlines,mNumlikes,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

}
