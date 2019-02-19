package com.github.colaalex.cataclysmar.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.github.colaalex.cataclysmar.R;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class StartActivity extends AppCompatActivity {

    Button btnDay;
    Button btnWeek;
    Button btnMonth;
    Button btnFire;
    Button btnQuake;

    int selectedTime; // button ids are used
    HashMap<Integer, Boolean> selectedDisastes;

    @SuppressLint("UseSparseArrays")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        selectedDisastes = new HashMap<>();

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
        selectedDisastes.put(R.id.btnFire, false);

        btnQuake = findViewById(R.id.btnQuake);
        btnQuake.setOnClickListener(view -> toggleDisaster(R.id.btnQuake));
        selectedDisastes.put(R.id.btnQuake, false);

        Button startButton = findViewById(R.id.btnStart);
        startButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SceneActivity.class);
            intent.putExtra("time", selectedTime);
            intent.putExtra("disasters", selectedDisastes);
            //TODO проверка на непустой ввод
            startActivity(intent);
        });
    }

    private void toggleTime(int buttonId) {
        btnDay.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.buttonDeactivated), PorterDuff.Mode.MULTIPLY);
        btnWeek.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.buttonDeactivated), PorterDuff.Mode.MULTIPLY);
        btnMonth.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.buttonDeactivated), PorterDuff.Mode.MULTIPLY);
        selectedTime = buttonId;
        switch (buttonId) {
            case R.id.btnDay:
                btnDay.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.buttonActivated), PorterDuff.Mode.MULTIPLY);
                break;
            case R.id.btnWeek:
                btnWeek.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.buttonActivated), PorterDuff.Mode.MULTIPLY);
                break;
            case R.id.btnMonth:
                btnMonth.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.buttonActivated), PorterDuff.Mode.MULTIPLY);
                break;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void toggleDisaster(int buttonId) {
        switch (buttonId) {
            case R.id.btnFire:
                if (selectedDisastes.get(R.id.btnFire)) {
                    btnFire.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.buttonDeactivated), PorterDuff.Mode.MULTIPLY);
                } else
                    btnFire.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.buttonActivated), PorterDuff.Mode.MULTIPLY);
                break;
            case R.id.btnQuake:
                if (selectedDisastes.get(R.id.btnQuake)) {
                    btnQuake.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.buttonDeactivated), PorterDuff.Mode.MULTIPLY);
                } else
                    btnQuake.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.buttonActivated), PorterDuff.Mode.MULTIPLY);
                break;
        }
        selectedDisastes.replace(buttonId, !selectedDisastes.get(buttonId));
    }
}
