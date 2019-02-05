package com.github.colaalex.cataclysmar.domain;

import android.util.Log;

import com.github.colaalex.cataclysmar.domain.entity.Wildfire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.AFRICA_EAST;
import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.AFRICA_NORTH;
import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.AFRICA_SOUTH;
import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.AFRICA_WEST;
import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.AUSTRALIA_EAST;
import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.AUSTRALIA_NORTH;
import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.AUSTRALIA_SOUTH;
import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.AUSTRALIA_WEST;

public class CSVWorker {

    private static final int AFRICA = 0;
    private static final int AUSTRALIA = 1;

    private InputStream inputStream;

    public CSVWorker(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public List<List<Float>> read() {
        //List<String[]> list = new ArrayList<>();

        List<Wildfire> africanWildfires = new ArrayList<>();
        List<Wildfire> australianWildfires = new ArrayList<>();

        List<List<Float>> coordinates = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String line;
            //int i = 0;
            while ((line = reader.readLine()) != null) {
                //i++;
                String[] row = line.split(",");
                //list.add(row);
                try {
                    List<Float> pair = new ArrayList<>();

                    float lat = Float.parseFloat(row[0]);
                    float lon = Float.parseFloat(row[1]);

//                    if (lat >= AUSTRALIA_SOUTH && lat <= AUSTRALIA_NORTH && lon >= AUSTRALIA_WEST && lon <= AUSTRALIA_EAST) {
//                        pair.add(lat);
//                        pair.add(lon);
//                        coordinates.add(pair);
//                        wildfires.add(new Wildfire(lat, lon, Integer.parseInt(row[8])));
//                    }

                    switch (determineContinent(lat, lon)) {
                        case AFRICA:
                            africanWildfires.add(new Wildfire(lat, lon, Integer.parseInt(row[8].replaceAll("\\s", ""))));
                            break;
                        case AUSTRALIA:
                            australianWildfires.add(new Wildfire(lat, lon, Integer.parseInt(row[8].replaceAll("\\s", ""))));
                            break;
                        default:
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(africanWildfires, (wildfire, t1) -> {
            if (wildfire.getConfidence() == t1.getConfidence())
                return 0;
            return wildfire.getConfidence() < t1.getConfidence() ? -1 : 1;
        });

        Collections.sort(australianWildfires, (wildfire, t1) -> {
            if (wildfire.getConfidence() == t1.getConfidence())
                return 0;
            return wildfire.getConfidence() < t1.getConfidence() ? -1 : 1;
        });

        for (int i = 0; i < 200 && i < africanWildfires.size(); i++) {
            List<Float> pair = new ArrayList<>();
            pair.add(africanWildfires.get(i).getLatitude());
            pair.add(africanWildfires.get(i).getLongitutde());
            coordinates.add(pair);
        }

        for (int i = 0; i < 200 && i < australianWildfires.size(); i++) {
            List<Float> pair = new ArrayList<>();
            pair.add(australianWildfires.get(i).getLatitude());
            pair.add(australianWildfires.get(i).getLongitutde());
            coordinates.add(pair);
        }

        Log.d("Africa length: ", String.valueOf(africanWildfires.size()));
        Log.d("Australia length: ", String.valueOf(australianWildfires.size()));
        Log.d("Coordiantes length: ", String.valueOf(coordinates.size()));

        return coordinates;
    }

    private int determineContinent(float lat, float lon) {
        if (lat >= AUSTRALIA_SOUTH && lat <= AUSTRALIA_NORTH && lon >= AUSTRALIA_WEST && lon <= AUSTRALIA_EAST)
            return AUSTRALIA;
        else if (lat >= AFRICA_SOUTH && lat <= AFRICA_NORTH && lon >= AFRICA_WEST && lon <= AFRICA_EAST)
            return AFRICA;
        else
            return -1;
    }
}
