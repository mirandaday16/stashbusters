package edu.neu.madcourse.stashbusters.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.contracts.LoginContract;
import edu.neu.madcourse.stashbusters.databinding.LoginLandingScreenBinding;
import edu.neu.madcourse.stashbusters.presenters.LoginPresenter;

public class LoginActivity extends AppCompatActivity implements LoginContract.MvpView {
    private static final String TAG = NewAccountActivity.class.getSimpleName();

    // Set up ViewBinding for the layout
    private LoginLandingScreenBinding binding;
    private Button logInButton, signUpButton;

    private FirebaseAuth mAuth;
    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing auth object for Firebase login
        mAuth = FirebaseAuth.getInstance();
        mPresenter = new LoginPresenter(this);

        // Setting up binding instance and view instances
        binding = LoginLandingScreenBinding.inflate(getLayoutInflater());
        logInButton = binding.logInButton;
        signUpButton = binding.signUpButton;

        initListeners();
        setContentView(binding.getRoot());
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in; if so, go to World Feed activity
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            mPresenter.startWorldFeedActivity();
        }
    }

    @Override
    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    @Override
    public void showToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    /*
        // Helper functions:
        */
    private void initListeners(){
        // Setting onClickListener for Log In Button - opens dialog box to enter username and password
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogBox(LoginActivity.this, R.style.AlertDialogCustom);
            }
        });

        // Setting onClickListener for Sign Up Button - opens New Account Activity
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.createNewAccount();
            }
        });
    }

    // Opens a dialog box for user to input username and password
    private void showDialogBox(Context context, int themeResId) {
        // Creating a Linear Layout containing EditText fields for entering username and password
        LayoutInflater factory = LayoutInflater.from(this);
        final View credentialsView = factory.inflate(R.layout.login_dialog, null);
        final EditText usernameField = (EditText) credentialsView.findViewById(R.id.usernameEntry);
        final EditText passwordField = (EditText) credentialsView.findViewById(R.id.passwordEntry);

        // Creating an Alert Dialog for the user to enter their username and password
        final AlertDialog credentialsBox = new AlertDialog.Builder(context, themeResId)
                .setView(credentialsView)
                .setPositiveButton("Log In", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Pass username and password entered by the user into Firebase for validation
                        String username = usernameField.getText().toString().trim();
                        String password = passwordField.getText().toString().trim();
                        mPresenter.validateNewUser(username, password);
//                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        credentialsBox.show();
    }

}