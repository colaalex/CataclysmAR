package com.github.colaalex.cataclysmar.pins;

import com.github.colaalex.cataclysmar.pojo.Wildfire;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

import static com.github.colaalex.cataclysmar.pojo.Constants.RADIUS;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public class FirePin {

    private Wildfire wildfire;

    private float latitude;
    private float longitutde;
    private int confidence;
    private Quaternion rotationQuaternion;

    private float x;
    private float y;
    private float z;

    public FirePin(Wildfire wildfire) {
        this.wildfire = wildfire;
        latitude = wildfire.getLatitude();
        longitutde = wildfire.getLongitutde();
        confidence = wildfire.getConfidence();

        Quaternion q1 = Quaternion.axisAngle(Vector3.forward(), latitude + 90);
        Quaternion q2 = Quaternion.axisAngle(Vector3.left(), longitutde);
        rotationQuaternion = Quaternion.multiply(q1, q2);

        double theta = toRadians(latitude) + PI * 1.5;
        double phi = toRadians(longitutde) + PI * 0.5;
        x = (float) (RADIUS * sin(theta) * sin(phi));
        y = (float) (RADIUS + RADIUS * cos(theta));
        z = (float) (RADIUS * sin(theta) * cos(phi));
    }

    public Quaternion getRotationQuaternion() {
        return rotationQuaternion;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}
