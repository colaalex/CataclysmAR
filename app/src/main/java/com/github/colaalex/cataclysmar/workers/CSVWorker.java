package com.github.colaalex.cataclysmar.workers;

import com.github.colaalex.cataclysmar.App;
import com.github.colaalex.cataclysmar.database.AppDatabase;
import com.github.colaalex.cataclysmar.database.DisasterDao;
import com.github.colaalex.cataclysmar.database.DisasterEntity;
import com.github.colaalex.cataclysmar.pojo.Disaster;
import com.github.colaalex.cataclysmar.pojo.FireCluster;
import com.github.colaalex.cataclysmar.pojo.QuakeCluster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.github.colaalex.cataclysmar.pojo.Constants.FIRE;
import static com.github.colaalex.cataclysmar.pojo.Constants.QUAKE;

public class CSVWorker {

    private InputStream inputStream;
    private int disaster;

    private DisasterDao disasterDao;

    public CSVWorker(InputStream inputStream, int disaster) {
        this.inputStream = inputStream;
        this.disaster = disaster;

        AppDatabase db = App.getInstance().getDatabase();
        disasterDao = db.disasterDao();
    }

    public List<Disaster> read() {
        switch (disaster) {
            case FIRE:
                return readCluster(FIRE);
            case QUAKE:
                return readCluster(QUAKE);
            default:
                return null;
        }
    }

    private List<Disaster> readCluster(int disaster) {

        List<Disaster> clusters = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");

                try {
                    float lat = Float.parseFloat(row[0]);
                    float lon = Float.parseFloat(row[1]);
                    float size = Float.parseFloat(row[2]) + 1.0f;
                    long datetime = Long.parseLong(row[3].substring(0, row[3].indexOf(".")));

                    DisasterEntity entity = new DisasterEntity();
                    entity.datetime = datetime;
                    entity.latitude = lat;
                    entity.longitude = lon;
                    entity.size = size;

                    switch (disaster) {
                        case FIRE:
                            clusters.add(new FireCluster(lat, lon, size));
                            entity.disasterType = "fire";
                            break;
                        case QUAKE:
                            clusters.add(new QuakeCluster(lat, lon, size));
                            entity.disasterType = "quake";
                            break;
                    }

                    disasterDao.insertDisaster(entity);
                } catch (NumberFormatException e) {
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

        return clusters;

    }
}
