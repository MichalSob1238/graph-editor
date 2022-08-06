package cause.effect.chain.editor.model.skins.statemachine.machine.utils;

import de.tesis.dynaware.grapheditor.utils.Arrow;
import de.tesis.dynaware.grapheditor.utils.GeometryUtils;
import javafx.geometry.Point2D;
import javafx.scene.Group;

public class LineNode extends Group {

    private static final String STYLE_CLASS_LINE = "line";

    private final javafx.scene.shape.Line line = new javafx.scene.shape.Line();

    private double startX;
    private double startY;

    private double endX;
    private double endY;

    /**
     * Creates a new {@link Arrow}.
     */
    public LineNode() {

        line.getStyleClass().add(STYLE_CLASS_LINE);

        getChildren().addAll(line);
    }


    /**
     * Gets the start point of the arrow.
     *
     * @return the start {@link Point2D} of the arrow
     */
    public Point2D getStart() {
        return new Point2D(startX, startY);
    }

    /**
     * Sets the start position of the arrow.
     *
     * @param startX the x-coordinate of the start position of the arrow
     * @param startY the y-coordinate of the start position of the arrow
     */
    public void setStart(final double startX, final double startY) {
        this.startX = startX;
        this.startY = startY;
    }

    /**
     * Gets the start point of the arrow.
     *
     * @return the start {@link Point2D} of the arrow
     */
    public Point2D getEnd() {
        return new Point2D(endX, endY);
    }

    /**
     * Sets the end position of the arrow.
     *
     * @param endX the x-coordinate of the end position of the arrow
     * @param endY the y-coordinate of the end position of the arrow
     */
    public void setEnd(final double endX, final double endY) {
        this.endX = endX;
        this.endY = endY;
    }

    /**
     * Draws the arrow for its current size and position values.
     */
    public void draw() {

        final double deltaX = endX - startX;
        final double deltaY = endY - startY;

        final double angle = Math.atan2(deltaX, deltaY);


        line.setStartX(GeometryUtils.moveOffPixel(startX));
        line.setStartY(GeometryUtils.moveOffPixel(startY));
        line.setEndX(GeometryUtils.moveOffPixel(endX));
        line.setEndY(GeometryUtils.moveOffPixel(endY));

    }
}
