package com.github.colaalex.cataclysmar.pins;

import android.content.Context;
import android.graphics.Color;

import com.github.colaalex.cataclysmar.pojo.Wildfire;
import com.google.ar.sceneform.ux.TransformableNode;

public class FirePin extends BasePin{

    public FirePin(Wildfire wildfire) {
        super(wildfire);
        float latitude = wildfire.getLatitude();
        float longitude = wildfire.getLongitude();

        setPosition(latitude, longitude);
    }

    @Override
    public void setup(Context context, TransformableNode earth) {
        //setTextureAndLocation(context, Color.RED, earth, 0.001f);
        setModelTextureAndLocation(context, earth, 0.001f);
    }
}
