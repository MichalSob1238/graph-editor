package de.tesis.dynaware.grapheditor.demo.customskins.state.machine;

import de.tesis.dynaware.grapheditor.GConnectionSkin;
import de.tesis.dynaware.grapheditor.GJointSkin;
import de.tesis.dynaware.grapheditor.core.skins.defaults.connection.IntersectionFinder;
import de.tesis.dynaware.grapheditor.core.skins.defaults.connection.segment.ConnectionSegment;
import de.tesis.dynaware.grapheditor.demo.customskins.state.machine.utils.LineUtils;
import de.tesis.dynaware.grapheditor.demo.customskins.tree.ArrowUtils;
import de.tesis.dynaware.grapheditor.demo.customskins.tree.TreeSkinConstants;
import de.tesis.dynaware.grapheditor.model.GConnection;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StateMachineConnectionSkin extends GConnectionSkin {

    private static final double OFFSET_FROM_CONNECTOR = 10;

    protected final Path path = new Path();
    protected final Path backgroundPath = new Path();
    private final Line line = new Line();
    private final Line background = new Line();
    private Text text = new Text("some text");
    private final Group root = new Group(background, text);

    private List<Point2D> points;

    /**
     * Creates a new {@link GConnectionSkin}.
     *
     * @param connection the {@link GConnection} represented by the skin
     */
    public StateMachineConnectionSkin(GConnection connection) {
        super(connection);

        text.setText("TEXT" + getConnection().getDescription());
        text.setManaged(false);
        System.out.println("SMCON");
        background.setManaged(false);
    }

    @Override
    public void setJointSkins(List<GJointSkin> jointSkins) {
        //no joints
    }

    @Override
    public void draw(List<Point2D> points, Map<GConnection, List<Point2D>> allConnections) {
        if (!points.equals(this.points) && points.size() == 2) {

            final Point2D start = points.get(0);
            final Point2D end = points.get(1);

            LineUtils.draw(line, start, end, OFFSET_FROM_CONNECTOR);
            LineUtils.draw(background, start, end, OFFSET_FROM_CONNECTOR);

            this.points = points;
        }
        text.setVisible(true);
        double x = points.get(0).getX()/2.0 + points.get(1).getX()/2;
        text.setX(x);
        text.setY(points.get(1).getY()/2.0 + points.get(0).getY()/2.0);
    }

    @Override
    public Node getRoot() {
        return root;
    }
}
