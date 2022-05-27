package de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram;

import de.tesis.dynaware.grapheditor.GConnectorSkin;
import de.tesis.dynaware.grapheditor.GNodeSkin;
import de.tesis.dynaware.grapheditor.model.GNode;
import de.tesis.dynaware.grapheditor.utils.GeometryUtils;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class CecaDiagramNodeSkin extends GNodeSkin {

    private Label title = new Label();
    private final Rectangle border = new Rectangle();

    private final List<GConnectorSkin> inputConnectorSkins = new ArrayList<>();
    private final List<GConnectorSkin> outputConnectorSkins = new ArrayList<>();

    /**
     * Creates a new {@link GNodeSkin}.
     *
     * @param node the {@link GNode} represented by the skin
     */
    public CecaDiagramNodeSkin(GNode node) {
        super(node);


        border.widthProperty().bind(getRoot().widthProperty());
        border.heightProperty().bind(getRoot().heightProperty());

        getRoot().getChildren().add(border);


        title.setText("title!");
        System.out.println("setting title");
        title.setAlignment(Pos.CENTER);
        title.setVisible(true);
        getRoot().getChildren().add(title);
    }

    @Override
    public void setConnectorSkins(final List<GConnectorSkin> connectorSkins) {

        removeAllConnectors();

        inputConnectorSkins.clear();
        outputConnectorSkins.clear();

        if (connectorSkins != null) {
            for (final GConnectorSkin connectorSkin : connectorSkins) {

                final boolean isInput = connectorSkin.getConnector().getType().contains("input");
                final boolean isOutput = connectorSkin.getConnector().getType().contains("output");

                if (isInput) {
                    inputConnectorSkins.add(connectorSkin);
                } else if (isOutput) {
                    outputConnectorSkins.add(connectorSkin);
                }

                if (isInput || isOutput) {
                    getRoot().getChildren().add(connectorSkin.getRoot());
                }
            }
        }
        layoutConnectors();
    }

    private void removeAllConnectors() {

        for (final GConnectorSkin connectorSkin : inputConnectorSkins) {
            getRoot().getChildren().remove(connectorSkin.getRoot());
        }

        for (final GConnectorSkin connectorSkin : outputConnectorSkins) {
            getRoot().getChildren().remove(connectorSkin.getRoot());
        }
    }
    @Override
    public void layoutConnectors() {
        layoutLeftAndRightConnectors();

    }

    private void layoutLeftAndRightConnectors() {

        final int inputCount = inputConnectorSkins.size();
        final double inputOffsetY = (getRoot().getHeight()) / (inputCount + 1);

        for (int i = 0; i < inputCount; i++) {

            final GConnectorSkin inputSkin = inputConnectorSkins.get(i);
            final Node connectorRoot = inputSkin.getRoot();

            final double layoutX = GeometryUtils.moveOnPixel(0 - inputSkin.getWidth() / 2);
            final double layoutY = GeometryUtils.moveOnPixel((i + 1) * inputOffsetY - inputSkin.getHeight() / 2);

            connectorRoot.setLayoutX(layoutX);
            connectorRoot.setLayoutY(layoutY);
        }

        final int outputCount = outputConnectorSkins.size();
        final double outputOffsetY = (getRoot().getHeight()) / (outputCount + 1);

        for (int i = 0; i < outputCount; i++) {

            final GConnectorSkin outputSkin = outputConnectorSkins.get(i);
            final Node connectorRoot = outputSkin.getRoot();

            final double layoutX = GeometryUtils.moveOnPixel(getRoot().getWidth() - outputSkin.getWidth() / 2);
            final double layoutY = GeometryUtils.moveOnPixel((i + 1) * outputOffsetY - outputSkin.getHeight() / 2);

            connectorRoot.setLayoutX(layoutX);
            connectorRoot.setLayoutY(layoutY);
        }
    }

    @Override
    public Point2D getConnectorPosition(GConnectorSkin connectorSkin) {
        final Node connectorRoot = connectorSkin.getRoot();

        final double x = connectorRoot.getLayoutX() + connectorSkin.getWidth() / 2;
        final double y = connectorRoot.getLayoutY() + connectorSkin.getHeight() / 2;

        if (inputConnectorSkins.contains(connectorSkin)) {
            return new Point2D(x, y);
        } else {
            // Subtract 1 to align start-of-connection correctly. Compensation for rounding errors?
            return new Point2D(x - 1, y);
        }
    }
}
