package com.twistezo.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.twistezo.GameScreenManager;

public class Player extends Actor {
    private final String NINJA_IDLE_FILE = "ninja-idle.atlas";
    private final String NINJA_MOVE_FILE = "ninja-run-right.atlas";
    private final String NINJA_ATTACK_FILE = "ninja-attack.atlas";
    private final String NINJA_DEAD_FILE = "ninja-dead.atlas";
    private final float MOVEMENT_DURATION = 1 / 10f;
    private final float FRAME_DURATION = 1 / 10f;
    private final int MOVEMENT_STEP = 10;
    private final float PLAYER_SCALE = 1 / 3f;
    private final int BOUNDS_SHIFT_IDLE = 10;
    private final int BOUNDS_SHIFT_ATTACK = 40;
    private SpriteBatch spriteBatch;
    private TextureAtlas textureAtlasIdle;
    private TextureAtlas textureAtlasMove;
    private TextureAtlas textureAtlasAttack;
    private TextureAtlas textureAtlasDead;
    private Animation<TextureRegion> animationIdle;
    private Animation<TextureRegion> animationMove;
    private Animation<TextureRegion> animationAttack;
    private Animation<TextureRegion> animationDead;
    private TextureRegion textureRegion;
    private Rectangle bounds;
    private ShapeRenderer shapeRenderer;
    private float stateTime = 0;
    private float stateDeadTime = 0;
    private float stateAttackTime = 0;
    private boolean isPlayerFlippedToLeft = false;
    private boolean isInEnemyBounds = false;
    private boolean isPlayerGoRight = false;
    private boolean isAttacking = false;
    private boolean isDebugMode = false;
    private boolean isDead = false;
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;
    private boolean isTest = false;

    public Player(float positionX, float positionY) {
        this.setPosition(positionX, positionY);
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        generateAnimations();
    }

    private void generateAnimations() {
        textureAtlasIdle = new TextureAtlas(Gdx.files.internal(NINJA_IDLE_FILE));
        animationIdle = new Animation<TextureRegion>(FRAME_DURATION, textureAtlasIdle.getRegions());
        textureAtlasMove = new TextureAtlas(Gdx.files.internal(NINJA_MOVE_FILE));
        animationMove = new Animation<TextureRegion>(FRAME_DURATION, textureAtlasMove.getRegions());
        textureAtlasAttack = new TextureAtlas(Gdx.files.internal(NINJA_ATTACK_FILE));
        animationAttack = new Animation<TextureRegion>(FRAME_DURATION, textureAtlasAttack.getRegions());
        textureAtlasDead = new TextureAtlas(Gdx.files.internal(NINJA_DEAD_FILE));
        animationDead = new Animation<TextureRegion>(FRAME_DURATION, textureAtlasDead.getRegions());
    }

    private enum Movement {
        IDLE, LEFT, RIGHT, ATTACK, DEAD
    }

    public void doMovement(String movement) {
        switch (Movement.valueOf(movement)) {
        case IDLE:
            textureRegion = animationIdle.getKeyFrame(stateTime, true);
            setPlayerWidthAndHeight();
            break;
        case LEFT:
            textureRegion = animationMove.getKeyFrame(stateTime, true);
            isPlayerFlippedToLeft = true;
            setPlayerWidthAndHeight();
            this.addAction(Actions.moveTo(getX() - MOVEMENT_STEP, getY(), MOVEMENT_DURATION));
            break;
        case RIGHT:
            textureRegion = animationMove.getKeyFrame(stateTime, true);
            isPlayerFlippedToLeft = false;
            setPlayerWidthAndHeight();
            isPlayerGoRight = true;
            this.addAction(Actions.moveTo(getX() + MOVEMENT_STEP, getY(), MOVEMENT_DURATION));
            break;
        case ATTACK:
            if (isPlayerGoRight) {
                textureRegion = animationAttack.getKeyFrame(stateAttackTime);
            } else {
                isPlayerFlippedToLeft = true;
                textureRegion = animationAttack.getKeyFrame(stateAttackTime);
            }
            if (animationAttack.isAnimationFinished(stateAttackTime)) {
                isAttacking = false;
                stateAttackTime = 0;
            }
            setPlayerWidthAndHeight();
            break;
        case DEAD:
            if (!animationDead.isAnimationFinished(stateDeadTime)) {
                textureRegion = animationDead.getKeyFrame(stateDeadTime);
                setPlayerWidthAndHeight();
            }
            break;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;

        if (isDead) {
            stateDeadTime += delta;
            doMovement("DEAD");
        }
        if (!isDead) {
            if (!isAttacking) {
                doMovement("IDLE");
                if (isMovingLeft || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    doMovement("LEFT");
                }
                if (isMovingRight || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    doMovement("RIGHT");
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                isAttacking = true;
            }
            if (isAttacking) {
                stateAttackTime += delta;
                doMovement("ATTACK");
            }
        }
        if (isDebugMode) {
            changePlayerPositionByTouch();
        }
        holdPlayerInScreenBounds();
        getPlayerCurrentX();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (isPlayerFlippedToLeft) {
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

    private void changePlayerPositionByTouch() {
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            this.addAction(Actions.moveTo(touchPos.x - this.getWidth() / 2, getY(), MOVEMENT_DURATION));
        }
    }

    private void holdPlayerInScreenBounds() {
        if (this.getX() < 0) {
            this.setPosition(0, 50);
        }
        if (this.getX() > GameScreenManager.SCREEN_WIDTH - this.getWidth()) {
            this.setPosition(GameScreenManager.SCREEN_WIDTH - this.getWidth(), getY());
        }
    }

    /* Set correct width and height for different sizes of textureRegions */
    private void setPlayerWidthAndHeight() {
        setWidth(textureRegion.getRegionWidth() * PLAYER_SCALE);
        setHeight(textureRegion.getRegionHeight() * PLAYER_SCALE);
    }

    public Rectangle getBounds() {
        if (isAttacking()) {
            bounds = new Rectangle((int) getX() + BOUNDS_SHIFT_ATTACK, (int) getY(),
                    (int) getWidth() - 2 * BOUNDS_SHIFT_ATTACK, (int) getHeight());
        } else {
            bounds = new Rectangle((int) getX() + BOUNDS_SHIFT_IDLE, (int) getY(),
                    (int) getWidth() - 2 * BOUNDS_SHIFT_IDLE, (int) getHeight());
        }
        return bounds;
    }

    private void drawDebugBounds() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(1, 0, 0, 0.5f)); // last argument is alpha channel
        if (isAttacking()) {
            shapeRenderer.rect((int) getX() + BOUNDS_SHIFT_ATTACK, (int) getY(),
                    (int) getWidth() - 2 * BOUNDS_SHIFT_ATTACK, (int) getHeight());
        } else {
            shapeRenderer.rect((int) getX() + BOUNDS_SHIFT_IDLE, (int) getY(), (int) getWidth() - 2 * BOUNDS_SHIFT_IDLE,
                    (int) getHeight());
        }
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
    }

    public float getPlayerCurrentX() {
        return this.getX();
    }

    public void setInEnemyBounds(boolean inEnemyBounds) {
        isInEnemyBounds = inEnemyBounds;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setDebugMode(boolean debugMode) {
        this.isDebugMode = debugMode;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setMovingLeft(boolean movingLeft) {
        isMovingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        isMovingRight = movingRight;
    }
}
