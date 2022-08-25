package cause.effect.chain.editor.model.skins.statemachine;

import cause.effect.chain.editor.model.skins.statemachine.utils.LineNode;
import cause.effect.chain.editor.model.skins.statemachine.utils.LineUtils;
import de.tesis.dynaware.grapheditor.GTailSkin;
import de.tesis.dynaware.grapheditor.model.GConnector;
import javafx.geometry.Point2D;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class StateMachineTailSkin extends GTailSkin {

    private static final String STYLE_CLASS = "state-machine-tail";
    private static final double OFFSET_DISTANCE = 15;

    private final LineNode line = new LineNode();

    public StateMachineTailSkin(final GConnector connector) {

        super(connector);
        //////System.out.println("creating a tail skin");
        line.getStyleClass().add(STYLE_CLASS);
    }

    @Override
    public Node getRoot() {
        return line;
    }

    @Override
    public void draw(final Point2D start, final Point2D end) {
        drawLine(start, end);
    }

    @Override
    public void draw(final Point2D start, final Point2D end, final List<Point2D> jointPositions) {
        drawLine(start, end);
    }

    @Override
    public void draw(final Point2D start, final Point2D end, final GConnector target, final boolean valid) {
        drawLine(start, end);
    }

    @Override
    public void draw(final Point2D start, final Point2D end, final List<Point2D> jointPositions,
                     final GConnector target, final boolean valid) {
        drawLine(start, end);
    }

    @Override
    public List<Point2D> allocateJointPositions() {
        return new ArrayList<Point2D>();
    }

    /**
     * Draws an arrow from the start to end point.
     *
     * @param start the start point of the arrow
     * @param end the end point (tip) of the arrow
     */
    private void drawLine(final Point2D start, final Point2D end) {
        //////System.out.println("drawing line in a tail skin");

        if (getConnector().getType().equals(StateMachineConstants.STATE_MACHINE_RIGHT_OUTPUT_CONNECTOR)) {
            LineUtils.draw(line, start, end, OFFSET_DISTANCE);
        } else {
            LineUtils.draw(line, end, start, OFFSET_DISTANCE);
        }
    }
}