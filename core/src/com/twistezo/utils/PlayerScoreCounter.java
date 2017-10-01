package com.twistezo.utils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerScoreCounter extends Actor {
    private BitmapFont font;
    private int score = 0;

    public PlayerScoreCounter(){
        font = new BitmapFont();
        font.setColor(1,0,0,1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font.draw(batch, "KILLED ZOMBIES: " + score, getX(), getY());
    }

    public void addScore(){
        this.score += 1;
    }

    public int getScore() {
        return score;
    }

}