package com.twistezo.screens;

import com.twistezo.NinjaGame;
import com.twistezo.playground.PlayerFromAtlas;
import com.twistezo.playground.PlayerFromSheet;

/**
 * @author twistezo (24.04.2017)
 */

public class GameScreen extends AbstractScreen {
    PlayerFromSheet playerFromSheet;
    PlayerFromAtlas playerFromAtlas;

    public GameScreen(NinjaGame game) {
        super(game);
        init();
    }

    @Override
    protected void init() {
        initPlayer();
    }

    private void initPlayer() {
        playerFromSheet = new PlayerFromSheet();
        playerFromSheet.setSize(playerFromSheet.getRegionWidth()/2, playerFromSheet.getRegionHeight()/2);
        playerFromSheet.setPosition(100, 50);
        playerFromSheet.setDebug(true);
        stage.addActor(playerFromSheet);

        playerFromAtlas = new PlayerFromAtlas();
        playerFromAtlas.setSize(playerFromSheet.getRegionWidth()/2, playerFromSheet.getRegionHeight()/2);
        playerFromAtlas.setPosition(300, 50);
        playerFromAtlas.setDebug(true);
        stage.addActor(playerFromAtlas);
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
