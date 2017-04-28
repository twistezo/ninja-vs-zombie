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
    private final float MOVEMENT_DURATION = 10f;
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
    private float walkTargetX = 0;
    private int health = 1000;
    private boolean isPlayerFlippedToLeft = false;
    private boolean isInEnemyBounds = false;
    private boolean isMovingToRight = false;

    public ZombieFemale(boolean isMovingToRight) {
        this.setY(50);
        if(isMovingToRight) {
            this.setX(-200);
        } else if (!isMovingToRight) {
            this.setX(NinjaGame.SCREEN_WIDTH+200);
        }
        this.setSize(this.getWidth(), this.getHeight());
        this.isMovingToRight = isMovingToRight;
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
        if(!isInEnemyBounds){
            walk(isMovingToRight);
        }

        /* Attack when zombie is in player bounds */
        if(isInEnemyBounds) {
            clearActions();
            if (isMovingToRight) {
                textureRegion = animationAttack.getKeyFrame(stateTime,true);
            } else if (!isMovingToRight) {
                isPlayerFlippedToLeft = true;
                textureRegion = animationAttack.getKeyFrame(stateTime,true);
            }
            setZombieWidthAndHeight();
        }
    }

    /* Follow player */
    private void walk(boolean moveToRight) {
        /* Walk to RIGHT */
        if (moveToRight) {
            if(this.getX() < walkTargetX) {
                isPlayerFlippedToLeft = false;
                this.addAction(Actions.moveTo(walkTargetX, getY(), MOVEMENT_DURATION));
            } else if (this.getX() > walkTargetX){
                isPlayerFlippedToLeft = true;
                this.addAction(Actions.moveTo(walkTargetX, getY(), MOVEMENT_DURATION));
            }
        /* Walk to LEFT */
        } else if (!moveToRight) {
            if(this.getX() > walkTargetX) {
                isPlayerFlippedToLeft = true;
                this.addAction(Actions.moveTo(walkTargetX, getY(), MOVEMENT_DURATION));
            } else if (this.getX() < walkTargetX){
                isPlayerFlippedToLeft = false;
                this.addAction(Actions.moveTo(walkTargetX, getY(), MOVEMENT_DURATION));
                isMovingToRight = true;
            }
        }
    }

    public void setDirection(boolean moveToRight) {
        this.isMovingToRight = moveToRight;
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
        bounds = new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        return bounds;
    }

    public boolean isInEnemyBounds() {
        return isInEnemyBounds;
    }

    public void setInEnemyBounds(boolean inEnemyBounds) {
        isInEnemyBounds = inEnemyBounds;
    }

    public void setWalkTargetX(float x) {
        this.walkTargetX = x;
    }

    public void decreaseHealth() {
        this.health -= 25;
    }

    public int getHealth() {
        return this.health;
    }
}
