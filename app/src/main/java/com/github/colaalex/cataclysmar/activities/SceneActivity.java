package com.github.colaalex.cataclysmar.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.github.colaalex.cataclysmar.R;
import com.github.colaalex.cataclysmar.pins.FirePin;
import com.github.colaalex.cataclysmar.workers.CSVWorker;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import static com.github.colaalex.cataclysmar.pojo.Constants.LIFT;
import static com.github.colaalex.cataclysmar.pojo.Constants.RADIUS;

public class SceneActivity extends AppCompatActivity {

    private Renderable pinRenderable;
    private TransformableNode earth;
    private Renderable earthRenderable;
    private ArFragment arFragment;
    private Scene scene;

    private int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        setTextures();
        scene = arFragment.getArSceneView().getScene();

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (earthRenderable == null)
                        return;

                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(scene);

                    earth = new TransformableNode(arFragment.getTransformationSystem());
                    earth.setParent(anchorNode);
                    earth.setRenderable(earthRenderable);
                    earth.select();

                    setupPins();
                }
        );
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

    private void setupPin(FirePin firePin) {
        Log.d("Setup Pin", "Started drawing pin no. " + Integer.toString(i++));

        Node pinNode = new Node();
        float x = firePin.getX();
        float y = firePin.getY();
        float z = firePin.getZ();
        MaterialFactory.makeOpaqueWithColor(this, new Color(android.graphics.Color.RED))
                .thenAccept(
                        material -> {
                            pinRenderable =
                                    ShapeFactory.makeCylinder(0.001f, 0.01f, Vector3.zero(), material);
                            pinNode.setRenderable(pinRenderable);
                            pinNode.setParent(earth);
                            pinNode.setLocalPosition(new Vector3(x, 0.5f * RADIUS + y, z));
                            pinNode.setLocalRotation(firePin.getRotationQuaternion());
                        }
                );

        Log.d("Setup Pin", "Finished drawing pin no. " + Integer.toString(i));
    }

    private void setupPins() {

        Log.d("Setup Pins", "Method started");

        new Thread(() -> {
            Log.d("Setup Pins Run", "Started inner method");
            CSVWorker worker = new CSVWorker(getResources().openRawResource(R.raw.wfdset));
            List<FirePin> coordinates = worker.read();

            for (FirePin firePin :
                    coordinates) {
                runOnUiThread(() -> setupPin(firePin));

            }
            Log.d("Setup Pins Run", "Finished inner method");
        }).start();

        Log.d("Setup Pins", "Method ended");
    }
}
