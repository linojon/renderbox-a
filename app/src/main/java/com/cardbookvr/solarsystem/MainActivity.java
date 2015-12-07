package com.cardbookvr.solarsystem;

import android.os.Bundle;

import com.cardbookvr.renderbox.IRenderBox;
import com.cardbookvr.renderbox.RenderBox;
import com.cardbookvr.renderbox.Transform;
import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;

public class MainActivity extends CardboardActivity implements IRenderBox {
    private static final String TAG = "SolarSystem";
    public static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRestoreGLStateEnabled(false);
        new RenderBox(this, this);
        cardboardView.setRenderer(RenderBox.instance);
        setCardboardView(cardboardView);
    }

    @Override
    public void setup() {
        Transform cube = new Transform();
        cube.setLocalPosition(1.0f, 1.0f, -10.0f);
    }

    @Override
    public void preDraw() {

    }

    @Override
    public void postDraw() {

    }
}
