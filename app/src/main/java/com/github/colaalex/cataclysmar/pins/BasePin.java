package com.github.colaalex.cataclysmar.pins;

import android.content.Context;

import com.github.colaalex.cataclysmar.pojo.Disaster;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.TransformableNode;

import static com.github.colaalex.cataclysmar.pojo.Constants.RADIUS;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

abstract class BasePin extends Node {

    private Disaster disaster;

    private Quaternion rotationQuaternion;

    private float x;
    private float y;
    private float z;

    protected BasePin(Disaster disaster) {
        this.disaster = disaster;
    }

    void setTextureAndLocation(Context context, int color, TransformableNode earth) {
        MaterialFactory.makeOpaqueWithColor(context, new Color(color))
                .thenAccept(
                        material -> {
                            Renderable pinRenderable =
                                    ShapeFactory.makeCylinder(0.001f, 0.01f, Vector3.zero(), material);
                            setRenderable(pinRenderable);
                            setParent(earth);
                            setLocalPosition(new Vector3(x, 0.5f * RADIUS + y, z));
                            setLocalRotation(rotationQuaternion);
                        }
                );
    }

    void setPosition(float latitude, float longitude) {
        Quaternion q1 = Quaternion.axisAngle(Vector3.forward(), latitude + 90);
        Quaternion q2 = Quaternion.axisAngle(Vector3.left(), longitude);
        rotationQuaternion = Quaternion.multiply(q1, q2);

        double theta = toRadians(latitude) + PI * 1.5;
        double phi = toRadians(longitude) + PI * 0.5;
        x = (float) (RADIUS * sin(theta) * sin(phi));
        y = (float) (RADIUS + RADIUS * cos(theta));
        z = (float) (RADIUS * sin(theta) * cos(phi));
    }

    @Override
    public String toString() {
        return disaster.toString();
    }
}
