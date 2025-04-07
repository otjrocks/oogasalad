package oogasalad.player.view;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


public class SpriteAnimationView extends Transition {

    private final ImageView myImageView;
    private final int myFrameCount;
    private final int myFrameWidth;
    private final int myFrameHeight;
    private final int myOffsetX;
    private final int myOffsetY;
    private final int myCurrentIndex;

    public SpriteAnimationView(ImageView imageView, Duration duration, int frameCount,
            int offsetX, int offsetY, int frameWidth, int frameHeight, int currentIndex) {
        myImageView = imageView;
        myFrameCount = frameCount;
        myFrameWidth = frameWidth;
        myFrameHeight = frameHeight;
        myOffsetX = offsetX;
        myOffsetY = offsetY;
        myCurrentIndex = currentIndex;

        imageView.setViewport(new Rectangle2D(offsetX, offsetY, frameWidth, frameHeight));
        setCycleDuration(duration);
        setCycleCount(Animation.INDEFINITE);
        setInterpolator(Interpolator.LINEAR);
    }

    @Override
    protected void interpolate(double frac) {
        int x = (myCurrentIndex % myFrameCount) * myFrameWidth + myOffsetX;
        myImageView.setViewport(new Rectangle2D(x, myOffsetY, myFrameWidth, myFrameHeight));
    }
}
