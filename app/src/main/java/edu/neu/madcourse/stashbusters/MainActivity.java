package edu.neu.madcourse.stashbusters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.FirebaseApp;

import edu.neu.madcourse.stashbusters.views.LoginActivity;
import edu.neu.madcourse.stashbusters.views.PersonalProfileActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
    }


    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.new_post_button:
                intent = new Intent(this, NewPostActivity.class);
                startActivity(intent);
                break;
            case R.id.snack_busting_button:
                intent = new Intent(this, SnackBustingActivity.class);
                startActivity(intent);
                break;
            case R.id.user_profile_btn:
                intent = new Intent(this, PersonalProfileActivity.class);
                intent.putExtra("userId", "vGI4NaY1YBVl2CZ0WSJxehTCew33"); // TODO: change this later
                startActivity(intent);
                break;
            case R.id.register_btn:
                System.out.println("register btn clicked");
                intent = new Intent(this, NewAccountActivity.class);
                startActivity(intent);
                break;
            case R.id.login_btn:
                System.out.println("login btn clicked");
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}