package cause.effect.chain.editor.model.skins.StateActionModel;

import de.tesis.dynaware.grapheditor.GTailSkin;
import de.tesis.dynaware.grapheditor.model.GConnector;
import javafx.geometry.Point2D;
import javafx.scene.Node;

import java.util.List;

public class CecaDiagramTailSkin extends GTailSkin {
    /**
     * Creates a new {@link GTailSkin}.
     *
     * @param connector the {@link GConnector} that the tail will extend from
     */
    public CecaDiagramTailSkin(GConnector connector) {
        super(connector);
    }

    @Override
    public Node getRoot() {
        return null;
    }

    @Override
    public void draw(Point2D start, Point2D end) {

    }

    @Override
    public void draw(Point2D start, Point2D end, GConnector target, boolean valid) {

    }

    @Override
    public void draw(Point2D start, Point2D end, List<Point2D> jointPositions) {

    }

    @Override
    public void draw(Point2D start, Point2D end, List<Point2D> jointPositions, GConnector target, boolean valid) {

    }

    @Override
    public List<Point2D> allocateJointPositions() {
        return null;
    }
}
