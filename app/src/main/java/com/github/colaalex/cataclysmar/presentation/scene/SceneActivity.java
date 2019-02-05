package com.github.colaalex.cataclysmar.presentation.scene;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.github.colaalex.cataclysmar.R;
import com.github.colaalex.cataclysmar.domain.CSVWorker;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.List;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public class SceneActivity extends AppCompatActivity {

    private static final float RADIUS = 0.1f;
    private static final float LIFT = 0.15f;

    //private float lat = 55.751244f;
    //private float lon = 37.618423f;

    private Renderable pinRenderable;
    private TransformableNode earth;

    private Renderable earthRenderable;
    private ArFragment arFragment;
    private Scene scene;

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

    private void setupPin(float x, float y, float z, float lat, float lon) {
        Node pinNode = new Node();
        MaterialFactory.makeOpaqueWithColor(this, new Color(android.graphics.Color.RED))
                .thenAccept(
                        material -> {
                            pinRenderable =
                                    ShapeFactory.makeCylinder(0.001f, 0.01f, Vector3.zero(), material);
                            pinNode.setRenderable(pinRenderable);
                            pinNode.setParent(earth);
                            pinNode.setLocalPosition(new Vector3(x, 0.5f * RADIUS + y, z));
                            Quaternion q1 = Quaternion.axisAngle(Vector3.forward(), lat + 90);
                            Quaternion q2 = Quaternion.axisAngle(Vector3.left(), lon);
                            pinNode.setLocalRotation(Quaternion.multiply(q1, q2));
                        }
                );
    }

    private void setupPins() {
        CSVWorker worker = new CSVWorker(getResources().openRawResource(R.raw.wfdset));
        List<List<Float>> coordinates = worker.read();

        for (List<Float> pair :
                coordinates) {
            float lat = pair.get(0);
            float lon = pair.get(1);


            double theta = toRadians(lat) + PI * 1.5;
            double phi = toRadians(lon) + PI * 0.5;
            float x = (float) (RADIUS * sin(theta) * sin(phi));
            float y = (float) (RADIUS + RADIUS * cos(theta));
            float z = (float) (RADIUS * sin(theta) * cos(phi));

            setupPin(x, y, z, lat, lon);
        }
    }
}
