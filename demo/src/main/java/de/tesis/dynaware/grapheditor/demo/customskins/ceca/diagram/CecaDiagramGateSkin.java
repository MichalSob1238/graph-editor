package de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram;

import com.jfoenix.controls.JFXTextField;
import de.tesis.dynaware.grapheditor.GConnectorSkin;
import de.tesis.dynaware.grapheditor.GNodeSkin;
import de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram.gateutils.AndGateShape;
import de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram.gateutils.OrGateShape;
import de.tesis.dynaware.grapheditor.model.GNode;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CecaDiagramGateSkin extends CecaDiagramNodeSkin {
    private final Rectangle selectionHalo = new Rectangle();

    private final List<GConnectorSkin> topConnectorSkins = new ArrayList<>();
    private final List<GConnectorSkin> rightConnectorSkins = new ArrayList<>();
    private final List<GConnectorSkin> bottomConnectorSkins = new ArrayList<>();
    private final List<GConnectorSkin> leftConnectorSkins = new ArrayList<>();

    private final AndGateShape andGateShape = new AndGateShape();
    private final OrGateShape orGateShape = new OrGateShape();

    private final Rectangle border = new Rectangle();
    private final Rectangle background = new Rectangle();

    private final Label description = new Label();

    private final String type = getNode().getSubtype();
    private EventHandler<? super MouseEvent> doubleClickedListener = getDoubleClickedListener();

    private EventHandler<MouseEvent> getDoubleClickedListener() {
        return event -> {
            if (event.getClickCount() >= 2) {
                System.out.println("handling doubleclick");
                JFXTextField descriptionEditable = new JFXTextField();
                descriptionEditable.setPrefSize(-1, -1);
                descriptionEditable.setMinSize(description.getWidth(), description.getHeight());
                descriptionEditable.setMaxSize(background.getWidth(), background.getHeight());
                descriptionEditable.setTranslateX(description.getTranslateX());
                descriptionEditable.setTranslateY(description.getTranslateY());
                descriptionEditable.setText(description.getText());
                description.setVisible(false);
                getRoot().getChildren().add(descriptionEditable);
                if (getNode().getType() != null) {
                    descriptionEditable.positionCaret(getNode().getDescription().length());
                }
                descriptionEditable.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        getNode().setDescription(descriptionEditable.getText());
                        description.setText(descriptionEditable.getText());
                        getRoot().getChildren().remove(descriptionEditable);
                        setDescription();
                        description.setVisible(true);
                    }
                });
            }
        };
    }

    //TODO: customise
    public void setDescription() {
        System.out.println("setting description");
        final Text text = new Text(getNode().getDescription());
        new Scene(new Group(text));
        text.applyCss();
        final double width = Math.max(50, text.getLayoutBounds().getWidth());
        final double height = Math.max(20, text.getLayoutBounds().getHeight());

        Font font = new Font("Arial", 17);

        description.setMinSize(width, height);
        description.setMaxSize(border.getWidth(), border.getHeight());
        description.setTextAlignment(TextAlignment.CENTER);
        description.resize(border.getWidth(),border.getHeight());
        description.setText(Optional.ofNullable(getNode().getDescription()).orElse("!!"));
        description.setFont(font);
    }


    /**
     * Creates a new {@link GNodeSkin}.
     *
     * @param node the {@link GNode} represented by the skin
     */
    public CecaDiagramGateSkin(GNode node) {
        super(node);
        if (type == "and") {
            andGateShape.getBackground().translateXProperty().bind(getRoot().widthProperty().divide(-2.0).add(andGateShape.getBackground().widthProperty().divide(2.0)));
            andGateShape.getBackground().widthProperty().bind(getRoot().widthProperty().multiply(0.9));
            andGateShape.getBackground().heightProperty().bind(getRoot().heightProperty());
            andGateShape.getBackground().setStroke(Color.TRANSPARENT);
            andGateShape.getSemiCircleBackground().radiusXProperty().bind(getRoot().widthProperty().multiply(0.1));
            andGateShape.getSemiCircleBackground().radiusYProperty().bind(andGateShape.getBackground().heightProperty().divide(2.0));
            andGateShape.getSemiCircleBackground().translateXProperty().bind(getRoot().widthProperty().divide(2.0).subtract(andGateShape.getSemiCircleBackground().radiusXProperty().divide(2.0).add(1.0)));

            getRoot().getChildren().addAll(andGateShape.getBackgroundComponents());
        } else {
            System.out.println("not and");
            orGateShape.getBackground().translateXProperty().bind(getRoot().widthProperty().divide(-2.0).add(orGateShape.getBackground().widthProperty().divide(2.0)).add(orGateShape.getSemiCircleBackgroundFront().radiusXProperty().divide(2.0).add(1.0)));
            orGateShape.getBackground().widthProperty().bind(getRoot().widthProperty().multiply(0.8));
            orGateShape.getBackground().heightProperty().bind(getRoot().heightProperty());
            orGateShape.getBackground().setStroke(Color.TRANSPARENT);
            orGateShape.getSemiCircleBackgroundBack().radiusXProperty().bind(getRoot().widthProperty().multiply(0.1));
            orGateShape.getSemiCircleBackgroundBack().radiusYProperty().bind(orGateShape.getBackground().heightProperty().divide(2.0));
            orGateShape.getSemiCircleBackgroundBack().translateXProperty().bind(getRoot().widthProperty().divide(2.0).subtract(orGateShape.getSemiCircleBackgroundBack().radiusXProperty().divide(2.0).add(1.0)));
            orGateShape.getSemiCircleBackgroundFront().radiusXProperty().bind(getRoot().widthProperty().multiply(0.2));
            orGateShape.getSemiCircleBackgroundFront().radiusYProperty().bind(orGateShape.getBackground().heightProperty().divide(2.0));
            orGateShape.getSemiCircleBackgroundFront().translateXProperty().bind(getRoot().widthProperty().divide(-2.0).add(orGateShape.getSemiCircleBackgroundFront().radiusXProperty().divide(2.0).add(1.0)));

            getRoot().getChildren().addAll(orGateShape.getBackgroundComponents());
        }

        description.setText("description!");
        System.out.println("setting description");
        description.setAlignment(Pos.CENTER);
        description.setVisible(true);
        description.setOnMouseClicked(doubleClickedListener);
        getRoot().getChildren().add(description);
        //addSelectionHalo();
        addSelectionListener();
    }
}
