package edu.neu.madcourse.stashbusters;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.stashbusters.model.SnackBustPost;
import edu.neu.madcourse.stashbusters.model.SnackBustChoice;

public class SnackBustingActivity extends AppCompatActivity {
    private int currentPost;
    private List<SnackBustPost> posts;
    private ImageView snackImage;
    private TextView questionText;
    private TextView choiceOne;
    private TextView choiceTwo;
    private Handler imageHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_snack_busting);

        currentPost=0;
        snackImage = (ImageView) findViewById(R.id.snack_image);
        questionText = (TextView) findViewById(R.id.question_text);
        choiceOne = (TextView) findViewById(R.id.choice_one);
        choiceTwo = (TextView) findViewById(R.id.choice_two);

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


        // Fill layout with snack bust post data
        SnackBustPost currPost = posts.get(currentPost);
        questionText.setText(currPost.getTitle());
        choiceOne.setText(currPost.getChoiceList().get(0).getText());
        choiceTwo.setText(currPost.getChoiceList().get(1).getText());

        new Thread(new Runnable() {
            @Override
            public void run() {
                setImageView(posts.get(currentPost).getPhotoUrl());
            }
        }).start();

    }

    private void setImageView(String url) {
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
                snackImage.setImageBitmap(finalBitmap);
            }
        });
    }
}