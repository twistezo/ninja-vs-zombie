package com.twistezo.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class FpsCounter extends Actor {
    BitmapFont font;

    public FpsCounter(){
        font = new BitmapFont();
        font.setColor(0,1,0,1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font.draw(batch, Gdx.graphics.getFramesPerSecond() + " FPS", getX(), getY());
    }
}