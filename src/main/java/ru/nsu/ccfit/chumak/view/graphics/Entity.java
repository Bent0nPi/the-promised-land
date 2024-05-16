package ru.nsu.ccfit.chumak.view.graphics;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

public class Entity{
    private final ImageView view;
    private  SpriteSheet sprites;
    private Animation animation;
    private double angle = 0;


    public Entity(SpriteSheet sprites, Point2D position) {
        if (sprites == null) {
            throw new IllegalArgumentException("Entity received null sprite sheet");
        }
        if (sprites.getImage() == null) {
            throw new IllegalArgumentException("Entity sprite sheet has null image");
        }

        this.sprites = sprites;
        this.animation = new Animation(sprites.getRow(0), false, true);
        this.view = new ImageView(sprites.getImage());
        this.view.setViewport(animation.getFrame());
        this.setPosition(position);
    }

    public Entity(SpriteSheet sprites, int startAnimationIdx, Point2D position) {
        if (sprites == null) {
            throw new IllegalArgumentException("Entity received null sprite sheet");
        }
        if (sprites.getImage() == null) {
            throw new IllegalArgumentException("Entity sprite sheet has null image");
        }

        this.sprites = sprites;
        this.animation = new Animation(sprites.getRow(startAnimationIdx), false, true);
        this.view = new ImageView(sprites.getImage());
        this.view.setViewport(animation.getFrame());
        this.setPosition(position);
    }



    public void update() {
        this.view.setViewport(animation.getFrame());
        animation.play();
    }

    public void setAnimation(Animation animation) {
        if (this.animation != animation) {
            this.animation = animation.restart();
        }
    }

    public void setPosition(Point2D position) {
        this.view.setLayoutX(position.getX());
        this.view.setLayoutY(position.getY());
    }

    public SpriteSheet getSprites() {
        return sprites;
    }

    public void setSprites(SpriteSheet sprites) {
        this.sprites = sprites;
        this.view.setImage(sprites.getImage());
    }

    public Point2D getPosition() {
        return new Point2D(this.view.getLayoutX(), this.view.getLayoutY());
    }

    public boolean intersects(Entity other) {
        return this.view.getBoundsInParent().intersects(other.getView().getBoundsInParent());
    }

    public double distanceFrom(Point2D target) {
        return this.getPosition().distance(target);
    }

    public void resize(double quotient) {
        this.view.setScaleX(quotient);
        this.view.setScaleY(quotient);
    }

    public void rotateView(double angle) {
        this.angle += angle;
        this.view.setRotate(-this.angle);
    }

    public double getAngle() {
        return angle;
    }

    public ImageView getView() {
        return this.view;
    }
}
