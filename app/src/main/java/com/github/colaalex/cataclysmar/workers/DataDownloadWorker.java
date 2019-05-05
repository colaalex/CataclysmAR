package com.github.colaalex.cataclysmar.workers;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataDownloadWorker {

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
}
