package com.twistezo.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.twistezo.NinjaGame;
import com.twistezo.characters.Player;
import com.twistezo.characters.Zombie;
import com.twistezo.characters.ZombieFemale;
import com.twistezo.characters.ZombieMale;
import com.twistezo.utils.FpsCounter;
import com.twistezo.utils.PlayerHealthBar;
import com.twistezo.utils.PlayerScoreCounter;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author twistezo (24.04.2017)
 */

public class GameScreen extends AbstractScreen {
    private final int SPAWN_TIME = 10 * 1000; //ms
    private Player player;
    private ZombieMale zombieMale;
    private ZombieFemale zombieFemale;
    private ArrayList<Zombie> zombies;
    private FpsCounter fpsCounter;
    private PlayerHealthBar playerHealthBar;
    private PlayerScoreCounter playerScoreCounter;
    private Texture background;
    private Image backgroundImg;
    private MyButton buttonDebug;
    private MyButton buttonLeft;
    private MyButton buttonRight;
    private MyButton buttonAttack;
    private Texture buttonDebugTextureUp;
    private Texture buttonDebugTextureDown;
    private Texture buttonLeftTexture;
    private Texture buttonRightTexture;
    private Texture buttonAttackTexture;
    private Random random;
    private long lastZombieTime;
    private float timeSinceCollision = 0;
    private float timeSinceDeath = 0;
    private boolean isCollision = false;
    private boolean isDebugMode = false;

    public GameScreen(NinjaGame game) {
        super(game);
        init();
    }

    @Override
    protected void init() {
        initBackground();
        initFpsCounter();
        initDebugButton();
        initGameButtons();
        initPlayerHealthBar();
        initPlayerScoreCounter();
        initPlayer();
        initButtonsListeners();
        zombies = new ArrayList<>();
        random = new Random();
    }

    private void initBackground() {
        background = new Texture("game-background.png");
        backgroundImg = new Image(background);
        backgroundImg.setPosition(0, 0);
        stage.addActor(backgroundImg);
    }

    private void initFpsCounter() {
        fpsCounter = new FpsCounter();
        fpsCounter.setPosition(5, 15);
        stage.addActor(fpsCounter);
    }

    private void initDebugButton() {
        buttonDebugTextureUp = new Texture("menu/debug_1.png");
        buttonDebugTextureDown = new Texture("menu/debug_2.png");
        buttonDebug = new MyButton(buttonDebugTextureUp, buttonDebugTextureDown);
        buttonDebug.setPosition(NinjaGame.SCREEN_WIDTH - 200, NinjaGame.SCREEN_HEIGHT - 60);
        stage.addActor(buttonDebug);
    }

    private void initGameButtons() {
        final int OFFSET = 25;
        buttonLeftTexture = new Texture("button-left.png");
        buttonRightTexture = new Texture("button-right.png");
        buttonAttackTexture = new Texture("button-action.png");
        buttonLeft = new MyButton(buttonLeftTexture, buttonLeftTexture);
        buttonRight = new MyButton(buttonRightTexture, buttonRightTexture);
        buttonAttack = new MyButton(buttonAttackTexture, buttonAttackTexture);
        buttonLeft.setPosition(NinjaGame.SCREEN_WIDTH - buttonRight.getWidth() - OFFSET - 10 - buttonLeft.getWidth(), OFFSET);
        buttonRight.setPosition(NinjaGame.SCREEN_WIDTH - buttonRight.getWidth() - OFFSET, OFFSET);
        buttonAttack.setPosition(OFFSET, OFFSET);
        stage.addActor(buttonLeft);
        stage.addActor(buttonRight);
        stage.addActor(buttonAttack);
    }

