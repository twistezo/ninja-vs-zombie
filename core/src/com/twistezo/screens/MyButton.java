package com.twistezo.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class MyButton extends ImageButton {
    public MyButton(Texture texture_up, Texture texture_down) {
        super(new SpriteDrawable(new Sprite(texture_up)), new SpriteDrawable(new Sprite(texture_down)));
    }
}
