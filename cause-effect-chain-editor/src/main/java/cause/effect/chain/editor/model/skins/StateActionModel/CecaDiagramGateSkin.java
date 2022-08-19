package cause.effect.chain.editor.model.skins.StateActionModel;

import cause.effect.chain.editor.model.skins.StateActionModel.gateutils.AndGateShape;
import cause.effect.chain.editor.model.skins.StateActionModel.gateutils.OrGateShape;
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
import java.util.Objects;
import java.util.Optional;

public class CecaDiagramGateSkin extends GNodeSkin {
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
    private static final String STYLE_CLASS_BACKGROUND = "and-node-background";
    private static final String STYLE_CLASS_SELECTION_HALO = "default-node-selection-halo";


    private static final double HALO_OFFSET = 5;
    private static final double HALO_CORNER_SIZE = 10;

    private static final double MINOR_POSITIVE_OFFSET = 2;
    private static final double MINOR_NEGATIVE_OFFSET = -3;

    private final String subtype = getNode().getSubtype();
    private EventHandler<? super MouseEvent> doubleClickedListener = getDoubleClickedListener();

    private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass("selected");

    private EventHandler<MouseEvent> getDoubleClickedListener() {
        return event -> {
            if (event.getClickCount() >= 2) {
                ////System.out.println("handling doubleclick");
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
        ////System.out.println("setting description");
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
        if (Objects.equals(subtype, "and")) {
            andGateShape.getBackground().translateXProperty().bind(getRoot().widthProperty().divide(-2.0).add(andGateShape.getBackground().widthProperty().divide(2.0)));
            andGateShape.getBackground().widthProperty().bind(getRoot().widthProperty().multiply(0.9));
            andGateShape.getBackground().heightProperty().bind(getRoot().heightProperty());
            andGateShape.getSemiCircleBackground().radiusXProperty().bind(getRoot().widthProperty().multiply(0.1));
            andGateShape.getSemiCircleBackground().radiusYProperty().bind(andGateShape.getBackground().heightProperty().divide(2.0));
            andGateShape.getSemiCircleBackground().translateXProperty().bind(getRoot().widthProperty().divide(2.0).subtract(andGateShape.getSemiCircleBackground().radiusXProperty().divide(2.0).add(1.0)));
            andGateShape.getBackgroundComponents().forEach(shape -> shape.getStyleClass().setAll(STYLE_CLASS_BACKGROUND));

            getRoot().getChildren().addAll(andGateShape.getBackgroundComponents());
        } else {
            ////System.out.println("not and");
            orGateShape.getBackground().translateXProperty().bind(getRoot().widthProperty().divide(-2.0).add(orGateShape.getBackground().widthProperty().divide(2.0)).add(orGateShape.getSemiCircleBackgroundFront().radiusXProperty().divide(2.0)).add(1));
            orGateShape.getBackground().widthProperty().bind(getRoot().widthProperty().multiply(0.4));
            orGateShape.getBackground().heightProperty().bind(getRoot().heightProperty());
            orGateShape.getSemiCircleBackgroundBack().radiusXProperty().bind(getRoot().widthProperty().multiply(0.4));
            orGateShape.getSemiCircleBackgroundBack().radiusYProperty().bind(orGateShape.getBackground().heightProperty().divide(2.0));
            orGateShape.getSemiCircleBackgroundBack().translateXProperty().bind(getRoot().widthProperty().divide(2.0).subtract(orGateShape.getSemiCircleBackgroundBack().radiusXProperty().divide(2.0)));
            orGateShape.getSemiCircleBackgroundFront().radiusXProperty().bind(getRoot().widthProperty().multiply(0.4));
            orGateShape.getSemiCircleBackgroundFront().radiusYProperty().bind(orGateShape.getBackground().heightProperty().divide(2.0));
            orGateShape.getSemiCircleBackgroundFront().translateXProperty().bind(getRoot().widthProperty().divide(-2.0).add(orGateShape.getSemiCircleBackgroundFront().radiusXProperty().divide(2.0)));
            orGateShape.getBackgroundComponents().forEach(shape -> shape.getStyleClass().setAll(STYLE_CLASS_BACKGROUND));

            getRoot().getChildren().addAll(orGateShape.getBackgroundComponents());
        }

        description.setText(subtype);
        ////System.out.println("setting description");
        description.setAlignment(Pos.CENTER);
        description.setVisible(true);
        description.setOnMouseClicked(doubleClickedListener);
        getRoot().getChildren().add(description);

        background.addEventFilter(MouseEvent.MOUSE_DRAGGED, this::filterMouseDragged);

        addSelectionHalo();
        addSelectionListener();

    }

    private void addSelectionListener() {

        selectedProperty().addListener((v, o, n) -> {
            if (Objects.equals(subtype, "and")) {
                if (n) {
                    selectionHalo.setVisible(true);
                    layoutSelectionHalo();
                    andGateShape.getBackgroundComponents().forEach(shape -> shape.pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, true));
                    getRoot().toFront();
                } else {
                    selectionHalo.setVisible(false);
                    andGateShape.getBackgroundComponents().forEach(shape -> shape.pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, false));
                }
            } else {
                if (n) {
                    selectionHalo.setVisible(true);
                    layoutSelectionHalo();
                    orGateShape.getBackgroundComponents().forEach(shape -> shape.pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, true));
                    getRoot().toFront();
                } else {
                    selectionHalo.setVisible(false);
                    orGateShape.getBackgroundComponents().forEach(shape -> shape.pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, false));
                }
            }
        });
    }

    private void addSelectionHalo() {

        getRoot().getChildren().add(selectionHalo);

        selectionHalo.setManaged(false);
        selectionHalo.setMouseTransparent(false);
        selectionHalo.setVisible(false);

        selectionHalo.setLayoutX(-HALO_OFFSET);
        selectionHalo.setLayoutY(-HALO_OFFSET);

        selectionHalo.getStyleClass().add(STYLE_CLASS_SELECTION_HALO);
    }

    @Override
    public void setConnectorSkins(List<GConnectorSkin> connectorSkins) {
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

    @Override
    public void layoutConnectors() {
        layoutAllConnectors();
        layoutSelectionHalo();

    }
    private void layoutSelectionHalo() {

        if (selectionHalo.isVisible()) {

            selectionHalo.setWidth(getRoot().getWidth() + 2 * HALO_OFFSET);
            selectionHalo.setHeight(getRoot().getHeight() + 2 * HALO_OFFSET);

            final double cornerLength = 2 * HALO_CORNER_SIZE;
            final double xGap = getRoot().getWidth() - 2 * HALO_CORNER_SIZE + 2 * HALO_OFFSET;
            final double yGap = getRoot().getHeight() - 2 * HALO_CORNER_SIZE + 2 * HALO_OFFSET;

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

                root.setLayoutX(GeometryUtils.moveOnPixel((i + 1) * offsetX - skin.getWidth() / 2 + 2));
                root.setLayoutY(GeometryUtils.moveOnPixel(offset - skin.getHeight() / 2 + offsetY));
            }
        }
    }

    private double getMinorOffsetX(final GConnector connector) {

        final String type = connector.getType();

        if (type.equals(DefaultConnectorTypes.LEFT_INPUT) || type.equals(DefaultConnectorTypes.RIGHT_OUTPUT)) {
            return MINOR_POSITIVE_OFFSET;
        } else {
            return MINOR_NEGATIVE_OFFSET;
        }
    }

    private double getMinorOffsetY(final GConnector connector) {

        final String type = connector.getType();

        if (type.equals(DefaultConnectorTypes.TOP_INPUT) || type.equals(DefaultConnectorTypes.BOTTOM_OUTPUT)) {
            return MINOR_POSITIVE_OFFSET;
        } else {
            return MINOR_NEGATIVE_OFFSET;
        }
    }

    @Override
    public Point2D getConnectorPosition(GConnectorSkin connectorSkin) {
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
    public int updateStatus(List<String> status) {
        return status.size();
    }

    private void filterMouseDragged(final MouseEvent event) {
        if (event.isPrimaryButtonDown() && !isSelected()) {
            event.consume();
        }
    }
}
