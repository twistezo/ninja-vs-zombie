package com.twistezo.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.twistezo.NinjaGame;

/**
 * @author twistezo (23.04.2017)
 */

public class SplashScreen extends AbstractScreen {
    private Texture splashImg;

    public SplashScreen(final NinjaGame game) {
        super(game);
        init();

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.setScreen(new MenuScreen(game));
            }
        }, 1);
    }

    @Override
    protected void init() {
        // TODO implement better assets loading when game grows
        splashImg = new Texture("menu/background.png");
    }


    @Override
    public void render(float delta) {
        super.render(delta);

        spriteBatch.begin();
        spriteBatch.draw(splashImg, 0, 0);
        spriteBatch.end();
    }
}
