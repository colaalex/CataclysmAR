package com.github.colaalex.cataclysmar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVWorker {

    private InputStream inputStream;

    public CSVWorker(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public List<List<Float>> read() {
        List<String[]> list = new ArrayList<>();
        List<List<Float>> coordinates = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null && i < 5) {
                i++;
                String[] row = line.split(",");
                list.add(row);
                try {
                    List<Float> pair = new ArrayList<>();
                    pair.add(Float.parseFloat(row[0]));
                    pair.add(Float.parseFloat(row[1]));
                    coordinates.add(pair);
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
