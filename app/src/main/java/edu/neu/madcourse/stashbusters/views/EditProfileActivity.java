package edu.neu.madcourse.stashbusters.views;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.contracts.EditProfileContract;
import edu.neu.madcourse.stashbusters.databinding.EditAccountActivityBinding;
import edu.neu.madcourse.stashbusters.presenters.EditProfilePresenter;

public class EditProfileActivity extends AppCompatActivity implements EditProfileContract.MvpView {
    private static final String TAG = PersonalProfileActivity.class.getSimpleName();

    // Set up ViewBinding for the layout
    private EditAccountActivityBinding binding;
    private EditProfileContract.Presenter mPresenter;

    private EditText bioEditText;
    private ImageView imageButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account_activity);

        String userId = "meeky"; // TODO: change this with FirebaseAuth.getUid()
        mPresenter = new EditProfilePresenter(this, userId);
        binding = EditAccountActivityBinding.inflate(getLayoutInflater());

        bioEditText = binding.bioInput;
        imageButton = binding.imageButton;

        setContentView(binding.getRoot());
    }

    @Override
    public void setBio(String bio) {
        bioEditText.setText(bio);
    }

    @Override
    public void setProfilePhoto(String photoUrl) {
        Picasso.get().load(photoUrl).into(imageButton);
    }
}
