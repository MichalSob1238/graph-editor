package cause.effect.chain.editor.model.skins.statemachine;

import cause.effect.chain.editor.model.skins.statemachine.utils.LineNode;
import cause.effect.chain.editor.model.skins.statemachine.utils.LineUtils;
import de.tesis.dynaware.grapheditor.GConnectionSkin;
import de.tesis.dynaware.grapheditor.GJointSkin;
import de.tesis.dynaware.grapheditor.model.GConnection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
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
    Label lbl = new Label("");

    private final Group root = new Group(background, selectionHalo, line, line2, background2, line3, background3, text, lbl);
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

        betterSetText(getConnection().getDescription());

        text.setManaged(false);
        lbl.setVisible(true);
        text.setVisible(false);
        text.setOnMouseClicked(doubleClickedListener);
        root.getChildren().forEach(nd -> nd.setOnMouseClicked(doubleClickedListener));

        //Setting padding to the text flow


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

        final Point2D start = points.get(0);
        final Point2D end = points.get(1);
        final Point2D newStart = new Point2D((start.getX() + end.getX()) / 2.0 - 20, (start.getY() + end.getY()) / 2.0 - 20);
        final Point2D newEnd = new Point2D((start.getX() + end.getX()) / 2.0 + 20, (start.getY() + end.getY()) / 2.0 + 20);
        LineUtils.draw(line2, newStart, newEnd, OFFSET_FROM_CONNECTOR);
        LineUtils.draw(background2, newStart, newEnd, OFFSET_FROM_CONNECTOR);
        final Point2D newStart2 = new Point2D((start.getX() + end.getX()) / 2.0 - 20, (start.getY() + end.getY()) / 2.0 + 20);
        final Point2D newEnd2 = new Point2D((start.getX() + end.getX()) / 2.0 + 20, (start.getY() + end.getY()) / 2.0 - 20);
        LineUtils.draw(line3, newStart2, newEnd2, OFFSET_FROM_CONNECTOR);
        LineUtils.draw(background3, newStart2, newEnd2, OFFSET_FROM_CONNECTOR);
        adjustBlockVisibility();
        text.setVisible(false);
        Font font = new Font("Arial", 11);
        text.setFont(font);

//        textFlow.setVisible(true);
        double x = points.get(0).getX() / 2.0 + points.get(1).getX() / 2;
        int offset = Math.min(text.getText().length()*5, 150);
        text.setX(x - offset);
        text.setY(points.get(1).getY() / 2.0 + points.get(0).getY() / 2.0);
        lbl.setLayoutX(x- lbl.getWidth()/2.0 );

        lbl.setLayoutY(points.get(1).getY() / 2.0 + points.get(0).getY() / 2.0 - lbl.getHeight()/2);
        lbl.setWrapText(true);
        lbl.setMaxWidth(200);

        lbl.setOnMouseEntered(e -> {
            lbl.setScaleX(1.5);
            lbl.setScaleY(1.5);
            lbl.setTextFill(Color.DARKGOLDENROD);
        });

        lbl.setOnMouseExited(e -> {
            lbl.setScaleX(1);
            lbl.setScaleY(1);
            lbl.setTextFill(Color.BLACK);
        });
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public void betterSetText(String str) {
        getConnection().setDescription(str);
        lbl.setText(str);
        if (str.length() <= 50){
            text.setText(str);
            return;
        }
        List<String> results = new ArrayList<>();
        int length = str.length();

        for (int i = 0; i < length; i += 39) {
            results.add(str.substring(i, Math.min(length, i + 39)));
        }

        String toSet = String.join("\n",results);
        text.setText(toSet);

    }

    private void adjustBlockVisibility() {

        line2.setVisible(isBlocked);
        background2.setVisible(isBlocked);
        line3.setVisible(isBlocked);
        background3.setVisible(isBlocked);

    }

    private EventHandler<MouseEvent> getDoubleClickedListener() {
        return event -> {
            if (event.getClickCount() >= 2) {
                TextInputDialog td = new TextInputDialog(getConnection().getDescription());
                td.setContentText("Description");
                td.setHeaderText("Connection information");
                td.setTitle("Connection menu");
                ButtonType buttonTypeBlock = new ButtonType("BLOCK/UNBLOCK", ButtonBar.ButtonData.LEFT);


                td.getDialogPane().getButtonTypes().add(buttonTypeBlock);
                final Button btnBlock = (Button) td.getDialogPane().lookupButton(buttonTypeBlock);
                btnBlock.addEventFilter(
                        ActionEvent.ACTION,
                        action -> {
                            isBlocked = !isBlocked;
                            adjustBlockVisibility();
                        }
                );
                td.showAndWait();
                getConnection().setDescription(td.getEditor().getText());
                //////System.out.println("set description to + "  + getConnection().getDescription());
                betterSetText(getConnection().getDescription());
            }
        };
    }

}
