package edu.neu.madcourse.stashbusters.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.neu.madcourse.stashbusters.CommentRVAdapter;
import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.contracts.PostContract;
import edu.neu.madcourse.stashbusters.databinding.ActivityPanelSwapPostBinding;
import edu.neu.madcourse.stashbusters.model.Comment;
import edu.neu.madcourse.stashbusters.presenters.PostPresenter;

public abstract class PostActivity extends AppCompatActivity implements PostContract.MvpView {
    protected PostPresenter mPresenter;
    protected FirebaseAuth mAuth;
    protected String authorId, postId;
    protected DatabaseReference authorUserRef;
    protected DatabaseReference postRef;

    // Set up ViewBinding for the layout
    protected ActivityPanelSwapPostBinding binding;

    // Views
    protected ConstraintLayout userView;
    protected ImageView userPic;
    protected TextView usernameView;
    protected ImageView likedIcon;
    protected TextView titleView;
    protected ImageView postPhoto;
    protected TextView details;
    protected EditText commentInput;
    protected TextView timeStamp;
    protected LinearLayout swapSection;
    protected Button submitButton;

    // Attributes needed for displaying comments in recycler view.
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CommentRVAdapter adapter;

    // For updating ImageView in a separate thread.
    private Handler imageHandler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        authorId = intent.getStringExtra("userId");
        postId = intent.getStringExtra("postId");

        mAuth = FirebaseAuth.getInstance();

        setRefs();

        setPresenter();


        // Setting up binding instance and view instances
        binding = ActivityPanelSwapPostBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();

        initViews();
        onUsernameClick(this);
        initListeners();

        setContentView(rootView);
    }

    public abstract void setRefs();

    public abstract void setPresenter();

    public abstract void initViews();

    public abstract void onUsernameClick(Context context);

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
                    mPresenter.uploadComment(comment);
                    // Reset comment field and update RecyclerView so user can see their comment
                    commentInput.setText("");
                    // TODO: Hide soft keyboard and update RecyclerView
                }
            }
        });
    }


    @Override
    public void setAuthorViewData(String username, String profilePicUrl) {
        usernameView.setText(username);
        Picasso.get().load(profilePicUrl).into(userPic);
    }

    @Override
    public void setPostViewData(String title, String postPicUrl, String description,
                                long createdDate) {
        titleView.setText(title);
        Picasso.get().load(postPicUrl).into(postPhoto);
        details.setText(description);

        // Format time stamp
        Date date = new Date(createdDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
        String dateText = dateFormat.format(date);
        timeStamp.setText(dateText);
    }
}
