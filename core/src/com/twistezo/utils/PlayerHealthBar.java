package com.twistezo.utils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author twistezo (25.04.2017)
 */

public class PlayerHealthBar extends Actor {
    private BitmapFont font;
    private int playerHealth = 100;

    public PlayerHealthBar(){
        font = new BitmapFont();
        font.setColor(0,1,0,1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font.draw(batch, "HEALTH: " + playerHealth + " %", getX(), getY());
    }

    public void reducePlayerHealth(){
        this.playerHealth -= 5;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

}