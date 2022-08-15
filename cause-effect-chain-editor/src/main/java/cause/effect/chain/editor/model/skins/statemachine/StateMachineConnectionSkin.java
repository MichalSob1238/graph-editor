package cause.effect.chain.editor.model.skins.statemachine;

import cause.effect.chain.editor.model.skins.statemachine.utils.LineNode;
import cause.effect.chain.editor.model.skins.statemachine.utils.LineUtils;
import de.tesis.dynaware.grapheditor.GConnectionSkin;
import de.tesis.dynaware.grapheditor.GJointSkin;
import de.tesis.dynaware.grapheditor.model.GConnection;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Map;

public class StateMachineConnectionSkin extends GConnectionSkin {

    private static final double OFFSET_FROM_CONNECTOR = 10;
    private static final String STYLE_CLASS = "state-machine-connection";


    private final Line haloFirstSide = new Line();
    private final Line haloSecondSide = new Line();
    private final Group selectionHalo = new Group(haloFirstSide, haloSecondSide);
    private final LineNode line = new LineNode();
    private final LineNode background = new LineNode();
    private final LineNode line2 = new LineNode();
    private final LineNode background2 = new LineNode();
    private final LineNode line3 = new LineNode();
    private final LineNode background3 = new LineNode();
    private Text text = new Text("some text");
    private final Group root = new Group(background,selectionHalo, line, line2, background2, line3, background3, text);
    private EventHandler<? super MouseEvent> doubleClickedListener = getDoubleClickedListener();
    private boolean isBlocked = false;

    private List<Point2D> points;

    /**
     * Creates a new {@link GConnectionSkin}.
     *
     * @param connection the {@link GConnection} represented by the skin
     */
    public StateMachineConnectionSkin(GConnection connection) {
        super(connection);
        
        line.setManaged(false);
        line.getStyleClass().setAll(STYLE_CLASS);

        text.setText("TEXT " + getConnection().getDescription());
        text.setManaged(false);
        System.out.println("SMCON");
        text.setOnMouseClicked(doubleClickedListener);
        background.setManaged(false);
        getConnection().getTarget().getParent();
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
        if (true)
        {
            final Point2D start = points.get(0);
            final Point2D end = points.get(1);
            final Point2D newStart = new Point2D((start.getX() + end.getX())/2.0 -20 , (start.getY() + end.getY())/2.0 - 20);
            final Point2D newEnd = new Point2D((start.getX() + end.getX())/2.0 + 20 , (start.getY() + end.getY())/2.0 + 20);
            LineUtils.draw(line2, newStart, newEnd, OFFSET_FROM_CONNECTOR);
            LineUtils.draw(background2, newStart, newEnd, OFFSET_FROM_CONNECTOR);
            final Point2D newStart2 = new Point2D((start.getX() + end.getX())/2.0 - 20 , (start.getY() + end.getY())/2.0 + 20);
            final Point2D newEnd2 = new Point2D((start.getX() + end.getX())/2.0  + 20 , (start.getY() + end.getY())/2.0 - 20);
            LineUtils.draw(line3, newStart2, newEnd2, OFFSET_FROM_CONNECTOR);
            LineUtils.draw(background3, newStart2, newEnd2, OFFSET_FROM_CONNECTOR);
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

    private EventHandler<MouseEvent> getDoubleClickedListener() {
        return event -> {
            if (event.getClickCount() >= 2) {
                TextInputDialog td = new TextInputDialog();
                td.showAndWait();
                getConnection().setDescription(td.getEditor().getText());
                System.out.println("set description to + "  + getConnection().getDescription());
                text.setText(getConnection().getDescription());
            }
        };
    }

}