    private void initButtonsListeners() {
        buttonDebug.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isDebugMode = !isDebugMode;
                toggleDebugMode(isDebugMode);
            }
        });
        buttonLeft.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                player.setMovingLeft(true);
                player.doMovement("LEFT");
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                player.setMovingLeft(false);
                player.doMovement("IDLE");
            }
        });
        buttonRight.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                player.setMovingRight(true);
                player.doMovement("RIGHT");
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                player.setMovingRight(false);
                player.doMovement("IDLE");
            }
        });
        buttonAttack.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                player.setAttacking(true);
                player.doMovement("ATTACK");
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    private void initPlayerHealthBar(){
        playerHealthBar = new PlayerHealthBar();
        playerHealthBar.setPosition(5, NinjaGame.SCREEN_HEIGHT-5);
        stage.addActor(playerHealthBar);
    }

    private void initPlayerScoreCounter() {
        playerScoreCounter = new PlayerScoreCounter();
        playerScoreCounter.setPosition(5, NinjaGame.SCREEN_HEIGHT - 25);
        stage.addActor(playerScoreCounter);
    }

    private void initPlayer() {
        player = new Player(300, 50);
        stage.addActor(player);
    }

    private void spawnZombies(){
        int switcher = random.nextInt(2);
        switch(switcher) {
            case 0:
                zombies.add(new ZombieMale(random.nextBoolean()));
                break;
            case 1:
                zombies.add(new ZombieFemale(random.nextBoolean()));
                break;
        }
        lastZombieTime = TimeUtils.millis();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        timeSinceCollision += delta;
        timeSinceDeath += delta;
        update();
        stage.draw();

        if(!player.isDead()) {
            if (TimeUtils.millis() - lastZombieTime > SPAWN_TIME) {
                spawnZombies();
            }
            for(Zombie zombie : zombies) {
                stage.addActor(zombie);
            }
            if(timeSinceCollision > 0.75f) {
                checkCollision();
                timeSinceCollision = 0;
            }
            zombieFollowPlayerX();
            checkZombieDeath();
            if(timeSinceDeath > 2f) {
                removeDeathZombie();
                timeSinceDeath = 0;
            }
        }
        checkPlayerDeath();
        moveActorsToFront();
    }

    private void moveActorsToFront() {
        player.toFront();
        buttonLeft.toFront();
        buttonRight.toFront();
        buttonAttack.toFront();
    }

    private void checkCollision() {
        for(Zombie zombie : zombies) {
            if(player.getBounds().overlaps(zombie.getBounds())){
                zombie.setInEnemyBounds(true);
                player.setInEnemyBounds(true);
                if(player.isAttacking()) {
                    zombie.decreaseHealth();
                } else {
                    playerHealthBar.reducePlayerHealth();
                }
            } else {
                zombie.setInEnemyBounds(false);
                player.setInEnemyBounds(false);
            }
        }
    }

    private void checkZombieDeath() {
        for(int i=0; i<zombies.size(); i++) {
            if(zombies.get(i).getHealth() <= 0) {
                zombies.get(i).setDeath(true);
            }
        }
    }

    private void removeDeathZombie() {
        for(int i=0; i<zombies.size(); i++) {
            if(zombies.get(i).isReadyToRemove() && zombies.get(i).isDeath()){
                playerScoreCounter.addScore();
                zombies.get(i).remove();
                zombies.remove(i);
            }
        }
    }

    private void checkPlayerDeath() {
        if(playerHealthBar.getPlayerHealth() <= 0) {
            player.setDead(true);
            playerHealthBar.setHealth(0);
        }
    }

    private void zombieFollowPlayerX() {
        for(Zombie zombie : zombies) {
            zombie.setWalkTargetX(player.getPlayerCurrentX());
        }
    }

    private void update() {
        stage.act();
    }

    @Override
    public void resize(int width, int height){
        stage.getViewport().update(width,height);
    }

    private void toggleDebugMode(boolean value) {
        player.setDebug(value);
        player.setDebugMode(value);
        for (Zombie zombie : zombies) {
            zombie.setDebug(value);
            zombie.setDebugMode(value);
        }
    }
}
