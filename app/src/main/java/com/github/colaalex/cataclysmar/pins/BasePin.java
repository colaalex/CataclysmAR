package com.github.colaalex.cataclysmar.pins;

import android.content.Context;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.TransformableNode;

import static com.github.colaalex.cataclysmar.pojo.Constants.RADIUS;

abstract class BasePin extends Node {
    Quaternion rotationQuaternion;

    float x;
    float y;
    float z;

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

}
