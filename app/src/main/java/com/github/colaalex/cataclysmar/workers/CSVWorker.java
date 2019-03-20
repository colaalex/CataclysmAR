package com.github.colaalex.cataclysmar.workers;

import android.util.SparseArray;

import com.github.colaalex.cataclysmar.pojo.Disaster;
import com.github.colaalex.cataclysmar.pojo.Quake;
import com.github.colaalex.cataclysmar.pojo.Wildfire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.colaalex.cataclysmar.pojo.Constants.AFRICA_EAST;
import static com.github.colaalex.cataclysmar.pojo.Constants.AFRICA_NORTH;
import static com.github.colaalex.cataclysmar.pojo.Constants.AFRICA_SOUTH;
import static com.github.colaalex.cataclysmar.pojo.Constants.AFRICA_WEST;
import static com.github.colaalex.cataclysmar.pojo.Constants.AUSTRALIA_EAST;
import static com.github.colaalex.cataclysmar.pojo.Constants.AUSTRALIA_NORTH;
import static com.github.colaalex.cataclysmar.pojo.Constants.AUSTRALIA_SOUTH;
import static com.github.colaalex.cataclysmar.pojo.Constants.AUSTRALIA_WEST;
import static com.github.colaalex.cataclysmar.pojo.Constants.EURASIA_EAST;
import static com.github.colaalex.cataclysmar.pojo.Constants.EURASIA_NORTH;
import static com.github.colaalex.cataclysmar.pojo.Constants.EURASIA_SOUTH;
import static com.github.colaalex.cataclysmar.pojo.Constants.EURASIA_WEST;
import static com.github.colaalex.cataclysmar.pojo.Constants.FIRE;
import static com.github.colaalex.cataclysmar.pojo.Constants.NAMERICA_EAST;
import static com.github.colaalex.cataclysmar.pojo.Constants.NAMERICA_NORTH;
import static com.github.colaalex.cataclysmar.pojo.Constants.NAMERICA_SOUTH;
import static com.github.colaalex.cataclysmar.pojo.Constants.NAMERICA_WEST;
import static com.github.colaalex.cataclysmar.pojo.Constants.QUAKE;
import static com.github.colaalex.cataclysmar.pojo.Constants.SAMERICA_EAST;
import static com.github.colaalex.cataclysmar.pojo.Constants.SAMERICA_NORTH;
import static com.github.colaalex.cataclysmar.pojo.Constants.SAMERICA_SOUTH;
import static com.github.colaalex.cataclysmar.pojo.Constants.SAMERICA_WEST;
import static java.lang.Math.min;

public class CSVWorker {

    private int maxLoad = 1200;

    private static final int AFRICA = 0;
    private static final int AUSTRALIA = 1;
    private static final int EURASIA = 2;
    private static final int NAMERICA = 3;
    private static final int SAMERICA = 4;

    private InputStream inputStream;
    private int disaster;

    public CSVWorker(InputStream inputStream, int disaster) {
        this.inputStream = inputStream;
        this.disaster = disaster;
    }

    public List<Disaster> read(int maxLoad) {
        this.maxLoad = maxLoad;
        switch (disaster) {
            case FIRE:
                return readFire();
            case QUAKE:
                return readQuake();
            default:
                return null;
        }
    }

    private List<Disaster> readFire() {

        SparseArray<List<Wildfire>> wildfires = new SparseArray<>(5);
        for (int i = 0; i < 5; i++)
            wildfires.append(i, new ArrayList<>());

        List<Disaster> coordinates = new ArrayList<>();
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
            int len = i != EURASIA ? maxLoad / 6 : maxLoad / 4; // сколько элементов максимум может быть у одного континента
            Collections.sort(wildfires.get(i), ((wildfire, t1) -> {
                if (wildfire.getConfidence() == t1.getConfidence())
                    return 0;
                return wildfire.getConfidence() < t1.getConfidence() ? -1 : 1;
            }));
            for (int j = 0; j < len; j++) {
                try {
                    coordinates.add(wildfires.get(i).get(j));
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }

        return coordinates;
    }

    private List<Disaster> readQuake() {
        List<Quake> quakes = new ArrayList<>();

        List<Disaster> coordinates = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                try {

                    float lat = Float.parseFloat(row[1]);
                    float lon = Float.parseFloat(row[2]);

                    quakes.add(new Quake(lat, lon, Integer.parseInt(row[3].replaceAll("\\s", ""))));
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

        Collections.sort(quakes, ((wildfire, t1) -> {
            if (wildfire.getMagnitude() == t1.getMagnitude())
                return 0;
            return wildfire.getMagnitude() < t1.getMagnitude() ? -1 : 1;
        }));

        for (int i = 0; i < min(maxLoad, quakes.size()); i++)
            coordinates.add(quakes.get(i));

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
