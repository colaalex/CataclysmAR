package com.github.colaalex.cataclysmar.pins;

import android.content.Context;
import android.graphics.Color;

import com.github.colaalex.cataclysmar.pojo.Quake;
import com.google.ar.sceneform.ux.TransformableNode;

public class QuakePin extends BasePin {

    public QuakePin(Quake quake) {
        super(quake);
        float latitude = quake.getLatitude();
        float longitude = quake.getLongitude();

        setPosition(latitude, longitude);
    }

    @Override
    public void setup(Context context, TransformableNode earth) {
        setTextureAndLocation(context, Color.YELLOW, earth);
    }
}
