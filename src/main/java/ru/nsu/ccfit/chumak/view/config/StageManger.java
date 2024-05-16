package ru.nsu.ccfit.chumak.view.config;

public class StageManger {
    private int stageHeight;
    private int stageWidth;

    public StageManger() {
        this(1960,1040);
    }

    public StageManger(int stageWidth, int stageHeight) {
        this.stageWidth = stageWidth;
        this.stageHeight = stageHeight;
    }

    public int getStageHeight() {
        return stageHeight;
    }
    public void setStageHeight(int stageHeight) {
        this.stageHeight = stageHeight;
    }

    public int getStageWidth() {
        return stageWidth;
    }
    public void setStageWidth(int stageWidth) {
        this.stageWidth = stageWidth;
    }
}
