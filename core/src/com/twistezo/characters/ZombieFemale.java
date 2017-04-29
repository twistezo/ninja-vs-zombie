package com.twistezo.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    private final String ZOMBIE_FEMALE_DEAD_FILE = "zombie-female-dead.atlas";
    private final float MOVEMENT_DURATION = 10f;
    private final float FRAME_DURATION = 1/10f;
    private final float ZOMBIE_SCALE = 1/4f;
    private final int BOUNDS_SHIFT = 20;
    private SpriteBatch spriteBatch;
    private TextureAtlas textureAtlasMove;
    private TextureAtlas textureAtlasAttack;
    private TextureAtlas textureAtlasDead;
    private Animation<TextureRegion> animationMove;
    private Animation<TextureRegion> animationAttack;
    private Animation<TextureRegion> animationDead;
    private TextureRegion textureRegion;
    private Rectangle bounds;
    private ShapeRenderer shapeRenderer;
    private float stateTime = 0;
    private float stateDeadTime = 0;
    private float walkTargetX = 0;
    private int health = 100;
    private boolean isPlayerFlippedToLeft = false;
    private boolean isInEnemyBounds = false;
    private boolean isMovingToRight = false;
    private boolean isDebugMode = false;
    private boolean isDeath = false;
    private boolean isReadyToRemove = false;

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
        shapeRenderer = new ShapeRenderer();
    }

    private void generateAnimations() {
        textureAtlasMove = new TextureAtlas(Gdx.files.internal(ZOMBIE_FEMALE_MOVE_FILE));
        animationMove = new Animation<TextureRegion>(FRAME_DURATION, textureAtlasMove.getRegions());
        textureAtlasAttack = new TextureAtlas(Gdx.files.internal(ZOMBIE_FEMALE_ATTACK_FILE));
        animationAttack = new Animation<TextureRegion>(FRAME_DURATION, textureAtlasAttack.getRegions());
        textureAtlasDead = new TextureAtlas(Gdx.files.internal(ZOMBIE_FEMALE_DEAD_FILE));
        animationDead = new Animation<TextureRegion>(FRAME_DURATION, textureAtlasDead.getRegions());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;

        if(!isDeath) {
            /* zombie walk animation */
            textureRegion = animationMove.getKeyFrame(stateTime, true);
        /* Set correct width and height for different sizes of textureRegions */
            setZombieWidthAndHeight();

        /* Zombie walk */
            if(!isInEnemyBounds){
                walk(isMovingToRight);
            }

        /* Attack when player is in zombie bounds */
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
        } else if (isDeath){
            stateDeadTime += delta;
            if(!animationDead.isAnimationFinished(stateDeadTime)){
                textureRegion = animationDead.getKeyFrame(stateDeadTime);
            } else {
                isReadyToRemove = true;
            }
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
        if(isDebugMode) {
            batch.end();
            drawDebugBounds();
            batch.begin();
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
        bounds = new Rectangle((int)getX() + BOUNDS_SHIFT, (int)getY(), (int)getWidth() - 2 * BOUNDS_SHIFT, (int)getHeight());
        return bounds;
    }

    private void drawDebugBounds() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(1, 0, 0, 0.5f)); // last argument is alpha channel
        shapeRenderer.rect(getX() + BOUNDS_SHIFT, getY(), getWidth() - 2 * BOUNDS_SHIFT, getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
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

    public void setDebugMode(boolean debugMode) {
        this.isDebugMode = debugMode;
    }

    public void setDeath(boolean death) {
        isDeath = death;
    }

    public boolean isDeath() {
        return isDeath;
    }

    public boolean isReadyToRemove() {
        return isReadyToRemove;
    }
}
