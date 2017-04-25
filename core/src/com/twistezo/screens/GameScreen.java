package com.twistezo.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.twistezo.NinjaGame;
import com.twistezo.utils.FpsCounter;
import com.twistezo.characters.Player;

/**
 * @author twistezo (24.04.2017)
 */

public class GameScreen extends AbstractScreen {
    private Player player;
    private FpsCounter fpsCounter;
    private Texture background;
    private Image backgroundImg;

    public GameScreen(NinjaGame game) {
        super(game);
        init();
    }

    @Override
    protected void init() {
        initBackground();
        initFpsCounter();
        initPlayer();
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

    private void initPlayer() {
        player = new Player();
        player.setPosition(50, 50);
        player.setSize(player.getWidth(), player.getHeight());
        player.setDebug(true);
        stage.addActor(player);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update();
        stage.draw();
    }

    private void update() {
        stage.act();
    }

    @Override
    public void resize(int width, int height){
        stage.getViewport().update(width,height);
    }
}
