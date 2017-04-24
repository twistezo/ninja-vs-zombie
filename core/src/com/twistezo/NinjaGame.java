package com.twistezo;

import com.badlogic.gdx.Game;
import com.twistezo.screens.SplashScreen;

/**
 * @author twistezo (23.04.2017)
 */

public class NinjaGame extends Game{
    public final static int SCREEN_WIDTH = 800;
    public final static int SCREEN_HEIGHT = 480;
    private boolean paused;

    @Override
    public void create() {
        this.setScreen(new SplashScreen(this));
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}