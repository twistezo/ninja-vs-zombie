package com.twistezo.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.twistezo.GameScreenManager;
import com.twistezo.utils.GameSettings;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Drop";
		config.width = GameSettings.SCREEN_WIDTH;
		config.height = GameSettings.SCREEN_HEIGHT;
        config.title = GameScreenManager.GAME_NAME;
        config.resizable = false;
		new LwjglApplication(new GameScreenManager(), config);
	}
}

