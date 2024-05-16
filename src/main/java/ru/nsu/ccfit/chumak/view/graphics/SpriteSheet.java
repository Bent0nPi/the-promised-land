package ru.nsu.ccfit.chumak.view.graphics;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.Objects;

public class SpriteSheet {
    private final Image image;

    private final int rows;
    private final int cols;
    private final int spriteWidth;
    private final int spriteHeight;


    public SpriteSheet(String path, int spriteHeight, int spriteWidth) {
        image = new Image(Objects.requireNonNull(getClass().getResource(path)).toExternalForm());
        this.spriteHeight = spriteHeight;
        this.spriteWidth = spriteWidth;

        this.cols = (int)image.getWidth() / spriteWidth;
        this.rows = (int)image.getHeight() / spriteHeight;
    }

    public SpriteSheet(String path) {
        image = new Image(Objects.requireNonNull(getClass().getResource(path)).toExternalForm());
        this.spriteWidth = (int)image.getWidth();
        this.spriteHeight = (int)image.getHeight();
        this.rows = 1;
        this.cols = 1;
    }

    public Image getImage() { return image; }

    public int getWidth() { return spriteWidth; }

    public int getHeight() { return spriteHeight; }

    public Rectangle2D[] getRow(int y) {
        if(y < 0 || y >= rows) { throw new IndexOutOfBoundsException(); }
        Rectangle2D[] row = new Rectangle2D[this.cols];
        for (int x = 0; x < this.cols; x++) {
            row[x] = new Rectangle2D(x * spriteWidth, y * spriteHeight, spriteWidth, spriteHeight);
        }
        return row;
    }

}
