package com.github.colaalex.cataclysmar.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.github.colaalex.cataclysmar.App;
import com.github.colaalex.cataclysmar.R;
import com.github.colaalex.cataclysmar.database.AppDatabase;
import com.github.colaalex.cataclysmar.database.DisasterDao;
import com.github.colaalex.cataclysmar.database.DisasterEntity;
import com.github.colaalex.cataclysmar.pins.BasePin;
import com.github.colaalex.cataclysmar.pins.FireClusterPin;
import com.github.colaalex.cataclysmar.pins.FirePin;
import com.github.colaalex.cataclysmar.pins.QuakeClusterPin;
import com.github.colaalex.cataclysmar.pins.QuakePin;
import com.github.colaalex.cataclysmar.pojo.Constants;
import com.github.colaalex.cataclysmar.pojo.Disaster;
import com.github.colaalex.cataclysmar.pojo.FireCluster;
import com.github.colaalex.cataclysmar.pojo.Quake;
import com.github.colaalex.cataclysmar.pojo.QuakeCluster;
import com.github.colaalex.cataclysmar.pojo.Wildfire;
import com.github.colaalex.cataclysmar.workers.CSVWorker;
import com.github.colaalex.cataclysmar.workers.DataDownloadWorker;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import static com.github.colaalex.cataclysmar.pojo.Constants.LIFT;
import static com.github.colaalex.cataclysmar.pojo.Constants.RADIUS;

public class SceneActivity extends AppCompatActivity {

    private static final int DAY = 1;
    private static final int TWO_DAYS = 2;
    private static final int WEEK = 3;

    private boolean isPlaced;

    private TransformableNode earth;
    private Renderable earthRenderable;
    private Node infoCard;
    private ViewRenderable cardRenderable;
    private ArFragment arFragment;
    private Scene scene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        setTextures();
        scene = arFragment.getArSceneView().getScene();

        isPlaced = false;

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (earthRenderable == null)
                        return;

                    if (isPlaced)
                        return;

                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(scene);

                    earth = new TransformableNode(arFragment.getTransformationSystem());
                    earth.setParent(anchorNode);
                    earth.setRenderable(earthRenderable);
                    earth.select();

                    isPlaced = true;

                    infoCard = new Node();
                    infoCard.setParent(earth);
                    infoCard.setLocalPosition(new Vector3(0.0f, RADIUS * 2.5f, 0.0f));
                    ViewRenderable.builder()
                            .setView(this, R.layout.pin_info)
                            .build()
                            .thenAccept(viewRenderable -> {
                                cardRenderable = viewRenderable;
                                infoCard.setRenderable(cardRenderable);
                            });


