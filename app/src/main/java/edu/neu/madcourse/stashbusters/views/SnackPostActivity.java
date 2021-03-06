package edu.neu.madcourse.stashbusters.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.contracts.SnackPostContract;
import edu.neu.madcourse.stashbusters.model.SnackBustPost;
import edu.neu.madcourse.stashbusters.model.User;
import edu.neu.madcourse.stashbusters.presenters.SnackPostPresenter;
import edu.neu.madcourse.stashbusters.utils.Utils;

public class SnackPostActivity extends AppCompatActivity implements SnackPostContract.MvpView {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SnackRVAdapter adapter;
    private List<SnackBustPost> posts;
    int currPost = 0;
    ImageView snackImage;
    TextView postText, swipeText;
    SnackPostPresenter mPresenter;

    protected String authorId, postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack_busting);

        snackImage = (ImageView) findViewById(R.id.snack_image);
        postText = (TextView) findViewById(R.id.post_text);
        swipeText = (TextView) findViewById(R.id.swipe);

        mPresenter = new SnackPostPresenter(this);

        // Try to get userId and postId from Intent.
        // This is used if the user is coming from the NewSnackActivity.
        // Otherwise, authorId and postId will be null.
        Intent intent = getIntent();
        authorId = intent.getStringExtra("userId");
        postId = intent.getStringExtra("postId");

        if (authorId == null && postId == null) {
            // Show all SnackBusting posts
            mPresenter.loadSnackPosts();

        } else {
            // Get just the one SnackBusting post
            mPresenter.loadSingleSnackPost(authorId, postId);
        }
    }

    @Override
    public void setPostView(List<SnackBustPost> postList) {

        this.posts = postList;

        // Set up recycler view
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new UnscrollableLinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mPresenter.loadAuthorData(posts.get(currPost).getAuthorId());

        setUpSwipeHandler();
    }

    private void setUpSwipeHandler(){
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
                        // Add a vote to the corresponding choice when swiped left or right
                        if (swipeDir == ItemTouchHelper.LEFT) {
                            increaseVote(0);
                        }
                        else if (swipeDir == ItemTouchHelper.RIGHT) {
                            increaseVote(1);
                        }

                        // Send notification to author about new vote.
                        // We don't need this user's username when sending the notification,
                        // so I am just sending the authorID in the place of the senderId argument
                        // because it won't matter in the end.
                        Utils.startNotification("snack", posts.get(currPost).getAuthorId(), posts.get(currPost).getAuthorId(),
                                posts.get(currPost).getId(),
                                posts.get(currPost).getPhotoUrl());

                        // notify the recyclerview changes
                        currPost++;
                        if (currPost < posts.size()) {
                            mPresenter.loadAuthorData(posts.get(currPost).getAuthorId());
                        } else {
                            setNoPosts();
                        }
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void increaseVote(int i){
        SnackBustPost updatePost = posts.get(currPost);
        mPresenter.updateSnackPost(updatePost.getAuthorId(), updatePost.getId(), i,
                posts.get(currPost).getChoiceList().get(i).getVoteCount());
    }

    @Override
    public void setNewCard(User author) {
        adapter = new SnackRVAdapter(posts.get(currPost), author, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setNoPosts(){
        snackImage.setImageResource(R.drawable.cookie_icon);
        postText.setText(R.string.no_more_snacks);
        swipeText.setVisibility(View.GONE);
    }

    // An unscrollable linear layout manager object
    public static class UnscrollableLinearLayoutManager extends LinearLayoutManager {
        public UnscrollableLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }

    /**
     * If this activity was opened from a notification,
     * set back stack so back button goes to World Feed.
     */
    @Override
    public void onBackPressed() {
        Intent thisIntent = getIntent();
        if (thisIntent.getExtras()!= null) {
            if (thisIntent.getExtras().containsKey("LAUNCHED_BY_NOTIFICATION")){
                Intent intent = new Intent(this, WorldFeedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}