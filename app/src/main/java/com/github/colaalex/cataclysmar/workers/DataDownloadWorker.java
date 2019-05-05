package com.github.colaalex.cataclysmar.workers;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataDownloadWorker {

    public void getFile() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://firms.modaps.eosdis.nasa.gov/data/active_fire/c6/csv/MODIS_C6_Global_24h.csv")
                .build();

        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            Log.d("Downloader", response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InputStream getClusterFile(String disaster, String period) throws IOException {
        Log.d("Downloader", "Downloader started");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(String.format("http://ar.clxbox.host/api/%s/%s", disaster, period))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                Log.d("Downloader", "Download successful");
                return new ByteArrayInputStream(response.body().string().getBytes());
            }
            else {
                Log.e("Downloader", "Got null");
                throw new IOException("Got null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public InputStream getFireFile(String period) throws IOException {
        Log.d("Downloader", "Downloader started");
        OkHttpClient client = new OkHttpClient();
        Log.d("Downloader url", String.format("https://firms.modaps.eosdis.nasa.gov/data/active_fire/c6/csv/MODIS_C6_Global_%s.csv", period));

        Request request = new Request.Builder()
                .url(String.format("https://firms.modaps.eosdis.nasa.gov/data/active_fire/c6/csv/MODIS_C6_Global_%s.csv", period))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                Log.d("Downloader", "Download successful");
                //Log.d("Downloader output", response.body().string());
                //обратиться к response.body можно только один раз, потом он закрывается
                return new ByteArrayInputStream(response.body().string().getBytes());
                //return response.body().byteStream();
            }
            else {
                Log.e("Downloader", "Got null");
                throw new IOException("Got null");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public InputStream getQuakeFile(String period) throws IOException {
        Log.d("Downloader", "Downloader started");
        OkHttpClient client = new OkHttpClient();
        Log.d("Downloader url", String.format("https://firms.modaps.eosdis.nasa.gov/data/active_fire/c6/csv/MODIS_C6_Global_%s.csv", period));

        Request request = new Request.Builder()
                .url(String.format("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_%s.csv", period))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                Log.d("Downloader", "Download successful");
                //Log.d("Downloader output", response.body().string());
                //обратиться к response.body можно только один раз, потом он закрывается
                return new ByteArrayInputStream(response.body().string().getBytes());
                //return response.body().byteStream();
            }
            else {
                Log.e("Downloader", "Got null");
                throw new IOException("Got null");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
