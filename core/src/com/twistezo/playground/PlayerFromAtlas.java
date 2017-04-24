package com.twistezo.playground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author twistezo (23.04.2017)
 */

public class PlayerFromAtlas extends Actor {
    private SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private Animation<TextureRegion> animation;
    private TextureRegion textureRegion;
    private float stateTime;

    public PlayerFromAtlas(){
        generateAnimationFromAtlas();
    }

    private void generateAnimationFromAtlas() {
        batch = new SpriteBatch();
        textureAtlas = new TextureAtlas(Gdx.files.internal("ninja-idle/ninja-idle.atlas"));
        animation = new Animation<TextureRegion>(1/10f, textureAtlas.getRegions());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        textureRegion = animation.getKeyFrame(stateTime,true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.draw(textureRegion,
                getX(),getY(),
                getWidth()/2,getHeight()/2,
                getWidth(),getHeight(),
                getScaleX(),getScaleY(),
                getRotation());
    }

}

