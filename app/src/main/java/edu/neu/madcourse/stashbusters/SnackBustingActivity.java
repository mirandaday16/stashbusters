package edu.neu.madcourse.stashbusters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.stashbusters.model.SnackBustPost;
import edu.neu.madcourse.stashbusters.model.SnackBustChoice;

public class SnackBustingActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SnackRVAdapter adapter;
    private List<SnackBustPost> posts;
    int currPost;
    ImageView snackImage;
    TextView postText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_snack_busting);

        snackImage = (ImageView) findViewById(R.id.snack_image);
        postText = (TextView) findViewById(R.id.post_text);

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
        choice_one = new SnackBustChoice("yes, change it");
        choice_two = new SnackBustChoice("no, keep as is");
        choices = new ArrayList<>();
        choices.add(choice_one);
        choices.add(choice_two);
        post = new SnackBustPost("Leather handle?",
                "https://i.ibb.co/P6mt3d5/test-laptop-case.png", choices);
        posts.add(post);

        currPost = 0;


        // Set up recycler view
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new UnscrollableLinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SnackRVAdapter(posts.get(currPost));
        recyclerView.setAdapter(adapter);


        // Attach itemTouchHandler to handle swipes
        // Adapted from: https://stackoverflow.com/questions/54042319/how-do-i-add-swipe-to-delete-to-cards-on-cardview
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                // do something
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                // code to add a vote to the corresponding choice when swiped left or right
                // notify the recyclerview changes
                currPost++;
                if (currPost < posts.size()) {
                    adapter = new SnackRVAdapter(posts.get(1));
                    recyclerView.setAdapter(adapter);
                } else {
                    snackImage.setImageResource(R.drawable.cookie_icon);
                    postText.setText(R.string.no_more_snacks);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
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