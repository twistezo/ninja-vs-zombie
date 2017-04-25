package com.twistezo.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.twistezo.NinjaGame;

/**
 * @author twistezo (23.04.2017)
 */

public class Player extends Actor {
    private final String NINJA_IDLE = "ninja-idle.atlas";
    private final String NINJA_RUN_LEFT = "ninja-run-left.atlas";
    private final String NINJA_RUN_RIGHT = "ninja-run-right.atlas";
    private final float MOVEMENT_DURATION = 1/10f;
    private final float FRAME_DURATION = 1/10f;
    private final int MOVEMENT_STEP = 20;
    private final float PLAYER_SCALE = 0.5f;
    private Movement movement;
    private SpriteBatch spriteBatch;
    private TextureAtlas textureAtlas;
    private Animation<TextureRegion> animation;
    private TextureRegion textureRegion;
    private float stateTime = 0;

    public Player() {
        spriteBatch = new SpriteBatch();
        generateAnimation("IDLE");
    }

    private enum Movement {
        IDLE,
        LEFT,
        RIGHT,
        UP
    }

    private void generateAnimation(String atlasName) {
        switch(Movement.valueOf(atlasName)) {
            case IDLE:
                textureAtlas = new TextureAtlas(Gdx.files.internal(NINJA_IDLE));
                animation = new Animation<TextureRegion>(FRAME_DURATION, textureAtlas.getRegions());
                break;
            case LEFT:
                textureAtlas = new TextureAtlas(Gdx.files.internal(NINJA_RUN_LEFT));
                animation = new Animation<TextureRegion>(FRAME_DURATION, textureAtlas.getRegions());
                break;
            case RIGHT:
                textureAtlas = new TextureAtlas(Gdx.files.internal(NINJA_RUN_RIGHT));
                animation = new Animation<TextureRegion>(FRAME_DURATION, textureAtlas.getRegions());
                break;
            case UP:
                // TODO up movement
                break;
            default:
                textureAtlas = new TextureAtlas(Gdx.files.internal(NINJA_IDLE));
                animation = new Animation<TextureRegion>(FRAME_DURATION, textureAtlas.getRegions());
                break;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        /* Make animation depends on frames */
        stateTime += delta;
        textureRegion = animation.getKeyFrame(stateTime,true);
        setPlayerWidthAndHeight();

        /* Keyboard events */
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            moveLeft();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveRight();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            idle();
        }

        /* Mouse/Touch events */
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            generateAnimation("RIGHT");
            this.addAction(Actions.moveTo(touchPos.x - this.getWidth()/2, getY(), MOVEMENT_DURATION));
        }

        /* Hold player in screen bounds */
        if (this.getX() < 0 ) {
            this.setPosition(0, 50);
        }
        if (this.getX() > NinjaGame.SCREEN_WIDTH - this.getWidth()) {
            this.setPosition(NinjaGame.SCREEN_WIDTH - this.getWidth(), getY());
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(textureRegion,
                getX(),getY(),
                getWidth()/2,getHeight()/2,
                getWidth(), getHeight(),
                getScaleX(),getScaleY(),
                getRotation());
    }

    private void setPlayerWidthAndHeight() {
        setWidth(textureRegion.getRegionWidth() * PLAYER_SCALE);
        setHeight(textureRegion.getRegionHeight() * PLAYER_SCALE);
    }

    private void moveLeft() {
        setPlayerWidthAndHeight();
        generateAnimation("LEFT");
//            this.setPosition(getX()-MOVEMENT_STEP, getY());
        this.addAction(Actions.moveTo(getX()- MOVEMENT_STEP, getY(), MOVEMENT_DURATION));
    }

    private void moveRight() {
        setPlayerWidthAndHeight();
        generateAnimation("RIGHT");
        this.setPosition(getX()+MOVEMENT_STEP, getY());
//            this.addAction(Actions.moveTo(getX()+ MOVEMENT_STEP, getY(), MOVEMENT_DURATION));
    }

    private void idle() {
        setPlayerWidthAndHeight();
        generateAnimation("IDLE");
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
    }
}

