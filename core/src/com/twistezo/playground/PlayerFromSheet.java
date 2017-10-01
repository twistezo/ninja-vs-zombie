package com.twistezo.playground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerFromSheet extends Actor {
    private static final int FRAME_COLS = 10, FRAME_ROWS = 1;
    private Animation<TextureRegion> walkAnimation;
    private TextureRegion textureRegion;
    private float stateTime;

    public PlayerFromSheet() {
        generateRegionFromSheet();
    }

    private void generateRegionFromSheet() {
        Texture walkSheet = new Texture(Gdx.files.internal("sheets/ninja-idle-sheet.png"));

        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        walkAnimation = new Animation<TextureRegion>(0.025f, walkFrames);
        stateTime = 0f;
        textureRegion = walkAnimation.getKeyFrame(0);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        stateTime += delta;
        textureRegion = walkAnimation.getKeyFrame(stateTime, true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(textureRegion, getX(), getY(), getWidth() / 2, getHeight() / 2, getWidth(), getHeight(), getScaleX(),
                getScaleY(), getRotation());
    }

    public float getRegionHeight() {
        return textureRegion.getRegionHeight();
    }

    public float getRegionWidth() {
        return textureRegion.getRegionWidth();
    }
}
