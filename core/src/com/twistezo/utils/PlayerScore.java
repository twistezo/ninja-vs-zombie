package com.twistezo.utils;

public class PlayerScore {
    private String name;
    private int score;

    public PlayerScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "PlayerScore{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
