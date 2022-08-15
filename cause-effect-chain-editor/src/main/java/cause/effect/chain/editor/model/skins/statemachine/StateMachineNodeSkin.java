package cause.effect.chain.editor.model.skins.statemachine;

import cause.effect.chain.editor.model.CauseEffectChainModel;
import com.jfoenix.controls.JFXTextField;
import de.tesis.dynaware.grapheditor.Commands;
import de.tesis.dynaware.grapheditor.GConnectorSkin;
import de.tesis.dynaware.grapheditor.GNodeSkin;
import de.tesis.dynaware.grapheditor.core.connections.ConnectionCommands;
import de.tesis.dynaware.grapheditor.core.skins.defaults.utils.DefaultConnectorTypes;
import de.tesis.dynaware.grapheditor.demo.customskins.NodeTraversalUtils;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private static final EReference CONNECTIONS = GraphPackage.Literals.GMODEL__CONNECTIONS;
    private static final EReference CONNECTOR_CONNECTIONS = GraphPackage.Literals.GCONNECTOR__CONNECTIONS;

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
        ////System.out.println("setting title");
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

                showNodeInformation();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Node Status");
                alert.setHeaderText("Node has the following issues that prevent the model from being legal");

                Label label1 = new Label("Name: ");
               TextField text1 = new TextField();
                TextField text2 = new TextField();

                GridPane grid = new GridPane();
                grid.add(label1, 1, 1);
                grid.add(text1, 2, 1);
                grid.add(text2, 2, 2);
                alert.getDialogPane().setContent(grid);
                ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                alert.getDialogPane().getButtonTypes().add(buttonTypeOk);


                ////System.out.println("State Machine handling doubleclick");
                ////System.out.println(getNode());
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

    private void showNodeInformation() {

        GridPane grid = new GridPane();
        Label label1 = new Label("Description: ");
        TextField text1 = new TextField();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);
        Button avoid = new Button("avoid");
        Button warn = new Button("warn");
        Button counteract = new Button("Counteract");
        warn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Stage stage = (Stage) warn.getScene().getWindow();
                stage.close();
                handleWarnButton();
            }
        });

        avoid.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                final GNode node = GraphFactory.eINSTANCE.createGNode();
                node.setType(de.tesis.dynaware.grapheditor.demo.customskins.state.machine.StateMachineConstants.STATE_MACHINE_NODE);
                node.setY(getRoot().getLayoutY() + 50);

                node.setX(getRoot().getLayoutX() + getRoot().getWidth() + 10);
                node.setId(allocateNewId());

                final GConnector input = GraphFactory.eINSTANCE.createGConnector();
                input.setType(de.tesis.dynaware.grapheditor.demo.customskins.state.machine.StateMachineConstants.STATE_MACHINE_LEFT_INPUT_CONNECTOR);
                node.getConnectors().add(input);
                final GConnector output = GraphFactory.eINSTANCE.createGConnector();
                output.setType(de.tesis.dynaware.grapheditor.demo.customskins.state.machine.StateMachineConstants.STATE_MACHINE_RIGHT_OUTPUT_CONNECTOR);
                getNode().getConnectors().add(output);

                node.setDescription("avoid state description");

                Commands.addNode(getGraphEditor().getModel(), node);
                ////System.out.println("created avoid node" + node);
                ////System.out.println("alert node connectors: + " + node.getConnectors() );

                addStateMachineConnection(output,input, "avoidance");

            }
        });

        counteract.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                final GNode node = GraphFactory.eINSTANCE.createGNode();
                node.setType(de.tesis.dynaware.grapheditor.demo.customskins.state.machine.StateMachineConstants.STATE_MACHINE_NODE);
                node.setY(getRoot().getLayoutY() + 50);

                node.setX(getRoot().getLayoutX());
                node.setId(allocateNewId());

                final GConnector input = GraphFactory.eINSTANCE.createGConnector();
                input.setType(de.tesis.dynaware.grapheditor.demo.customskins.state.machine.StateMachineConstants.STATE_MACHINE_BOTTOM_INPUT_CONNECTOR);
                node.getConnectors().add(input);
                final GConnector output = GraphFactory.eINSTANCE.createGConnector();
                output.setType(de.tesis.dynaware.grapheditor.demo.customskins.state.machine.StateMachineConstants.STATE_MACHINE_BOTTOM_OUTPUT_CONNECTOR);
                node.getConnectors().add(output);


                final GConnector rootOutput = GraphFactory.eINSTANCE.createGConnector();
                rootOutput.setType(de.tesis.dynaware.grapheditor.demo.customskins.state.machine.StateMachineConstants.STATE_MACHINE_TOP_OUTPUT_CONNECTOR);
                getNode().getConnectors().add(rootOutput);
                final GConnector rootInput = GraphFactory.eINSTANCE.createGConnector();
                rootInput.setType(de.tesis.dynaware.grapheditor.demo.customskins.state.machine.StateMachineConstants.STATE_MACHINE_TOP_INPUT_CONNECTOR);
                getNode().getConnectors().add(rootInput);
                node.setDescription("avoid state description");

                Commands.addNode(getGraphEditor().getModel(), node);
                ////System.out.println("created counteract node" + node);
                ////System.out.println("alert node connectors: + " + node.getConnectors() );

                addStateMachineConnection(output, rootInput,"conteract");
                addStateMachineConnection(rootOutput,input, "conteract");
            }
        });

        grid.add(avoid,1,2);
        grid.add(warn,2,2);
        grid.add(counteract,3,2);

        Scene scene = new Scene(grid);
        Stage stage = new Stage();
        stage.setScene(scene);

        stage.showAndWait();
    }

    private void handleWarnButton() {
        List<GConnection> inputConnections = getNode().getConnectors().stream()
                .filter(NodeTraversalUtils::isInput)
                .map(GConnector::getConnections)
                .map(con -> con.get(0))
                .collect(Collectors.toList());

        List<String> descriptions = inputConnections.stream().map(GConnection::getDescription).collect(Collectors.toList());

        ChoiceDialog dialog = new ChoiceDialog(descriptions.get(0),descriptions);
        dialog.setTitle("Chose which transition to warn of");
        dialog.setHeaderText("Select your choice");
        Optional<String> result = dialog.showAndWait();
        String selected = descriptions.get(0);

        if (result.isPresent()) {

            selected = result.get();
        }
        ////System.out.println("connection selected: " + selected);
        int i = 0;
        while (i < inputConnections.size()) {
            GConnection selectedCon = inputConnections.get(i);
            if (Objects.equals(selectedCon.getDescription(), selected))
            {
                addAlertState(selected, selectedCon);
                break;
            }
            ++i;
        }
    }

    private void addAlertState(String selected, GConnection selectedCon) {
        final GNode node = GraphFactory.eINSTANCE.createGNode();
        node.setType(de.tesis.dynaware.grapheditor.demo.customskins.state.machine.StateMachineConstants.STATE_MACHINE_NODE);
        node.setY(getRoot().getLayoutY());

        node.setX(getRoot().getLayoutX() - getRoot().getWidth() - 10);
        node.setId(allocateNewId());

        final GConnector input = GraphFactory.eINSTANCE.createGConnector();
        input.setType(de.tesis.dynaware.grapheditor.demo.customskins.state.machine.StateMachineConstants.STATE_MACHINE_LEFT_INPUT_CONNECTOR);
        node.getConnectors().add(input);

        final GConnector output = GraphFactory.eINSTANCE.createGConnector();
        output.setType(de.tesis.dynaware.grapheditor.demo.customskins.state.machine.StateMachineConstants.STATE_MACHINE_RIGHT_OUTPUT_CONNECTOR);
        node.getConnectors().add(output);


        node.setDescription("alert state description");

        Commands.addNode(getGraphEditor().getModel(), node);
        ////System.out.println("created alert node" + node);
        ////System.out.println("alert node connectors: + " + node.getConnectors() );

        GConnector sourceConnector = selectedCon.getSource();
        GConnector targetConnector = selectedCon.getTarget();

        ConnectionCommands.removeConnection(getGraphEditor().getModel(), selectedCon);

        addStateMachineConnection(output,targetConnector, selected);
        addStateMachineConnection(sourceConnector,input, selected);
        return;
    }

    private String allocateNewId() {

        final List<GNode> nodes = getGraphEditor().getModel().getNodes();
        final OptionalInt max = nodes.stream().mapToInt(node -> Integer.parseInt(node.getId())).max();

        if (max.isPresent()) {
            return Integer.toString(max.getAsInt() + 1);
        } else {
            return "1";
        }
    }

    public GConnector addStateMachineConnector(GNode node, String type) {
        final GConnector connector = GraphFactory.eINSTANCE.createGConnector();
        connector.setType(type);
        final GModel model = getGraphEditor().getModel();
        final CompoundCommand command = new CompoundCommand();
        final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(model);
        final EReference connectors = GraphPackage.Literals.GCONNECTABLE__CONNECTORS;
        command.append(AddCommand.create(editingDomain, node, connectors, connector));
        if (command.canExecute()) {
            editingDomain.getCommandStack().execute(command);
        }
        return connector;
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

    public GConnection addStateMachineConnection(GConnector source, GConnector target, String description) {
        final GConnection connection = GraphFactory.eINSTANCE.createGConnection();

        connection.setType(de.tesis.dynaware.grapheditor.demo.customskins.state.machine.StateMachineConstants.STATE_MACHINE_CONNECTION);
        connection.setSource(source);
        connection.setTarget(target);
        connection.setDescription(description);

        source.getConnections().add(connection);

        // Set the rest of the values via EMF commands because they touch the currently-edited model.
        GModel model = getGraphEditor().getModel();
        final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(model);
        final CompoundCommand command = new CompoundCommand();

        command.append(AddCommand.create(editingDomain, model, CONNECTIONS, connection));
        command.append(AddCommand.create(editingDomain, target, CONNECTOR_CONNECTIONS, connection));
        //command.append(RemoveCommand.create(editingDomain, model, CONNECTOR, connector));

        if (command.canExecute()) {
            editingDomain.getCommandStack().execute(command);
        }

        return connection;
    }

}
