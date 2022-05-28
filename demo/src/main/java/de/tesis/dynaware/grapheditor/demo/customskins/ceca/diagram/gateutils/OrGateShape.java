package de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram.gateutils;

import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.Arrays;
import java.util.List;

public class OrGateShape extends Shape {

    private Rectangle background = new Rectangle();
    private Rectangle border = new Rectangle();
    private Arc semiCircleBackgroundBack = new Arc();
    private Arc semiCircleBackgroundFront = new Arc();


    public OrGateShape() {
        semiCircleBackgroundBack.setStartAngle(270.0f);
        semiCircleBackgroundBack.setLength(180.0f);
        semiCircleBackgroundBack.setSmooth(true);
        semiCircleBackgroundBack.setType(ArcType.OPEN);
        semiCircleBackgroundFront.setStartAngle(225.0f);
        semiCircleBackgroundFront.setLength(270.0f);
        semiCircleBackgroundFront.setSmooth(true);
        semiCircleBackgroundFront.setType(ArcType.ROUND);
        semiCircleBackgroundFront.setFill(Color.YELLOW);
        background.setArcHeight(15.0);
        background.setArcWidth(15.0);

    }

    public List<Shape> getComponents() {
        return Arrays.asList(background, border, semiCircleBackgroundBack, semiCircleBackgroundFront);
    }

    public List<Shape> getBackgroundComponents() {
        return Arrays.asList(background, semiCircleBackgroundBack, semiCircleBackgroundFront);
    }

    public Rectangle getBackground() {
        return background;
    }

    public Arc getSemiCircleBackgroundBack() {
        return semiCircleBackgroundBack;
    }

    public Arc getSemiCircleBackgroundFront() {
        return semiCircleBackgroundFront;
    }
}
