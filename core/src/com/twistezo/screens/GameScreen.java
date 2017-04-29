package com.twistezo.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.twistezo.NinjaGame;
import com.twistezo.characters.Player;
import com.twistezo.characters.ZombieFemale;
import com.twistezo.utils.FpsCounter;
import com.twistezo.utils.PlayerHealthBar;
import com.twistezo.utils.PlayerScoreCounter;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author twistezo (24.04.2017)
 */

public class GameScreen extends AbstractScreen {
    private Player player;
    private ZombieFemale zombieFemale;
    private ArrayList<ZombieFemale> femaleZombies;
    private FpsCounter fpsCounter;
    private PlayerHealthBar playerHealthBar;
    private PlayerScoreCounter playerScoreCounter;
    private Texture background;
    private Image backgroundImg;
    private MyButton debugButton;
    private Texture debugButtonUp;
    private Texture debugButtonDown;
    private long lastZombieTime;
    private int spawnTime = 10000; //ms
    private float timeSinceCollision = 0;
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
        initPlayerHealthBar();
        initPlayerScoreCounter();
        initPlayer();
        femaleZombies = new ArrayList<>();
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
        debugButtonUp = new Texture("menu/debug_1.png");
        debugButtonDown = new Texture("menu/debug_2.png");
        debugButton = new MyButton(debugButtonUp, debugButtonDown);
        debugButton.setPosition(NinjaGame.SCREEN_WIDTH - 200, NinjaGame.SCREEN_HEIGHT - 60);
        stage.addActor(debugButton);
    }

    private void debugButtonListener() {
        debugButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isDebugMode = !isDebugMode;
                toggleDebugMode(isDebugMode);
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
        player = new Player();
        player.setPosition(300, 50);
        player.setSize(player.getWidth(), player.getHeight());
        stage.addActor(player);
    }

    private void spawnFemaleZombie(){
        Random random = new Random();
        femaleZombies.add(new ZombieFemale(random.nextBoolean()));
        lastZombieTime = TimeUtils.millis();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        timeSinceCollision += delta;
        update();
        stage.draw();
        if (TimeUtils.millis() - lastZombieTime > spawnTime) {
            spawnFemaleZombie();
        }
        for(ZombieFemale zombie : femaleZombies) {
            stage.addActor(zombie);
        }
        if(timeSinceCollision > 0.75f) {
            checkCollision();
            timeSinceCollision = 0;
        }
        zombieFollowPlayerX();
        checkZombieDeath();
        debugButtonListener();
    }

    private void checkCollision() {
        for(ZombieFemale zombieFemale : femaleZombies) {
            if(player.getBounds().overlaps(zombieFemale.getBounds())){
                zombieFemale.setInEnemyBounds(true);
                player.setInEnemyBounds(true);
                if(!player.isAttacking()) {
                    playerHealthBar.reducePlayerHealth();
                }
                if(player.isAttacking()) {
                    zombieFemale.decreaseHealth();
                }
            } else if (!player.getBounds().overlaps(zombieFemale.getBounds())){
                zombieFemale.setInEnemyBounds(false);
                player.setInEnemyBounds(false);
            }
        }
    }

    private void checkZombieDeath() {
        for(int i=0; i<femaleZombies.size(); i++) {
            if(femaleZombies.get(i).getHealth() <= 0) {
                femaleZombies.get(i).setDeath(true);
//                femaleZombies.get(i).remove();
                femaleZombies.remove(i);
                playerScoreCounter.addScore();
            }
        }
    }

    private void zombieFollowPlayerX() {
        for(ZombieFemale zombieFemale : femaleZombies) {
            zombieFemale.setWalkTargetX(player.getPlayerCurrentX());
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
        for (ZombieFemale zombieFemale : femaleZombies) {
            zombieFemale.setDebug(value);
            zombieFemale.setDebugMode(value);
        }
    }
}
