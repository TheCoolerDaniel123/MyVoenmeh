package com.my.voenmeh.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.my.voenmeh.R;

public class MapActivity extends AppCompatActivity {
    private boolean isULK = false;
    private int selectedFloor = 1;
    private RadioButton button_rg4;
    private RadioButton button_rg5;
    private SubsamplingScaleImageView map;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tracker);
        map = (SubsamplingScaleImageView) findViewById(R.id.map);
        button_rg4 = findViewById(R.id.rad_btn4);
        button_rg5 = findViewById(R.id.rad_btn5);
        map.setImage(ImageSource.resource(R.drawable.map11));
        findViewById(R.id.rad_btn1).setOnClickListener(this::onRadioButtonClicked);
        findViewById(R.id.rad_btn2).setOnClickListener(this::onRadioButtonClicked);
        findViewById(R.id.rad_btn3).setOnClickListener(this::onRadioButtonClicked);
        findViewById(R.id.rad_btn4).setOnClickListener(this::onRadioButtonClicked);
        findViewById(R.id.rad_btn5).setOnClickListener(this::onRadioButtonClicked);
        findViewById(R.id.switch1).setOnClickListener(this::onRadioButtonClicked);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void onRadioButtonClicked(View view) {
        RadioGroup floorGroup = findViewById(R.id.group1);
        int floorId = floorGroup.getCheckedRadioButtonId();
        RadioButton floorButton = findViewById(floorId);
        selectedFloor = Integer.parseInt(floorButton.getText().toString());
        SwitchCompat buildingSwitch = findViewById(R.id.switch1);
        isULK = buildingSwitch.isChecked();
        updateMapImage();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateMapImage() {
        if (isULK) {
            switch (selectedFloor) {
                case 1:
                    map.setImage(ImageSource.resource(R.drawable.map21));
                    break;
                case 2:
                    map.setImage(ImageSource.resource(R.drawable.map22));
                    break;
                case 3:
                    map.setImage(ImageSource.resource(R.drawable.map23));
                    break;
                case 4:
                    map.setImage(ImageSource.resource(R.drawable.map24));
                    break;
                case 5:
                    map.setImage(ImageSource.resource(R.drawable.map25));
                    break;
            }
            button_rg4.setBackground(getResources().getDrawable(R.drawable.button_rg4_1));
            button_rg5.setClickable(true);
            button_rg5.setBackground(getResources().getDrawable(R.drawable.button_rg5));
        } else {
            switch (selectedFloor) {
                case 1:
                    map.setImage(ImageSource.resource(R.drawable.map11));
                    break;
                case 2:
                    map.setImage(ImageSource.resource(R.drawable.map12));
                    break;
                case 3:
                    map.setImage(ImageSource.resource(R.drawable.map13));
                    break;
                case 4:
                    map.setImage(ImageSource.resource(R.drawable.map14));
                    break;
                case 5:
                    map.setImage(ImageSource.resource(R.drawable.map14));
                    button_rg4.setChecked(true);
                    break;
            }
            button_rg4.setBackground(getResources().getDrawable(R.drawable.button_rg4));
            button_rg5.setClickable(false);
            button_rg5.setBackground(null);
        }
    }
}