package com.github.colaalex.cataclysmar.pins;

import android.content.Context;
import android.graphics.Color;

import com.github.colaalex.cataclysmar.pojo.QuakeCluster;
import com.google.ar.sceneform.ux.TransformableNode;

public class QuakeClusterPin extends BasePin {

    private float size;

    public QuakeClusterPin(QuakeCluster quakeCluster) {
        super(quakeCluster);
        size = quakeCluster.getSize();

        setPosition(quakeCluster.getLatitude(), quakeCluster.getLongitude());
    }

    @Override
    public void setup(Context context, TransformableNode earth) {
        setTextureAndLocation(context, Color.YELLOW, earth, size * 0.005f);
    }
}
