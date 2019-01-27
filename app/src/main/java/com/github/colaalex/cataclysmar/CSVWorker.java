package com.github.colaalex.cataclysmar;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVWorker {

    public static List<Wildfire> getWildfires(InputStream fn) {
        List<Wildfire> wildfires = new ArrayList<>();
        CSVReader reader;
        CsvToBean<Wildfire> csv = new CsvToBean<>();

        reader = new CSVReader(new InputStreamReader(fn), ',', '"', 1);
        List<Wildfire> list = csv.parse(setColumnMapping(), reader);
        for (Object objeect :
                list) {
            Wildfire wildfire = (Wildfire) objeect;
            wildfires.add(wildfire);
        }

        return wildfires;
    }

    private static ColumnPositionMappingStrategy<Wildfire> setColumnMapping() {
        ColumnPositionMappingStrategy<Wildfire> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(Wildfire.class);
        String[] columns = new String[]{"latitude", "longitude", "brightness", "scan", "track", "acq_date", "acq_time", "satellite", "confidence", "version", "bright_t31", "frp", "daynight"};
        strategy.setColumnMapping(columns);
        return strategy;
    }
}
