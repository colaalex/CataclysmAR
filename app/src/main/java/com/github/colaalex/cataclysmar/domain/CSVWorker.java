package com.github.colaalex.cataclysmar.domain;

import com.github.colaalex.cataclysmar.domain.entity.Wildfire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVWorker {

    private static final float AFRICA_NORTH = 37.34f;
    private static final float AFRICA_SOUTH = -34.82f;
    private static final float AFRICA_WEST = -17.53f;
    private static final float AFRICA_EAST = 51.4f;

    private static final float AUSTRALIA_NORTH = -10.689167f;
    private static final float AUSTRALIA_SOUTH = -43.644444f;
    private static final float AUSTRALIA_WEST = 113.155f;
    private static final float AUSTRALIA_EAST = 153.637222f;

    private InputStream inputStream;

    public CSVWorker(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public List<List<Float>> read() {
        //List<String[]> list = new ArrayList<>();
        List<Wildfire> wildfires = new ArrayList<>();
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

                    if (lat >= AUSTRALIA_SOUTH && lat <= AUSTRALIA_NORTH && lon >= AUSTRALIA_WEST && lon <= AUSTRALIA_EAST) {
                        pair.add(lat);
                        pair.add(lon);
                        coordinates.add(pair);
                        wildfires.add(new Wildfire(lat, lon, Integer.parseInt(row[8])));
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

        return coordinates;
    }
}
