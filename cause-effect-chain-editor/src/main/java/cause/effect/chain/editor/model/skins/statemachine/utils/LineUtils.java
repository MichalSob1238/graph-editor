package cause.effect.chain.editor.model.skins.statemachine.utils;

import javafx.geometry.Point2D;

public class LineUtils {

    public static void draw(final LineNode line, final Point2D start, final Point2D end, final double offset) {

        final double deltaX = end.getX() - start.getX();
        final double deltaY = end.getY() - start.getY();

        final double angle = Math.atan2(deltaX, deltaY);

        final double startX = start.getX() + offset * Math.sin(angle);
        final double startY = start.getY() + offset * Math.cos(angle);

        final double endX = end.getX() - offset * Math.sin(angle);
        final double endY = end.getY() - offset * Math.cos(angle);

        line.setStart(startX, startY);
        line.setEnd(endX, endY);
        line.draw();
        line.setVisible(true);

    }
}
