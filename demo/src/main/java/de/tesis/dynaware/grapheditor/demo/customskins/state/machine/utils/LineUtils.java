package de.tesis.dynaware.grapheditor.demo.customskins.state.machine.utils;

import de.tesis.dynaware.grapheditor.utils.GeometryUtils;
import javafx.geometry.Point2D;

import javafx.scene.shape.Line;

public class LineUtils {

    public static void draw(final Line line, final Point2D start, final Point2D end, final double offset) {

        final double deltaX = end.getX() - start.getX();
        final double deltaY = end.getY() - start.getY();

        final double angle = Math.atan2(deltaX, deltaY);

        final double startX = start.getX() + offset * Math.sin(angle);
        final double startY = start.getY() + offset * Math.cos(angle);

        final double endX = end.getX() - offset * Math.sin(angle);
        final double endY = end.getY() - offset * Math.cos(angle);

        line.setStartX(GeometryUtils.moveOffPixel(startX));
        line.setStartY(GeometryUtils.moveOffPixel(startY));
        line.setEndX(GeometryUtils.moveOffPixel(endX));
        line.setEndY(GeometryUtils.moveOffPixel(endY));

    }
}
