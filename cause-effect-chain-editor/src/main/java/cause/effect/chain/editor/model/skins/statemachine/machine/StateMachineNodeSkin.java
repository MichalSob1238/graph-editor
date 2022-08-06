package cause.effect.chain.editor.model.skins.statemachine.machine;

import com.jfoenix.controls.JFXTextField;
import de.tesis.dynaware.grapheditor.GConnectorSkin;
import de.tesis.dynaware.grapheditor.GNodeSkin;
import de.tesis.dynaware.grapheditor.core.skins.defaults.utils.DefaultConnectorTypes;
import de.tesis.dynaware.grapheditor.model.GConnector;
import de.tesis.dynaware.grapheditor.model.GNode;
import de.tesis.dynaware.grapheditor.utils.GeometryUtils;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StateMachineNodeSkin extends GNodeSkin {

    private final Label title = new Label();
    private final Rectangle border = new Rectangle();

    private static final double HALO_OFFSET = 5;
    private static final double HALO_CORNER_SIZE = 10;

    private static final double MINOR_POSITIVE_OFFSET = 2;
    private static final double MINOR_NEGATIVE_OFFSET = -3;

    private static final double MIN_WIDTH = 41;
    private static final double MIN_HEIGHT = 41;

    private final Rectangle selectionHalo = new Rectangle();
    private final Rectangle background = new Rectangle();

    private static final String STYLE_CLASS_BACKGROUND = "and-node-background";
    private static final String STYLE_CLASS_SELECTION_HALO = "default-node-selection-halo";
    private static final String STYLE_CLASS_BORDER = "default-node-border";

    private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass("selected");

    private final List<GConnectorSkin> topConnectorSkins = new ArrayList<>();
    private final List<GConnectorSkin> rightConnectorSkins = new ArrayList<>();
    private final List<GConnectorSkin> bottomConnectorSkins = new ArrayList<>();
    private final List<GConnectorSkin> leftConnectorSkins = new ArrayList<>();


    private EventHandler<? super MouseEvent> doubleClickedListener = getDoubleClickedListener();

    /**
     * Creates a new {@link GNodeSkin}.
     *
     * @param node the {@link GNode} represented by the skin
     */
    public StateMachineNodeSkin(GNode node) {
        super(node);

        border.widthProperty().bind(getRoot().widthProperty());
        border.heightProperty().bind(getRoot().heightProperty());
        background.widthProperty().bind(border.widthProperty().subtract(border.strokeWidthProperty().multiply(2)));
        background.heightProperty().bind(border.heightProperty().subtract(border.strokeWidthProperty().multiply(2)));

        getRoot().getChildren().addAll(border, background);

        title.setText("title!");
        System.out.println("setting title");
        title.setAlignment(Pos.CENTER);
        title.setVisible(true);
        title.setOnMouseClicked(doubleClickedListener);
        getRoot().getChildren().add(title);

        background.getStyleClass().setAll(STYLE_CLASS_BACKGROUND);
        border.getStyleClass().setAll(STYLE_CLASS_BORDER);
        getRoot().setMinSize(MIN_WIDTH, MIN_HEIGHT);

        addSelectionHalo();
        addSelectionListener();
    }

    private void addSelectionListener() {
        selectedProperty().addListener((v, o, n) -> {

            if (n) {
                selectionHalo.setVisible(true);
                layoutSelectionHalo();
                background.pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, true);
                getRoot().toFront();
            } else {
                selectionHalo.setVisible(false);
                background.pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, false);
            }
        });
    }

    @Override
    public void layoutConnectors() {
        layoutAllConnectors();
        layoutSelectionHalo();

    }

    void addSelectionHalo() {

        getRoot().getChildren().add(selectionHalo);

        selectionHalo.setManaged(false);
        selectionHalo.setMouseTransparent(false);
        selectionHalo.setVisible(false);

        selectionHalo.setLayoutX(-HALO_OFFSET);
        selectionHalo.setLayoutY(-HALO_OFFSET);
        selectionHalo.getStyleClass().add(STYLE_CLASS_SELECTION_HALO);

    }

    private void layoutSelectionHalo() {

        if (selectionHalo.isVisible()) {

            selectionHalo.setWidth(border.getWidth() + 2 * HALO_OFFSET);
            selectionHalo.setHeight(border.getHeight() + 2 * HALO_OFFSET);

            final double cornerLength = 2 * HALO_CORNER_SIZE;
            final double xGap = border.getWidth() - 2 * HALO_CORNER_SIZE + 2 * HALO_OFFSET;
            final double yGap = border.getHeight() - 2 * HALO_CORNER_SIZE + 2 * HALO_OFFSET;

            selectionHalo.setStrokeDashOffset(HALO_CORNER_SIZE);
            selectionHalo.getStrokeDashArray().setAll(cornerLength, yGap, cornerLength, xGap);
        }
    }

    private void layoutAllConnectors() {

        layoutConnectors(topConnectorSkins, false, 0);
        layoutConnectors(rightConnectorSkins, true, getRoot().getWidth());
        layoutConnectors(bottomConnectorSkins, false, getRoot().getHeight());
        layoutConnectors(leftConnectorSkins, true, 0);
    }

    private void layoutConnectors(final List<GConnectorSkin> connectorSkins, final boolean vertical, final double offset) {

        final int count = connectorSkins.size();

        for (int i = 0; i < count; i++) {

            final GConnectorSkin skin = connectorSkins.get(i);
            final Node root = skin.getRoot();

            if (vertical) {

                final double offsetY = getRoot().getHeight() / (count + 1);
                final double offsetX = getMinorOffsetX(skin.getConnector());

                root.setLayoutX(GeometryUtils.moveOnPixel(offset - skin.getWidth() / 2 + offsetX));
                root.setLayoutY(GeometryUtils.moveOnPixel((i + 1) * offsetY - skin.getHeight() / 2));

            } else {

                final double offsetX = getRoot().getWidth() / (count + 1);
                final double offsetY = getMinorOffsetY(skin.getConnector());

                root.setLayoutX(GeometryUtils.moveOnPixel((i + 1) * offsetX - skin.getWidth() / 2));
                root.setLayoutY(GeometryUtils.moveOnPixel(offset - skin.getHeight() / 2 + offsetY));
            }
        }
    }
    private double getMinorOffsetX(final GConnector connector) {

        final String type = connector.getType();

        if (type.equals(StateMachineConstants.STATE_MACHINE_LEFT_INPUT_CONNECTOR) || type.equals(StateMachineConstants.STATE_MACHINE_RIGHT_OUTPUT_CONNECTOR)) {
            return MINOR_POSITIVE_OFFSET;
        } else {
            return MINOR_NEGATIVE_OFFSET;
        }
    }

    private double getMinorOffsetY(final GConnector connector) {

        final String type = connector.getType();

        if (type.equals(StateMachineConstants.STATE_MACHINE_TOP_INPUT_CONNECTOR) || type.equals(StateMachineConstants.STATE_MACHINE_BOTTOM_OUTPUT_CONNECTOR)) {
            return MINOR_POSITIVE_OFFSET;
        } else {
            return MINOR_NEGATIVE_OFFSET;
        }
    }

    private EventHandler<MouseEvent> getDoubleClickedListener() {
        return event -> {
            if (event.getClickCount() >= 2) {
                System.out.println("State Machine handling doubleclick");
                JFXTextField descriptionEditable = new JFXTextField();
                descriptionEditable.setPrefSize(-1, -1);
                descriptionEditable.setMinSize(title.getWidth(), title.getHeight());
                descriptionEditable.setMaxSize(background.getWidth(), background.getHeight());
                descriptionEditable.setTranslateX(title.getTranslateX());
                descriptionEditable.setTranslateY(title.getTranslateY());
                descriptionEditable.setText(title.getText());
                title.setVisible(false);
                getRoot().getChildren().add(descriptionEditable);
                if (getNode().getType() != null) {
                    descriptionEditable.positionCaret(getNode().getDescription().length());
                }
                descriptionEditable.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        getNode().setDescription(descriptionEditable.getText());
                        title.setText(descriptionEditable.getText());
                        getRoot().getChildren().remove(descriptionEditable);
                        setDescription();
                        title.setVisible(true);
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

        title.setMinSize(width, height);
        title.setMaxSize(border.getWidth(), border.getHeight());
        title.setTextAlignment(TextAlignment.CENTER);
        title.resize(border.getWidth(),border.getHeight());
        title.setText(Optional.ofNullable(getNode().getDescription()).orElse("!!"));
        title.setFont(font);
    }

    @Override
    public void setConnectorSkins(final List<GConnectorSkin> connectorSkins) {

        removeAllConnectors();

        topConnectorSkins.clear();
        rightConnectorSkins.clear();
        bottomConnectorSkins.clear();
        leftConnectorSkins.clear();

        if (connectorSkins != null) {
            for (final GConnectorSkin connectorSkin : connectorSkins) {

                final String connectorType = connectorSkin.getConnector().getType();
                switch (typeOfConnector(connectorType)) {
                    case "top":
                        topConnectorSkins.add(connectorSkin);
                        break;
                    case "bottom":
                        bottomConnectorSkins.add(connectorSkin);
                        break;
                    case "left":
                        leftConnectorSkins.add(connectorSkin);
                        break;
                    case "right":
                        rightConnectorSkins.add(connectorSkin);
                        break;
                }

                getRoot().getChildren().add(connectorSkin.getRoot());
            }
        }
        layoutConnectors();
    }

    private void removeAllConnectors() {

        for (final GConnectorSkin connectorSkin : topConnectorSkins) {
            getRoot().getChildren().remove(connectorSkin.getRoot());
        }

        for (final GConnectorSkin connectorSkin : bottomConnectorSkins) {
            getRoot().getChildren().remove(connectorSkin.getRoot());
        }

        for (final GConnectorSkin connectorSkin : leftConnectorSkins) {
            getRoot().getChildren().remove(connectorSkin.getRoot());
        }

        for (final GConnectorSkin connectorSkin : rightConnectorSkins) {
            getRoot().getChildren().remove(connectorSkin.getRoot());
        }
    }

    private String typeOfConnector(String connectorType) {

        String result = "invalid";
        if (DefaultConnectorTypes.isTop(connectorType)) {
            result = "top";
        } else if (DefaultConnectorTypes.isBottom(connectorType)) {
            result = "bottom";
        } else if (DefaultConnectorTypes.isRight(connectorType)) {
            result = "right";
        } else if (DefaultConnectorTypes.isLeft(connectorType)) {
            result = "left";
        }
        return result;
    }

    @Override
    public Point2D getConnectorPosition(GConnectorSkin connectorSkin){
        final Node connectorRoot = connectorSkin.getRoot();

        final Side side = DefaultConnectorTypes.getSide(connectorSkin.getConnector().getType());

        // The following logic is required because the connectors are offset slightly from the node edges.
        final double x, y;
        if (side.equals(Side.LEFT)) {
            x = 0;
            y = connectorRoot.getLayoutY() + connectorSkin.getHeight() / 2;
        } else if (side.equals(Side.RIGHT)) {
            x = getRoot().getWidth();
            y = connectorRoot.getLayoutY() + connectorSkin.getHeight() / 2;
        } else if (side.equals(Side.TOP)) {
            x = connectorRoot.getLayoutX() + connectorSkin.getWidth() / 2;
            y = 0;
        } else {
            x = connectorRoot.getLayoutX() + connectorSkin.getWidth() / 2;
            y = getRoot().getHeight();
        }

        return new Point2D(x, y);
    }

    @Override
    public void updateStatus(List<String> status) {

    }
}
