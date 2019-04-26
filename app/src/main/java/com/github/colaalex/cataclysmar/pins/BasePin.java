package com.github.colaalex.cataclysmar.pins;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.github.colaalex.cataclysmar.pojo.Disaster;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.TransformableNode;

import androidx.annotation.NonNull;

import static com.github.colaalex.cataclysmar.pojo.Constants.RADIUS;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public abstract class BasePin extends Node {

    private Disaster disaster;

    private Quaternion rotationQuaternion;

    private float x;
    private float y;
    private float z;

    BasePin(Disaster disaster) {
        this.disaster = disaster;
    }

    void setTextureAndLocation(Context context, int color, TransformableNode earth, float radius) {
        Quaternion q1 = Quaternion.axisAngle(Vector3.back(), disaster.getLatitude() + 90);
        Quaternion q2 = Quaternion.axisAngle(Vector3.right(), disaster.getLongitude() + 180);
        rotationQuaternion = Quaternion.multiply(q1, q2);

        MaterialFactory.makeOpaqueWithColor(context, new Color(color))
                .thenAccept(
                        material -> {
                            Renderable pinRenderable =
                                    ShapeFactory.makeCylinder(0.001f, radius, Vector3.zero(), material);
                            setRenderable(pinRenderable);
                            setParent(earth);
                            setLocalPosition(new Vector3(x, 0.5f * RADIUS + y, z));
                            setLocalRotation(rotationQuaternion);
                        }
                );
    }

    void setModelTextureAndLocation(Context context, TransformableNode earth, float radius) {
        ModelRenderable.builder()
                .setSource(context, Uri.parse("model.sfb"))
                .build()
                .thenAccept(modelRenderable -> {
                    setRenderable(modelRenderable);
                    setParent(earth);
                    setLocalPosition(new Vector3(x, 0.5f * RADIUS + y, z));
                    //setLocalPosition(new Vector3(0, 0, 0));
                    setLocalRotation(rotationQuaternion);
                    setLocalScale(new Vector3(radius, radius, radius));
                })
                .exceptionally(throwable -> {Log.e("BasePin", "Couldn't load renderable", throwable); return null;});

    }

    void setPosition(float latitude, float longitude) {
        Quaternion q1 = Quaternion.axisAngle(Vector3.forward(), latitude + 90);
        Quaternion q2 = Quaternion.axisAngle(Vector3.left(), longitude + 180);
        rotationQuaternion = Quaternion.multiply(q1, q2);

        double theta = toRadians(latitude) + PI * 1.5;
        double phi = toRadians(longitude) + PI * 0.5;
        x = (float) (RADIUS * sin(theta) * sin(phi));
        y = (float) (RADIUS + RADIUS * cos(theta));
        z = (float) (RADIUS * sin(theta) * cos(phi));
    }

    public abstract void setup(Context context, TransformableNode earth);

    @Override
    @NonNull
    public String toString() {
        return disaster.toString();
    }
}