                    passIntent();
                }
        );
    }

    private void passIntent() {
        int selectedTime = getIntent().getIntExtra("time", 0);
        int selectedDisaster = getIntent().getIntExtra("disaster", 0);
        int maxLoad = getIntent().getIntExtra("maxLoad", 0);
        switch (selectedTime) {
            case R.id.btnDay:
                setupPins(DAY, selectedDisaster, maxLoad);
                break;
            case R.id.btnWeek:
                setupPins(TWO_DAYS, selectedDisaster, maxLoad);
                break;
            case R.id.btnMonth:
                setupPins(WEEK, selectedDisaster, maxLoad);
                break;
        }
    }

    private void setTextures() {
        Texture.Builder builder = Texture.builder();
        builder.setSource(this, R.drawable.earth);
        builder.build().thenAccept(texture ->
                MaterialFactory.makeOpaqueWithTexture(this, texture).thenAccept(
                        material -> earthRenderable =
                                ShapeFactory.makeSphere(RADIUS, new Vector3(0.0f, LIFT, 0.0f), material)
                ));
    }

    private void setupPin(Disaster disaster) {
        Log.d("Setup Pin", "Started drawing pin");

        BasePin pin;
        if (disaster instanceof Wildfire)
            pin = new FirePin((Wildfire) disaster);
        else if (disaster instanceof Quake)
            pin = new QuakePin((Quake) disaster);
        else if (disaster instanceof FireCluster)
            pin = new FireClusterPin((FireCluster) disaster);
        else if (disaster instanceof QuakeCluster)
            pin = new QuakeClusterPin((QuakeCluster) disaster);
        else
            throw new RuntimeException("Disaster is not an instance of suitable class");

        pin.setup(this, earth);
        pin.setOnTapListener((hitTestResult, motionEvent) -> ((TextView) cardRenderable.getView()).setText(pin.toString()));

        Log.d("Setup Pin", "Finished drawing pin");
    }

    private void setupPins(int period, int selectedDisaster, int maxLoad) {

        Log.d("Setup Pins", "Method started");

        new Thread(() -> {
            Log.d("Setup Pins Run", "Started inner method");

            CSVWorker worker = null;
            String disType = "";
            DataDownloadWorker downloadWorker = new DataDownloadWorker();
            long startTime = 0;

            boolean offlineFlag = false;

            switch (period) {
                case WEEK:
                    if (selectedDisaster == R.id.btnFire)
                        try {
                            InputStream inputStream = downloadWorker.getClusterFile("fire", "7");
                            worker = new CSVWorker(inputStream, Constants.FIRE);
                        } catch (Exception e) {
                            //если не сможет скачать, будем использовать кэш
                            offlineFlag = true;
                            disType = "fire";
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DAY_OF_YEAR, -7);
                            startTime = cal.getTimeInMillis();
                        }
                    else
                        try {
                            InputStream inputStream = downloadWorker.getClusterFile("quake", "30");
                            worker = new CSVWorker(inputStream, Constants.QUAKE);
                        } catch (Exception e) {
                            offlineFlag = true;
                            disType = "quake";
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DAY_OF_YEAR, -30);
                            startTime = cal.getTimeInMillis();
                        }
                    break;
                case DAY:
                    if (selectedDisaster == R.id.btnFire)
                        try {
                            InputStream inputStream = downloadWorker.getClusterFile("fire", "24");
                            worker = new CSVWorker(inputStream, Constants.FIRE);
                        } catch (Exception e) {
                            offlineFlag = true;
                            disType = "fire";
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DAY_OF_YEAR, -1);
                            startTime = cal.getTimeInMillis();
                        }
                    else
                        try {
                            InputStream inputStream = downloadWorker.getClusterFile("quake", "24");
                            worker = new CSVWorker(inputStream, Constants.QUAKE);
                        } catch (Exception e) {
                            offlineFlag = true;
                            disType = "quake";
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DAY_OF_YEAR, -1);
                            startTime = cal.getTimeInMillis();
                        }
                    break;
                case TWO_DAYS:
                    if (selectedDisaster == R.id.btnFire)
                        try {
                            InputStream inputStream = downloadWorker.getClusterFile("fire", "48");
                            worker = new CSVWorker(inputStream, Constants.FIRE);
                        } catch (Exception e) {
                            offlineFlag = true;
                            disType = "fire";
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DAY_OF_YEAR, -2);
                            startTime = cal.getTimeInMillis();
                        }
                    else
                        try {
                            InputStream inputStream = downloadWorker.getClusterFile("quake", "7");
                            //week, потому что нет отдельного файла для двух дней
                            worker = new CSVWorker(inputStream, Constants.QUAKE);
                        } catch (Exception e) {
                            offlineFlag = true;
                            disType = "quake";
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DATE, -7);
                            startTime = cal.getTimeInMillis();
                        }
                    break;
                default:
                    throw new RuntimeException("Couldn't understand query");
            }

            List<Disaster> coordinates;
            if (!offlineFlag) {
                coordinates = worker.read(maxLoad);
            } else {
                AppDatabase db = App.getInstance().getDatabase();
                DisasterDao disasterDao = db.disasterDao();
                List<DisasterEntity> entities = disasterDao.getDisastersByType(disType, startTime / 1000, System.currentTimeMillis() / 1000);
                coordinates = new ArrayList<>();
                for (DisasterEntity entity : entities) {
                    if (entity.disasterType.equals("fire"))
                        coordinates.add(new FireCluster(entity.latitude, entity.longitude, entity.size));
                    else if (entity.disasterType.equals("quake"))
                        coordinates.add(new QuakeCluster(entity.latitude, entity.longitude, entity.size));
                }
            }

            for (Disaster disaster : coordinates) {
                Log.d("Coordinates length:", String.valueOf(coordinates.size()));
                runOnUiThread(() -> setupPin(disaster));
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            Log.d("Setup Pins Run", "Finished inner method");
        }).start();

        Log.d("Setup Pins", "Method ended");
    }
}
