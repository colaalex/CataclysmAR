package com.github.colaalex.cataclysmar.domain;

import android.util.SparseArray;

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
import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.EURASIA_EAST;
import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.EURASIA_NORTH;
import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.EURASIA_SOUTH;
import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.EURASIA_WEST;
import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.NAMERICA_EAST;
import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.NAMERICA_NORTH;
import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.NAMERICA_SOUTH;
import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.NAMERICA_WEST;
import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.SAMERICA_EAST;
import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.SAMERICA_NORTH;
import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.SAMERICA_SOUTH;
import static com.github.colaalex.cataclysmar.domain.entity.Coordinates.SAMERICA_WEST;

public class CSVWorker {

    private static final int AFRICA = 0;
    private static final int AUSTRALIA = 1;
    private static final int EURASIA = 2;
    private static final int NAMERICA = 3;
    private static final int SAMERICA = 4;

    private InputStream inputStream;

    public CSVWorker(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public List<List<Float>> read() {

        SparseArray<List<Wildfire>> wildfires = new SparseArray<>(5);
        for (int i = 0; i < 5; i++)
            wildfires.append(i, new ArrayList<>());

        List<List<Float>> coordinates = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                try {

                    float lat = Float.parseFloat(row[0]);
                    float lon = Float.parseFloat(row[1]);

                    int cont = determineContinent(lat, lon);

                    if (cont != -1) {
                        wildfires.get(cont).add(new Wildfire(lat, lon, Integer.parseInt(row[8].replaceAll("\\s", ""))));
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

        for (int i = 0; i < 5; i++) {
            int len = i != EURASIA ? 200 : 400; // сколько элементов максимум может быть у одного континента
            Collections.sort(wildfires.get(i), ((wildfire, t1) -> {
                if (wildfire.getConfidence() == t1.getConfidence())
                    return 0;
                return wildfire.getConfidence() < t1.getConfidence() ? -1 : 1;
            }));
            for (int j = 0; j < len; j++) {
                List<Float> pair = new ArrayList<>();
                pair.add(wildfires.get(i).get(j).getLatitude());
                pair.add(wildfires.get(i).get(j).getLongitutde());
                coordinates.add(pair);
            }
        }

        return coordinates;
    }

    private int determineContinent(float lat, float lon) {
        if (lat >= AUSTRALIA_SOUTH && lat <= AUSTRALIA_NORTH && lon >= AUSTRALIA_WEST && lon <= AUSTRALIA_EAST)
            return AUSTRALIA;
        else if (lat >= AFRICA_SOUTH && lat <= AFRICA_NORTH && lon >= AFRICA_WEST && lon <= AFRICA_EAST)
            return AFRICA;
        else if (lat >= EURASIA_SOUTH && lat <= EURASIA_NORTH && lon >= EURASIA_WEST && lon <= EURASIA_EAST)
            return EURASIA;
        else if (lat >= NAMERICA_SOUTH && lat <= NAMERICA_NORTH && lon >= NAMERICA_WEST && lon <= NAMERICA_EAST)
            return NAMERICA;
        else if (lat >= SAMERICA_SOUTH && lat <= SAMERICA_NORTH && lon >= SAMERICA_WEST && lon <= SAMERICA_EAST)
            return SAMERICA;
        else
            return -1;
    }
}
