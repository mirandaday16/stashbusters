package edu.neu.madcourse.stashbusters.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import edu.neu.madcourse.stashbusters.adapters.CommentRVAdapter;
import edu.neu.madcourse.stashbusters.contracts.PostContract;
import edu.neu.madcourse.stashbusters.databinding.ActivityPanelSwapPostBinding;
import edu.neu.madcourse.stashbusters.model.Comment;
import edu.neu.madcourse.stashbusters.model.Post;
import edu.neu.madcourse.stashbusters.presenters.PostPresenter;
import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.utils.Utils;

/**
 * Abstract class that represents the post detail activity.
 * Extended by {@link SwapPostActivity} and {@link PanelPostActivity}
 */
public abstract class PostActivity extends AppCompatActivity implements PostContract.MvpView {
    private static final String TAG = PostActivity.class.getSimpleName();
    public static final String LIKED_POSTS = "liked posts";
    public static final String MY_POSTS = "my posts";

    protected PostPresenter mPresenter;
    protected FirebaseAuth mAuth;
    protected String authorId, postId, currentUserId;
    protected DatabaseReference authorUserRef;
    protected DatabaseReference postRef;

    // Set up ViewBinding for the layout
    protected ActivityPanelSwapPostBinding binding;

    // Views
    protected ConstraintLayout userView;
    protected ImageView userPic;
    protected TextView usernameView;
    protected TextView titleView;
    protected ImageView postPhoto;
    protected TextView details;
    protected EditText commentInput;
    protected TextView timeStamp;
    protected TextView likeCountView;
    protected ImageView heartIcon;
    protected LinearLayout swapSection;
    protected Button swapButton;
    protected Button submitButton;
    protected ImageView more;
    protected RecyclerView commentsSection;

    protected boolean currentUserLikedPost = false;

    // Attributes needed for displaying comments in recycler view.
    LinearLayoutManager layoutManager;

    // For updating ImageView in a separate thread.
    private Handler imageHandler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        authorId = intent.getStringExtra("userId");
        postId = intent.getStringExtra("postId");

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        setRefs();
        setPresenter();

        // Setting up binding instance and view instances
        binding = ActivityPanelSwapPostBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();

        initViews();
        initRecyclerView();
        setMoreButton();
        initListeners();
        onSwapButtonClick();
        setContentView(rootView);
    }

    private void setMoreButton() {
        if (!authorId.equals(currentUserId)) {
            more.setVisibility(View.GONE);
        }
    }

    public abstract void setRefs();

    public abstract void setPresenter();

    public abstract void initViews();

    @Override
    public boolean getCurrentUserLikedPostStatus() {
        return this.currentUserLikedPost;
    }

    public abstract void onSwapButtonClick();

    public void initRecyclerView() {
        commentsSection = binding.commentRecyclerView;
        commentsSection.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        commentsSection.setLayoutManager(layoutManager);
    }

    @Override
    public void setCurrentUserLikedPostStatus(boolean likeStatus) {
        this.currentUserLikedPost = likeStatus;
    }

    @Override
    public void setAuthorViewData(String username, String profilePicUrl) {
        usernameView.setText(username);
        Picasso.get().load(profilePicUrl).into(userPic);
    }

    public void setNewLikeCount(long newLikeCount) {
        String likeCountText = String.format(getResources().getString(R.string.like_count), newLikeCount);
        likeCountView.setText(likeCountText);
    }

    public void updateHeartIconDisplay(boolean status) {
        if (status) {
            heartIcon.setImageResource(R.drawable.heart_icon_filled);
        } else {
            heartIcon.setImageResource(R.drawable.heart_icon_empty);
        }
    }

    @Override
    public void setCommentAdapter(CommentRVAdapter commentsAdapter) {
        commentsSection.setAdapter(commentsAdapter);
    }

    public void initListeners() {

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new Comment object
                String commentText = commentInput.getText().toString();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                Comment comment = new Comment(commentText);
                comment.setAuthorId(currentUser.getUid());
                // Check that the user has entered a comment in the EditText field
                if (commentText != null) {
                    mPresenter.uploadComment(postRef, comment);
                    mPresenter.startCommentNotification("comment", authorId, postId);
                    // Reset comment field and update RecyclerView so user can see their comment
                    commentInput.setText("");
                }
            }
        });

        userView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check to see if the author user is the same as the current user
                FirebaseUser currentUser = mAuth.getCurrentUser();
                String currentUserId = currentUser.getUid();
                if (authorId.equals(currentUserId)) {
                    // If current user, take user to their personal profile
                    Intent intent = new Intent(getApplicationContext(), PersonalProfileActivity.class);
                    startActivity(intent);
                } else {
                    // Send user to author user's public profile
                    Intent intent = new Intent(getApplicationContext(), PublicProfileActivity.class);
                    intent.putExtra("userId", authorId);
                    startActivity(intent);
                }
            }
        });

        heartIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mPresenter.onHeartIconClick(postRef, "like", authorId, postId);
            }
        });

        more.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(PostActivity.this, v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch ((item.getItemId())) {
                            case R.id.edit_post:
                                displayEditAlert();
                                return true;
                            case R.id.delete_post:
                                displayDeleteAlert();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popupMenu.inflate(R.menu.post_menu);
                if (!authorId.equals(currentUserId)) {
                    popupMenu.getMenu().findItem(R.id.edit_post).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.delete_post).setVisible(false);
                }
                popupMenu.show();
            }
        });


    }

    private void displayDeleteAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
        builder.setTitle("Do you want to delete this post?");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.deletePost();
                Utils.showToast(PostActivity.this, "Post has been deleted");
            }
        });

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(getResources().getColor(R.color.colorTextLight));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(getResources().getColor(R.color.colorAccentDark));
            }
        });

        dialog.show();
    }

    private void displayEditAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
        builder.setTitle("Edit description");

        final EditText inputDesc = new EditText(PostActivity.this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        inputDesc.setLayoutParams(lp);

        builder.setView(inputDesc);

        // filled the box with current description
        getText(inputDesc);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.editPost(inputDesc);
            }
        });

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(getResources().getColor(R.color.colorTextLight));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(getResources().getColor(R.color.colorAccentDark));
            }
        });

        dialog.show();
    }

    private void getText(final EditText editDescText) {
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // check post type
                String desc = snapshot.child("description").getValue().toString();

                editDescText.setText(desc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }
        });
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