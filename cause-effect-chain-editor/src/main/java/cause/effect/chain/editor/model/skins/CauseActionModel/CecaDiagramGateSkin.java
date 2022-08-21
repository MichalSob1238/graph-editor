package cause.effect.chain.editor.model.skins.CauseActionModel;

import cause.effect.chain.editor.model.skins.CauseActionModel.gateutils.AndGateShape;
import cause.effect.chain.editor.model.skins.CauseActionModel.gateutils.OrGateShape;
import cause.effect.chain.editor.utils.NodeTraversalUtils;
import de.tesis.dynaware.grapheditor.GConnectorSkin;
import de.tesis.dynaware.grapheditor.GNodeSkin;
import de.tesis.dynaware.grapheditor.core.skins.defaults.utils.DefaultConnectorTypes;
import de.tesis.dynaware.grapheditor.model.*;
import de.tesis.dynaware.grapheditor.utils.GeometryUtils;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

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
    private final Rectangle errorHalo = new Rectangle();
    List<String> issuesWithNode = new ArrayList<>();


    public Tooltip tooltip = new Tooltip("");
    private static final String STYLE_CLASS_BACKGROUND = "and-node-background";
    private static final String STYLE_CLASS_SELECTION_HALO = "default-node-selection-halo";


    private static final double HALO_OFFSET = 5;
    private static final double HALO_CORNER_SIZE = 10;

    private static final double MINOR_POSITIVE_OFFSET = 2;
    private static final double MINOR_NEGATIVE_OFFSET = -3;

    private final String subtype = getNode().getSubtype();
    private EventHandler<? super MouseEvent> doubleClickedListener = getDoubleClickedListener();

    private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass("selected");

    private void showNodeInformation() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setResizable(false);

        alert.getDialogPane().setPrefSize(500, 300);
        alert.getDialogPane().setMinSize(500, 300);

        alert.setHeaderText("Information about the node:");

        Label nodeType = new Label("Node type: Gate node");
        Label nodeSubtype = new Label("Node subtype: " + getNode().getSubtype());



        GridPane grid = new GridPane();
       // grid.setPadding(new Insets(5, 5, 5, 60));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(nodeType, 0, 0, 1, 1);
        grid.add(nodeSubtype, 0, 1, 1, 1);
        if(!issuesWithNode.isEmpty()){
            Label issuesWithNodeLabel = new Label("Issues with node:");
            TextArea issuesText = new TextArea(String.join("\n", issuesWithNode));
            grid.add(issuesWithNodeLabel,0,2);
            grid.add(issuesText,0,3,2,2);
        }


        alert.getDialogPane().setContent(grid);
        alert.getDialogPane().resize(400, 400);
        //alert.getDialogPane().getContent().prefWidth();
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.LEFT);
        ButtonType addInputConnector = new ButtonType("Add inputs", ButtonBar.ButtonData.LEFT);
        ButtonType clearConnectors = new ButtonType("Clean inputs", ButtonBar.ButtonData.LEFT);

        alert.getDialogPane().getButtonTypes().add(buttonTypeOk);
        alert.getDialogPane().getButtonTypes().add(addInputConnector);
        alert.getDialogPane().getButtonTypes().add(clearConnectors);

        alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        final Button btOk = (Button) alert.getDialogPane().lookupButton(buttonTypeOk);
        final Button addInputConnectorBtn = (Button) alert.getDialogPane().lookupButton(addInputConnector);
        final Button clearConnectorsBtn = (Button) alert.getDialogPane().lookupButton(clearConnectors);


        addInputConnectorBtn.addEventFilter(
                ActionEvent.ACTION,
                action -> {
                    addConnector(getNode(), DefaultConnectorTypes.LEFT_INPUT);
                    action.consume();
                }
        );

        clearConnectorsBtn.addEventFilter(
                ActionEvent.ACTION,
                action -> {
                    List<GConnector> cons = getNode().getConnectors();
                    List<GConnector> consToRemove = new ArrayList<>();
                    int counter = 0;
                    for (GConnector conctr : cons) {
                        if (NodeTraversalUtils.isInput(conctr)){
                            counter++;
                            if( conctr.getConnections().isEmpty())
                            {
                                consToRemove.add(conctr);
                            }
                        }
                    }
                    while (!consToRemove.isEmpty() && counter > 2) {
                        getNode().getConnectors().remove(consToRemove.get(0));
                        consToRemove.remove(0);
                        counter--;
                    }
                    getGraphEditor().reload();
                    action.consume();
                }
        );

        //System.out.println("alert width: " + alert.getWidth());
        alert.setWidth(1200);
        //System.out.println("alert width: " + alert.getWidth());

        Optional<ButtonType> x = alert.showAndWait();

    }

    private void addConnector(GNode node, String leftInput) {
        final GConnector connector = GraphFactory.eINSTANCE.createGConnector();
        connector.setType(leftInput);
        final GModel model = getGraphEditor().getModel();
        final CompoundCommand command = new CompoundCommand();
        final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(model);
        final EReference connectors = GraphPackage.Literals.GCONNECTABLE__CONNECTORS;
        command.append(AddCommand.create(editingDomain, node, connectors, connector));
        if (command.canExecute()) {
            editingDomain.getCommandStack().execute(command);
        }
    }

    private EventHandler<MouseEvent> getDoubleClickedListener() {
        return event -> {
            if (event.getButton().compareTo(MouseButton.SECONDARY) == 0) {
                showNodeInformation();
            }
        };
    }

    private void addErrorHalo() {
        getRoot().getChildren().add(errorHalo);

        errorHalo.setManaged(false);
        errorHalo.setMouseTransparent(false);
        errorHalo.setVisible(false);

        errorHalo.setLayoutX(-HALO_OFFSET);
        errorHalo.setLayoutY(-HALO_OFFSET);
        errorHalo.getStyleClass().add("default-node-error-halo");
    }

    private void addInitialError() {
        String subtype = getNode().getSubtype();
        List<String> status = new ArrayList<>();
        switch (subtype) {
            case "and": {
                status.add("AND gate must have at least two inputs from condition nodes.");
                status.add("AND gate must have at least one output to an action or a target disadvantage.");
                break;
            }
            case "or": {
                status.add("OR gate must have at least two non-gate inputs.");
                status.add("OR gate must have at least one non-gate output.");
                break;
            }
        }
        updateStatus(status);
    }

    public void layoutErrorHalo() {
        if (errorHalo.isVisible()) {

            errorHalo.setWidth(getRoot().getWidth() + 2 * HALO_OFFSET);
            errorHalo.setHeight(getRoot().getHeight() + 2 * HALO_OFFSET);
            final double cornerLength = 2 * HALO_CORNER_SIZE;
            final double xGap = getRoot().getWidth() - 2 * HALO_CORNER_SIZE + 2 * HALO_OFFSET;
            final double yGap = getRoot().getHeight() - 2 * HALO_CORNER_SIZE + 2 * HALO_OFFSET;

            errorHalo.setStrokeDashOffset(-HALO_CORNER_SIZE);
            errorHalo.getStrokeDashArray().setAll(yGap, cornerLength, xGap, cornerLength);
        }
    }
    //TODO: customise
    public void setDescription() {
        ////System.out.println("setting description");
        final Text text = new Text(getNode().getDescription());
        new Scene(new Group(text));
        text.applyCss();
        final double width = Math.max(50, text.getLayoutBounds().getWidth());
        final double height = Math.max(20, text.getLayoutBounds().getHeight());

        Font font = new Font("Arial", 11);

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
        //description.setOnMouseClicked(doubleClickedListener);
        getRoot().getChildren().add(description);

        tooltip.setText("");
        getRoot().getChildren().forEach(mhm -> Tooltip.install(mhm, tooltip));
        getRoot().getChildren().forEach(mhm -> mhm.setOnMouseClicked(doubleClickedListener) );
        getNode().setHeight(90);
        getNode().setWidth(120);
        getRoot().setMinSize(50, 50);
        background.addEventFilter(MouseEvent.MOUSE_DRAGGED, this::filterMouseDragged);

        addSelectionHalo();
        addSelectionListener();
        addErrorHalo();
        addInitialError();

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
        layoutErrorHalo();

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
        if (status.isEmpty()) {
            //System.out.println("true");
            this.isCorrect = true;
            this.issuesWithNode.clear();
            errorHalo.setVisible(false);
            tooltip.setShowDelay(Duration.seconds(1000));

        } else {
            this.isCorrect = false;
            this.issuesWithNode.clear();
            this.issuesWithNode.addAll(status);
            errorHalo.setVisible(true);
            layoutErrorHalo();
            tooltip.setText(String.join("\n", issuesWithNode));
            tooltip.setShowDelay(Duration.seconds(1));

        }
        return issuesWithNode.size();
    }

    private void filterMouseDragged(final MouseEvent event) {
        if (event.isPrimaryButtonDown() && !isSelected()) {
            event.consume();
        }
    }
}
