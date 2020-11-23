package edu.neu.madcourse.stashbusters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.stashbusters.model.SnackBustPost;
import edu.neu.madcourse.stashbusters.model.SnackBustChoice;

public class SnackBustingActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SnackRVAdapter adapter;
    private List<SnackBustPost> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_snack_busting);

        // Set up dummy data
        SnackBustChoice choice_one = new SnackBustChoice("smiley mouth");
        SnackBustChoice choice_two = new SnackBustChoice("-_- mouth");
        List<SnackBustChoice> choices = new ArrayList<>();
        choices.add(choice_one);
        choices.add(choice_two);
        SnackBustPost post = new SnackBustPost("What kind of mouth?",
                "https://i.ibb.co/cQg5V0d/test-photo-plush.png", choices);
        posts = new ArrayList<>();
        posts.add(post);

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new UnscrollableLinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SnackRVAdapter(posts);
        recyclerView.setAdapter(adapter);
    }

    // An unscrollable linear layout manager object
    public class UnscrollableLinearLayoutManager extends LinearLayoutManager {
        public UnscrollableLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }
}