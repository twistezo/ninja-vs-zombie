package com.twistezo.utils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

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
        Random rand = new Random();
        int x = rand.nextInt(6);
        this.playerHealth -= x;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public void setHealth(int health) {
        this.playerHealth = health;
    }

}