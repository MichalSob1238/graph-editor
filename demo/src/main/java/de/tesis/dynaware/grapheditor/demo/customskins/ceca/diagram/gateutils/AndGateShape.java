package de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram.gateutils;

import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.Arrays;
import java.util.List;

public class AndGateShape extends Shape {

    private Rectangle background = new Rectangle();
    private Rectangle border = new Rectangle();
    private Arc semiCircleBackground = new Arc();


    public AndGateShape() {
        semiCircleBackground.setStartAngle(270.0f);
        semiCircleBackground.setLength(180.0f);
        semiCircleBackground.setSmooth(true);
        semiCircleBackground.setType(ArcType.OPEN);
        background.setArcHeight(15.0);
        background.setArcWidth(15.0);

    }

    public List<Shape> getComponents() {
        return Arrays.asList(background, border, semiCircleBackground);
    }

    public List<Shape> getBackgroundComponents() {
        return Arrays.asList(background, semiCircleBackground);
    }

    public Rectangle getBackground() {
        return background;
    }

    public Arc getSemiCircleBackground() {
        return semiCircleBackground;
    }

    public Rectangle getBorder() { return border; }
}
