package edu.neu.madcourse.stashbusters.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import edu.neu.madcourse.stashbusters.R;
import edu.neu.madcourse.stashbusters.enums.MaterialType;


public class NewPanelActivity extends AppCompatActivity {
    private RadioGroup materialGroup;
    private int clickedMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_panel);

        materialGroup = findViewById(R.id.materialGroup);
        setUpMaterialGroup();

    }

    // Add a radio button for each material in the MaterialType enum and set up listener.
    private void setUpMaterialGroup(){
        int i = 0;
        for (MaterialType mat : MaterialType.values()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(mat.getDbCode());
            radioButton.setId(i);
            materialGroup.addView(radioButton);
            i++;
        }

        // Set a listener for the radio button group.
        materialGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                NewPanelActivity.this.clickedMaterial = materialGroup.getCheckedRadioButtonId();
            }
        });
    }

    // How to implement user inserting an image:
    // https://stackoverflow.com/questions/14433096/allow-the-user-to-insert-an-image-in-android-app
}