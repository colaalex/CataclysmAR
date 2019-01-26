package com.github.colaalex.cataclysmar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

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

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public class MainActivity extends AppCompatActivity {

    private static final float RADIUS = 0.1f;
    private float lat = 55.751244f;
    //private float lat = 0f;
    private float lon = 37.618423f;
    //private float lon = 0f;

    private Node pinNode;
    private Renderable pinRenderable;
    private TransformableNode earth;

    private Renderable renderable;
    private ArFragment arFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        Texture.Builder builder = Texture.builder();
        builder.setSource(this, R.drawable.earth);
        builder.build().thenAccept(texture ->
                MaterialFactory.makeOpaqueWithTexture(this, texture).thenAccept(
                        material -> renderable =
                                ShapeFactory.makeSphere(RADIUS, new Vector3(0.0f, 0.15f, 0.0f), material)
                ));

        Scene scene = arFragment.getArSceneView().getScene();

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (renderable == null)
                        return;

                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(scene);

                    earth = new TransformableNode(arFragment.getTransformationSystem());
                    earth.setParent(anchorNode);
                    earth.setRenderable(renderable);
                    earth.select();

                    double theta = toRadians(lat) + PI * 1.5;
                    double phi = toRadians(lon) + PI * 0.5;
                    float x = (float) (RADIUS * sin(theta) * sin(phi));
                    float y = (float) (RADIUS + RADIUS * cos(theta));
                    float z = (float) (RADIUS * sin(theta) * cos(phi));

                    pinNode = new Node();
                    ViewRenderable.builder()
                            .setView(this, R.layout.map_pin)
                            .build()
                            .thenAccept(viewRenderable -> {
                                viewRenderable.setSizer(view -> new Vector3(0.1f, 0.1f, 0.1f));
                                pinNode.setRenderable(viewRenderable);
                            });
//                    MaterialFactory.makeOpaqueWithColor(this, new Color(android.graphics.Color.RED))
//                            .thenAccept(
//                                    material -> pinRenderable =
//                                            ShapeFactory.makeCube(new Vector3(0.2f, 0.2f, 0.2f), new Vector3(x, y, z), material));
                    pinNode.setRenderable(pinRenderable);
                    pinNode.setParent(earth);
                    pinNode.setLocalPosition(new Vector3(x, y, z));
                }
        );
    }
}
