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
        ImageView userPic = binding.profilePic;
        TextView username = binding.username;
        ImageView likedIcon = binding.liked;
        TextView title = binding.title;
        ImageView postPhoto = binding.photo;
        TextView details = binding.details;
        EditText commentInput = binding.commentInput;
        TextView timeStamp = binding.timeStamp;
        LinearLayout swapSection = binding.swapFor;
    }

    private void initListeners() {
        // TODO: Implement all onClicks
    }

    @Override
    public void setAuthorViewData(String username, String profilePicUrl) {

    }

    @Override
    public void setPostViewData(String title, String postPicUrl, String description, long createdDate) {

    }
}
