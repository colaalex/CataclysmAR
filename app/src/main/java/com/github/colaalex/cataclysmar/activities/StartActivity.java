package com.github.colaalex.cataclysmar.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.github.colaalex.cataclysmar.R;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    Button btnDay;
    Button btnWeek;
    Button btnMonth;
    Button btnFire;
    Button btnQuake;
//    TextView tvMaxLoad;
//    SeekBar sbMaxLoad;

    int selectedTime; // button ids are used
    int selectedDisaster;

    @SuppressLint("UseSparseArrays")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        selectedDisaster = selectedTime = 0;

        Spinner regionSpinner = findViewById(R.id.srRegion);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.regions_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionSpinner.setAdapter(adapter);

        btnDay = findViewById(R.id.btnDay);
        btnDay.setOnClickListener(view -> toggleTime(R.id.btnDay));

        btnWeek = findViewById(R.id.btnWeek);
        btnWeek.setOnClickListener(view -> toggleTime(R.id.btnWeek));

        btnMonth = findViewById(R.id.btnMonth);
        btnMonth.setOnClickListener(view -> toggleTime(R.id.btnMonth));

        btnFire = findViewById(R.id.btnFire);
        btnFire.setOnClickListener(view -> toggleDisaster(R.id.btnFire));

        btnQuake = findViewById(R.id.btnQuake);
        btnQuake.setOnClickListener(view -> toggleDisaster(R.id.btnQuake));

//        tvMaxLoad = findViewById(R.id.tvMaxLoad);
//
//        sbMaxLoad = findViewById(R.id.sbMaxLoad);
//        sbMaxLoad.setMax(60);
//        sbMaxLoad.setProgress(10);
//        tvMaxLoad.setText(String.format(getResources().getString(R.string.maxLoad), sbMaxLoad.getProgress() * 100));
//        sbMaxLoad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                tvMaxLoad.setText(String.format(getResources().getString(R.string.maxLoad), progress * 100));
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

        Button startButton = findViewById(R.id.btnStart);
        startButton.setOnClickListener(view -> {
            if (selectedTime == 0 || selectedDisaster == 0)
                return;
            Intent intent = new Intent(getApplicationContext(), SceneActivity.class);
            intent.putExtra("time", selectedTime);
            intent.putExtra("disaster", selectedDisaster);
            intent.putExtra("maxLoad", 1000);
            startActivity(intent);
        });
    }

    private void toggleTime(int buttonId) {
        btnDay.setBackgroundColor(getColor(R.color.buttonDeactivated));
        btnWeek.setBackgroundColor(getColor(R.color.buttonDeactivated));
        btnMonth.setBackgroundColor(getColor(R.color.buttonDeactivated));
        selectedTime = buttonId;
        switch (buttonId) {
            case R.id.btnDay:
                btnDay.setBackgroundColor(getColor(R.color.buttonActivated));
                break;
            case R.id.btnWeek:
                btnWeek.setBackgroundColor(getColor(R.color.buttonActivated));
                break;
            case R.id.btnMonth:
                btnMonth.setBackgroundColor(getColor(R.color.buttonActivated));
                break;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void toggleDisaster(int buttonId) {
        btnFire.setBackgroundColor(getColor(R.color.buttonDeactivated));
        btnQuake.setBackgroundColor(getColor(R.color.buttonDeactivated));
        selectedDisaster = buttonId;
        switch (buttonId) {
            case R.id.btnFire:
                btnFire.setBackgroundColor(getColor(R.color.buttonActivated));
                break;
            case R.id.btnQuake:
                btnQuake.setBackgroundColor(getColor(R.color.buttonActivated));
                break;
        }
    }
}
