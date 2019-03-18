package com.github.colaalex.cataclysmar.pins;

import android.content.Context;
import android.graphics.Color;

import com.github.colaalex.cataclysmar.pojo.Wildfire;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.ux.TransformableNode;

import androidx.annotation.NonNull;

import static com.github.colaalex.cataclysmar.pojo.Constants.RADIUS;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public class FirePin extends BasePin{

    private Wildfire wildfire;

    public FirePin(Wildfire wildfire) {
        this.wildfire = wildfire;
        float latitude = wildfire.getLatitude();
        float longitude = wildfire.getLongitude();

        Quaternion q1 = Quaternion.axisAngle(Vector3.forward(), latitude + 90);
        Quaternion q2 = Quaternion.axisAngle(Vector3.left(), longitude);
        rotationQuaternion = Quaternion.multiply(q1, q2);

        double theta = toRadians(latitude) + PI * 1.5;
        double phi = toRadians(longitude) + PI * 0.5;
        x = (float) (RADIUS * sin(theta) * sin(phi));
        y = (float) (RADIUS + RADIUS * cos(theta));
        z = (float) (RADIUS * sin(theta) * cos(phi));
    }

    public void setup(Context context, TransformableNode earth) {
        setTextureAndLocation(context, Color.RED, earth);
    }

    @NonNull
    @Override
    public String toString() {
        return wildfire.toString();
    }
}
