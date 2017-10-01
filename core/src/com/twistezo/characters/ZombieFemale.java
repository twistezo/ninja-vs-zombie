package com.twistezo.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ZombieFemale extends Zombie {
    private final String ZOMBIE_MOVE_FILE = "zombie-female-run-right.atlas";
    private final String ZOMBIE_ATTACK_FILE = "zombie-female-attack.atlas";
    private final String ZOMBIE_DEAD_FILE = "zombie-female-dead.atlas";

    public ZombieFemale(boolean isMovingToRight) {
        super(isMovingToRight);
    }

    @Override
    void generateAnimations() {
        textureAtlasMove = new TextureAtlas(Gdx.files.internal(ZOMBIE_MOVE_FILE));
        animationMove = new Animation<TextureRegion>(FRAME_DURATION, textureAtlasMove.getRegions());
        textureAtlasAttack = new TextureAtlas(Gdx.files.internal(ZOMBIE_ATTACK_FILE));
        animationAttack = new Animation<TextureRegion>(FRAME_DURATION, textureAtlasAttack.getRegions());
        textureAtlasDead = new TextureAtlas(Gdx.files.internal(ZOMBIE_DEAD_FILE));
        animationDead = new Animation<TextureRegion>(FRAME_DURATION, textureAtlasDead.getRegions());
    }
}
