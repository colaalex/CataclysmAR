package com.github.colaalex.cataclysmar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.github.colaalex.cataclysmar.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Spinner regionSpinner = findViewById(R.id.srRegion);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.regions_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionSpinner.setAdapter(adapter);

        Button startButton = findViewById(R.id.btnStart);
        startButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SceneActivity.class);
            startActivity(intent);
        });
    }
}
