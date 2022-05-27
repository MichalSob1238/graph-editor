package de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram;

import de.tesis.dynaware.grapheditor.GConnectionSkin;
import de.tesis.dynaware.grapheditor.GJointSkin;
import de.tesis.dynaware.grapheditor.model.GConnection;
import javafx.geometry.Point2D;
import javafx.scene.Node;

import java.util.List;
import java.util.Map;

public class CecaDiagramConnectionSkin extends GConnectionSkin {
    /**
     * Creates a new {@link GConnectionSkin}.
     *
     * @param connection the {@link GConnection} represented by the skin
     */
    public CecaDiagramConnectionSkin(GConnection connection) {
        super(connection);
    }

    @Override
    public void setJointSkins(List<GJointSkin> jointSkins) {
        // This skin is not intended to show joints.
    }

    @Override
    public void draw(List<Point2D> points, Map<GConnection, List<Point2D>> allConnections) {

    }

    @Override
    public Node getRoot() {
        return null;
    }
}
