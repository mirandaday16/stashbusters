package edu.neu.madcourse.stashbusters.views;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.neu.madcourse.stashbusters.contracts.PostContract;
import edu.neu.madcourse.stashbusters.databinding.ActivityPanelSwapPostBinding;
import edu.neu.madcourse.stashbusters.presenters.PostPresenter;

public class PanelPostActivity extends AppCompatActivity implements PostContract.MvpView {
    private static final String TAG = PanelPostActivity.class.getSimpleName();

    private PostPresenter mPresenter;
    private FirebaseAuth mAuth;
    private String authorId, postId;
    private DatabaseReference authorUserRef;
    private DatabaseReference postRef;

    // Set up ViewBinding for the layout
    private ActivityPanelSwapPostBinding binding;

    // Views
    private ImageView userPic;
    private TextView usernameView;
    private ImageView likedIcon;
    private TextView titleView;
    private ImageView postPhoto;
    private TextView details;
    private EditText commentInput;
    private TextView timeStamp;
    private LinearLayout swapSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        authorId = ""; // TODO: Figure out how to get author ID and post ID info here!!
        postId = "";

        authorUserRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child("authorId");
        postRef = FirebaseDatabase.getInstance().getReference().child("panelPosts")
                .child("authorId").child("postId");

        mPresenter = new PostPresenter(this, authorId, postId);
        mPresenter.loadAuthorDataToView();
        mPresenter.loadPostDataToView();


        // Setting up binding instance and view instances
        binding = ActivityPanelSwapPostBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();

        initViews();
        initListeners();

        setContentView(rootView);


    }

    private void initViews() {
        userPic = binding.profilePic;
        usernameView = binding.username;
        likedIcon = binding.liked;
        titleView = binding.title;
        postPhoto = binding.photo;
        details = binding.details;
        commentInput = binding.commentInput;
        timeStamp = binding.timeStamp;
        swapSection = binding.swapFor;
    }

    private void initListeners() {
        // TODO: Implement all onClicks
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        String dateText = dateFormat.format(date);
        timeStamp.setText(dateText);
    }
}
