package com.twistezo.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;
import com.twistezo.NinjaGame;
import com.twistezo.characters.Player;
import com.twistezo.characters.ZombieFemale;
import com.twistezo.utils.FpsCounter;
import com.twistezo.utils.PlayerHealthBar;

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
    private Texture background;
    private Image backgroundImg;
    private long lastZombieTime;
    private int spawnTime = 5000; //ms
    private boolean isCollision = false;

    public GameScreen(NinjaGame game) {
        super(game);
        init();
    }

    @Override
    protected void init() {
        initBackground();
        initFpsCounter();
        initPlayerHealthBar();
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

    private void initPlayerHealthBar(){
        playerHealthBar = new PlayerHealthBar();
        playerHealthBar.setPosition(5, NinjaGame.SCREEN_HEIGHT-5);
        stage.addActor(playerHealthBar);
    }

    private void initPlayer() {
        player = new Player();
        player.setPosition(300, 50);
        player.setSize(player.getWidth(), player.getHeight());
        player.setDebug(true);
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
        update();
        stage.draw();
        if (TimeUtils.millis() - lastZombieTime > spawnTime) {
            spawnFemaleZombie();
        }
        for(ZombieFemale zombie : femaleZombies) {
            zombie.setDebug(true);
            stage.addActor(zombie);
        }
    }

    private void checkCollision() {
        for(ZombieFemale zombieFemale : femaleZombies) {
            if(player.getBounds().overlaps(zombieFemale.getBounds())){
                zombieFemale.setInEnemyBounds(true);
                player.setInEnemyBounds(true);
                playerHealthBar.reducePlayerHealth();
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
        for(ZombieFemale zombieFemale : femaleZombies) {
            if(zombieFemale.getHealth() <= 0){
                zombieFemale.remove();
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
        checkCollision();
        zombieFollowPlayerX();
        checkZombieDeath();
    }

    @Override
    public void resize(int width, int height){
        stage.getViewport().update(width,height);
    }
}
