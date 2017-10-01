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
import com.twistezo.GameScreenManager;

public abstract class Zombie extends Actor {
    private final float MOVEMENT_DURATION = 60f;
    protected final float FRAME_DURATION = 1 / 10f;
    private final float ZOMBIE_SCALE = 1 / 4f;
    private final int BOUNDS_SHIFT = 20;
    private final int HEALTH_TO_DECREASE = 25;
    private SpriteBatch spriteBatch;
    protected TextureAtlas textureAtlasMove;
    protected TextureAtlas textureAtlasAttack;
    protected TextureAtlas textureAtlasDead;
    protected Animation<TextureRegion> animationMove;
    protected Animation<TextureRegion> animationAttack;
    protected Animation<TextureRegion> animationDead;
    private TextureRegion textureRegion;
    private Rectangle bounds;
    private ShapeRenderer shapeRenderer;
    private float stateTime = 0;
    private float stateDeadTime = 0;
    private float walkTargetX = 0;
    private int health = 100;
    private boolean isZombieFlippedToLeft = false;
    private boolean isInEnemyBounds = false;
    private boolean isZombieGoRight = false;
    private boolean isDebugMode = false;
    private boolean isDeath = false;
    private boolean isReadyToRemove = false;

    protected Zombie(boolean isZombieGoRight) {
        this.isZombieGoRight = isZombieGoRight;
        this.setSize(this.getWidth(), this.getHeight());
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        initZombieStartPosition();
        generateAnimations();
    }

    abstract void generateAnimations();

    private void initZombieStartPosition() {
        this.setY(50);
        if (isZombieGoRight) {
            this.setX(-100);
        } else {
            this.setX(GameScreenManager.SCREEN_WIDTH);
        }
    }

    private enum Movement {
        WALK, ATTACK, DEAD
    }

    private void doMovement(String movement) {
        switch (Movement.valueOf(movement)) {
        case WALK:
            textureRegion = animationMove.getKeyFrame(stateTime, true);
            if (isZombieGoRight) {
                if (this.getX() < walkTargetX) {
                    isZombieFlippedToLeft = false;
                    this.addAction(Actions.moveTo(walkTargetX, getY(), MOVEMENT_DURATION));
                } else if (this.getX() > walkTargetX) {
                    isZombieFlippedToLeft = true;
                    this.addAction(Actions.moveTo(walkTargetX, getY(), MOVEMENT_DURATION));
                }
            } else {
                if (this.getX() > walkTargetX) {
                    isZombieFlippedToLeft = true;
                    this.addAction(Actions.moveTo(walkTargetX, getY(), MOVEMENT_DURATION));
                } else if (this.getX() < walkTargetX) {
                    isZombieFlippedToLeft = false;
                    this.addAction(Actions.moveTo(walkTargetX, getY(), MOVEMENT_DURATION));
                    isZombieGoRight = true;
                }
            }
            setZombieWidthAndHeight();
            break;
        case ATTACK:
            clearActions();
            if (isZombieGoRight) {
                textureRegion = animationAttack.getKeyFrame(stateTime, true);
            } else {
                isZombieFlippedToLeft = true;
                textureRegion = animationAttack.getKeyFrame(stateTime, true);
            }
            setZombieWidthAndHeight();
            break;
        case DEAD:
            if (!animationDead.isAnimationFinished(stateDeadTime)) {
                textureRegion = animationDead.getKeyFrame(stateDeadTime);
            } else {
                isReadyToRemove = true;
            }
            setZombieWidthAndHeight();
            break;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;

        if (isDeath) {
            stateDeadTime += delta;
            doMovement("DEAD");
        } else {
            if (isInEnemyBounds) {
                doMovement("ATTACK");
            } else {
                doMovement("WALK");
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (isZombieFlippedToLeft) {
            batch.draw(textureRegion, getX(), getY(), getWidth() / 2, getHeight() / 2, getWidth(), getHeight(),
                    getScaleX() * -1, getScaleY(), getRotation());
        } else {
            batch.draw(textureRegion, getX(), getY(), getWidth() / 2, getHeight() / 2, getWidth(), getHeight(),
                    getScaleX(), getScaleY(), getRotation());
        }
        if (isDebugMode) {
            batch.end();
            drawDebugBounds();
            batch.begin();
        }
    }

    /* Set correct width and height for different sizes of textureRegions */
    private void setZombieWidthAndHeight() {
        setWidth(textureRegion.getRegionWidth() * ZOMBIE_SCALE);
        setHeight(textureRegion.getRegionHeight() * ZOMBIE_SCALE);
    }

    public Rectangle getBounds() {
        bounds = new Rectangle((int) getX() + BOUNDS_SHIFT, (int) getY(), (int) getWidth() - 2 * BOUNDS_SHIFT,
                (int) getHeight());
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

    public void decreaseHealth() {
        this.health -= HEALTH_TO_DECREASE;
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
    }

    public void setInEnemyBounds(boolean inEnemyBounds) {
        isInEnemyBounds = inEnemyBounds;
    }

    public void setWalkTargetX(float x) {
        this.walkTargetX = x;
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
