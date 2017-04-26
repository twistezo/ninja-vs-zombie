package com.twistezo.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.twistezo.NinjaGame;

/**
 * @author twistezo (25.04.2017)
 */

public class ZombieFemale extends Actor {
    private final String ZOMBIE_FEMALE_MOVE_FILE = "zombie-female-run-right.atlas";
    private final String ZOMBIE_FEMALE_ATTACK_FILE = "zombie-female-attack.atlas";
    private final float MOVEMENT_DURATION = 60f;
    private final float FRAME_DURATION = 1/10f;
    private final float ZOMBIE_SCALE = 1/4f;
    private SpriteBatch spriteBatch;
    private TextureAtlas textureAtlasMove;
    private TextureAtlas textureAtlasAttack;
    private Animation<TextureRegion> animationMove;
    private Animation<TextureRegion> animationAttack;
    private TextureRegion textureRegion;
    private Rectangle bounds;
    private float stateTime = 0;
    private boolean isPlayerFlippedToLeft = false;
    private boolean isInPlayerBounds = false;
    private boolean moveToRight = false;

    public ZombieFemale(boolean moveToRight) {
        this.setY(50);
        if(moveToRight) {
//            this.setX(-521*ZOMBIE_SCALE);
            this.setX(0);
        } else if (!moveToRight) {
            this.setX(NinjaGame.SCREEN_WIDTH-100);
        }
        this.setSize(this.getWidth(), this.getHeight());
        this.moveToRight = moveToRight;
        spriteBatch = new SpriteBatch();
        generateAnimations();
    }

    private void generateAnimations() {
        textureAtlasMove = new TextureAtlas(Gdx.files.internal(ZOMBIE_FEMALE_MOVE_FILE));
        animationMove = new Animation<TextureRegion>(FRAME_DURATION, textureAtlasMove.getRegions());
        textureAtlasAttack = new TextureAtlas(Gdx.files.internal(ZOMBIE_FEMALE_ATTACK_FILE));
        animationAttack = new Animation<TextureRegion>(FRAME_DURATION, textureAtlasAttack.getRegions());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        /* Make animationMove depends on frames */
        stateTime += delta;
        textureRegion = animationMove.getKeyFrame(stateTime, true);

        /* Set correct width and height for different sizes of textureRegions */
        setZombieWidthAndHeight();

        /* Zombie walk */
        if(!isInPlayerBounds){
            walk(moveToRight);
        }

        /* Attack when zombie is in player bounds */
        if(isInPlayerBounds) {
            clearActions();
            if (moveToRight) {
                textureRegion = animationAttack.getKeyFrame(stateTime,true);
            } else if (!moveToRight) {
                isPlayerFlippedToLeft = true;
                textureRegion = animationAttack.getKeyFrame(stateTime,true);
            }
            setZombieWidthAndHeight();
        }
    }

    private void walk(boolean moveToRight) {
        if (moveToRight) {
            this.addAction(Actions.moveTo(NinjaGame.SCREEN_WIDTH-this.getWidth()/2, getY(), MOVEMENT_DURATION));
        } else if (!moveToRight) {
            isPlayerFlippedToLeft = true;
            this.addAction(Actions.moveTo(-this.getWidth()/2, getY(), MOVEMENT_DURATION));
        }
    }

    public void setDirection(boolean moveToRight) {
        this.moveToRight = moveToRight;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (isPlayerFlippedToLeft) {
            batch.draw(textureRegion,
                    getX(),getY(),
                    getWidth()/2,getHeight()/2,
                    getWidth(), getHeight(),
                    getScaleX()*-1,getScaleY(),
                    getRotation());
        } else {
            batch.draw(textureRegion,
                    getX(),getY(),
                    getWidth()/2,getHeight()/2,
                    getWidth(), getHeight(),
                    getScaleX(),getScaleY(),
                    getRotation());
        }
    }

    private void setZombieWidthAndHeight() {
        setWidth(textureRegion.getRegionWidth() * ZOMBIE_SCALE);
        setHeight(textureRegion.getRegionHeight() * ZOMBIE_SCALE);
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
    }

    public Rectangle getBounds() {
        bounds = new Rectangle((int)getX()+50, (int)getY(), (int)getWidth()-100, (int)getHeight());
        return bounds;
    }

    public boolean isInPlayerBounds() {
        return isInPlayerBounds;
    }

    public void setInPlayerBounds(boolean inPlayerBounds) {
        isInPlayerBounds = inPlayerBounds;
    }
}
