package com.github.colaalex.cataclysmar.pins;

import android.content.Context;

import com.github.colaalex.cataclysmar.pojo.FireCluster;
import com.google.ar.sceneform.ux.TransformableNode;

public class FireClusterPin extends BasePin {

    private float size;

    public FireClusterPin(FireCluster fireCluster) {
        super(fireCluster);
        size = fireCluster.getSize();

        setPosition(fireCluster.getLatitude(), fireCluster.getLongitude());
    }

    @Override
    public void setup(Context context, TransformableNode earth) {
        setModelTextureAndLocation(context, earth, size*0.005f);
    }
}
