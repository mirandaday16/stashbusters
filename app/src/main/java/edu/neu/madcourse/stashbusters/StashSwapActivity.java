package edu.neu.madcourse.stashbusters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.neu.madcourse.stashbusters.enums.MaterialType;
import edu.neu.madcourse.stashbusters.model.Comment;
import edu.neu.madcourse.stashbusters.model.StashSwapPost;
import edu.neu.madcourse.stashbusters.model.User;

public class StashSwapActivity extends AppCompatActivity {

    // Attributes needed for displaying comments in recycler view.
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CommentRVAdapter adapter;

    // For updating ImageView in a separate thread.
    private Handler imageHandler = new Handler();

    // For grabbing items in the layout.
    ImageView userPic;
    TextView username;
    ImageView liked;
    TextView title;
    ImageView photo;
    TextView details;
    EditText commentInput;
    TextView timeStamp;
    TextView swapItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_swap_post);

        // Grab items in the layout.
        userPic = (ImageView) findViewById(R.id.profile_pic);
        username = (TextView) findViewById(R.id.username);
        liked = (ImageView) findViewById(R.id.liked);
        title = (TextView) findViewById(R.id.title);
        photo = (ImageView) findViewById(R.id.photo);
        details = (TextView) findViewById(R.id.details);
        commentInput = (EditText) findViewById(R.id.comment_input);
        timeStamp = (TextView) findViewById(R.id.time_stamp);
        swapItem = (TextView) findViewById(R.id.swap_item);

        commentInput.setHint(R.string.offer_hint);

        ///////////////////////////   DUMMY DATA!  //////////////////////////////////////
        // Post user dummy data
        final User user = new User("Batman", "12345");
        user.setPhotoUrl("https://images-na.ssl-images-amazon.com/images/I/51zLZbEVSTL._AC_SX466_.jpg");

        // Post dummy data
        final StashSwapPost post = new StashSwapPost("Funky wooden beads",
                "Bought them because they were pretty to look at, but never ended up " +
                        "making anything with them. About 100 beads.",
                "https://montclairdispatch.com/wp-content/uploads/2015/04/Wooden_beads.jpg",
                "YARN");

        // Post liked dummy data
        boolean like = false;

        // Comment dummy data
        List<Comment> comments = new ArrayList<Comment>();
        Comment comment = new Comment("Would you be willing to swap for jute twine?");
        comments.add(comment);
        comment = new Comment("I've got two skeins of light blue yarn-- Red Heart brand.");
        comments.add(comment);

        // Comment users dummy data
        List<User> commenters = new ArrayList<User>();
        User commenter = new User("Robin", "012345");
        commenter.setPhotoUrl("https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg");
        commenters.add(commenter);
        commenter = new User("Ivy", "12345");
        commenter.setPhotoUrl("https://data.whicdn.com/images/322027365/original.jpg");
        commenters.add(commenter);
        ////////////////////////////////////////////////////////////////////////////




        // Set up recycler view for displaying comments.
        recyclerView = findViewById(R.id.comment_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommentRVAdapter(comments, commenters);
        recyclerView.setAdapter(adapter);




        ///////////////////////////   DISPLAY DATA!! //////////////////////////////////////
        // Display data
        username.setText(user.getUsername());
        title.setText(post.getTitle());
        details.setText(post.getDescription());
        swapItem.setText(post.getMaterial().toString());

        Timestamp time = new Timestamp(post.getCreatedDate());
        Date date = new Date(time.getTime());
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        String dateString = df.format(date);
        timeStamp.setText(dateString);

        if (like) {
            liked.setImageResource(R.drawable.heart_icon_filled);
        } else {
            liked.setImageResource(R.drawable.heart_icon_empty);
        }

        // Load in the user's profile pic in a separate thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                setImageView(user.getPhotoUrl(), userPic);
            }
        }).start();

        // Load in the post's photo' in a separate thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                setImageView(post.getPhotoUrl(), photo);
            }
        }).start();

        ////////////////////////////////////////////////////////////////////////////
    }

    // Function to load in an image from an image URL.
    private void setImageView(String url, final ImageView img) {
        Bitmap bitmap = null;
        try {
            InputStream in = new
                    URL(url)
                    .openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Bitmap finalBitmap = bitmap;
        imageHandler.post(new Runnable() {
            @Override
            public void run() {
                img.setImageBitmap(finalBitmap);
            }
        });
    }
}