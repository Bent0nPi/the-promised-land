package ru.nsu.ccfit.chumak.view.graphics;

import javafx.geometry.Rectangle2D;

public class Animation {
    Rectangle2D[] frames; //frames that are used in animation

    private int ticks = 0; //current count of ticks
    private int delay; //count of ticks required for single animation shift

    private int index = 0; //current index of frame

    private boolean isPlaying; // flag that represents animation playing
    private boolean playOnce; //flag that represents animation without loops

    private int playCount = 0;


    public Animation(Rectangle2D[] frames, boolean playOnce, boolean isPlaying) {
        this.frames = frames;
        this.delay = 6;
        this.playOnce = playOnce;
        this.isPlaying = isPlaying;
    }

    public Animation(Rectangle2D[] frames, int delay, boolean playOnce, boolean isPlaying) {
        this.frames = frames;
        this.delay = delay;
        this.playOnce = playOnce;
        this.isPlaying = isPlaying;
    }

    public Animation restart() {
        ticks = 0;
        index = 0;
        isPlaying = true;
        playCount = 0;
        return this;
    }

    public void concatenate(Rectangle2D[] fragment){
        Rectangle2D[] unitedFrames = new Rectangle2D[frames.length + fragment.length];
        System.arraycopy(frames, 0, unitedFrames, 0, frames.length);
        System.arraycopy(fragment, 0, unitedFrames, frames.length, fragment.length);
        frames = unitedFrames;
    }

    public void setPlayOnce(boolean playOnce) {
        this.playOnce = playOnce;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setFrames(Rectangle2D[] frames) {
        this.frames = frames;
    }

    public int getDelay() {
        return delay;
    }

    public int getPlayCount() {
        return playCount;
    }

    public Rectangle2D getFrame() {
        return frames[this.index];
    }

    public void play() {
        if(!isPlaying) return;

        ticks++;

        if(ticks % delay == 0) {
            ticks = 0;
            index++;
            if (index == frames.length) {
                if (playOnce) {
                    index--;
                    isPlaying = false;
                } else {
                    index = 0;
                }
            }
            playCount++;
        }
    }


}
