/*
 * Copyright (C) 2005 - 2014 by TESIS DYNAware GmbH
 */
package de.tesis.dynaware.grapheditor.demo;

import de.tesis.dynaware.grapheditor.Commands;
import de.tesis.dynaware.grapheditor.GraphEditor;
import de.tesis.dynaware.grapheditor.GraphEditorContainer;
import de.tesis.dynaware.grapheditor.core.DefaultGraphEditor;
import de.tesis.dynaware.grapheditor.core.skins.defaults.connection.SimpleConnectionSkin;
import de.tesis.dynaware.grapheditor.core.skins.defaults.utils.DefaultConnectorTypes;
import de.tesis.dynaware.grapheditor.demo.CoherencyChecker.CoherencyChecker;
import de.tesis.dynaware.grapheditor.demo.customskins.*;
import de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram.CecaDiagramConstants;
import de.tesis.dynaware.grapheditor.demo.customskins.state.machine.StateMachineConnectorValidator;
import de.tesis.dynaware.grapheditor.demo.customskins.state.machine.StateMachineConstants;
import de.tesis.dynaware.grapheditor.demo.customskins.titled.TitledSkinConstants;
import de.tesis.dynaware.grapheditor.demo.customskins.tree.TreeConnectionSelectionPredicate;
import de.tesis.dynaware.grapheditor.demo.customskins.tree.TreeConnectorValidator;
import de.tesis.dynaware.grapheditor.demo.customskins.tree.TreeSkinConstants;
import de.tesis.dynaware.grapheditor.demo.utils.AwesomeIcon;
import de.tesis.dynaware.grapheditor.model.*;
import de.tesis.dynaware.grapheditor.model.impl.GModelImpl;
import de.tesis.dynaware.grapheditor.window.WindowPosition;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for the {@link GraphEditorDemo} application.
 */
public class GraphEditorDemoController {

    private static final String STYLE_CLASS_TITLED_SKINS = "titled-skins";
    private static final EReference NODES = GraphPackage.Literals.GMODEL__NODES;
    private static final EReference NODE_CONNECTORS = GraphPackage.Literals.GCONNECTABLE__CONNECTORS;

    @FXML
    private AnchorPane root;
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem addConnectorButton;
    @FXML
    private MenuItem clearConnectorsButton;
    @FXML
    private Menu connectorTypeMenu;
    @FXML
    private Menu connectorPositionMenu;
    @FXML
    private RadioMenuItem inputConnectorTypeButton;
    @FXML
    private RadioMenuItem outputConnectorTypeButton;
    @FXML
    private RadioMenuItem leftConnectorPositionButton;
    @FXML
    private RadioMenuItem rightConnectorPositionButton;
    @FXML
    private RadioMenuItem topConnectorPositionButton;
    @FXML
    private RadioMenuItem bottomConnectorPositionButton;
    @FXML
    private RadioMenuItem showGridButton;
    @FXML
    private RadioMenuItem snapToGridButton;
    @FXML
    private RadioMenuItem defaultSkinButton;
    @FXML
    private RadioMenuItem treeSkinButton;
    @FXML
    private RadioMenuItem titledSkinButton;
    @FXML
    private RadioMenuItem cecaSkinButton;
    @FXML
    private RadioMenuItem stateMachineSkinButton;
    @FXML
    private Menu intersectionStyle;
    @FXML
    private RadioMenuItem gappedStyleButton;
    @FXML
    private RadioMenuItem detouredStyleButton;
    @FXML
    private Menu zoomOptions;
    @FXML
    private ToggleButton minimapButton;
    @FXML
    private GraphEditorContainer graphEditorContainer;

    private final GraphEditor graphEditor = new DefaultGraphEditor();
    private final GraphEditorPersistence graphEditorPersistence = new GraphEditorPersistence();

    private Scale scaleTransform;
    private double currentZoomFactor = 1;

    private DefaultSkinController defaultSkinController;
    private TreeSkinController treeSkinController;
    private TitledSkinController titledSkinController;
    private CecaDiagramSkinController cecaSkinController;
    private StateMachineController stateMachineController;
    private CoherencyChecker coherencyChecker;


    private final ObjectProperty<SkinController> activeSkinController = new SimpleObjectProperty<>();

    /**
     * Called by JavaFX when FXML is loaded.
     */
    public void initialize() {

        final GModel model = GraphFactory.eINSTANCE.createGModel();

        graphEditor.setModel(model);
        graphEditorContainer.setGraphEditor(graphEditor);
        setDetouredStyle();

        defaultSkinController = new DefaultSkinController(graphEditor, graphEditorContainer);
        treeSkinController = new TreeSkinController(graphEditor, graphEditorContainer);
        titledSkinController = new TitledSkinController(graphEditor, graphEditorContainer);
        cecaSkinController = new CecaDiagramSkinController(graphEditor, graphEditorContainer);
        stateMachineController = new StateMachineController(graphEditor, graphEditorContainer);

        activeSkinController.set(defaultSkinController);

        initializeMenuBar();
        addActiveSkinControllerListener();
        graphEditor.setOnConnectionCreated((connection, command) -> {
            System.out.println("connection added" + connection);
        });
        graphEditor.setOnConnectionRemoved((connection, command) -> {
            System.out.println("connection removed" + connection);
        });

    }

    @FXML
    public void load() {
        graphEditorPersistence.loadFromFile(graphEditor);
        checkSkinType();
    }

    @FXML
    public void loadSample() {
        defaultSkinButton.setSelected(true);
        setDefaultSkin();
        graphEditorPersistence.loadSample(graphEditor);
    }

    @FXML
    public void loadSampleLarge() {
        defaultSkinButton.setSelected(true);
        setDefaultSkin();
        graphEditorPersistence.loadSampleLarge(graphEditor);
    }

    @FXML
    public void loadTree() {
        treeSkinButton.setSelected(true);
        setTreeSkin();
        graphEditorPersistence.loadTree(graphEditor);
    }

    @FXML
    public void loadTitled() {
        titledSkinButton.setSelected(true);
        setTitledSkin();
        graphEditorPersistence.loadTitled(graphEditor);
    }

    @FXML
    public void save() {
        graphEditorPersistence.saveToFile(graphEditor);
    }

    @FXML
    public void clearAll() {
        Commands.clear(graphEditor.getModel());
    }

    @FXML
    public void exit() {
        Platform.exit();
    }

    @FXML
    public void undo() {
        System.out.println("AAA + " + graphEditor.getModel().getConnections());
        System.out.println("AAA + " + ((GModelImpl) graphEditor.getModel()).connections);
        Commands.undo(graphEditor.getModel());
    }

    @FXML
    public void redo() {
        Commands.redo(graphEditor.getModel());
    }

    @FXML
    public void cut() {
        graphEditor.getSelectionManager().cut();
    }

    @FXML
    public void copy() {
        graphEditor.getSelectionManager().copy();
    }

    @FXML
    public void paste() {
        activeSkinController.get().handlePaste();
    }

    @FXML
    public void selectAll() {
        activeSkinController.get().handleSelectAll();
    }

    @FXML
    public void deleteSelection() {
        graphEditor.getSelectionManager().deleteSelection();
    }

    @FXML
    public void addNode() {
        activeSkinController.get().addNode(currentZoomFactor);
    }


    @FXML
    public void addOrGate() {
        activeSkinController.get().addOrGate(currentZoomFactor);
    }

    private static final double CHILD_X_OFFSET = 80;

    @FXML
    public void transformIntoDiagram() throws InterruptedException {
        graphEditor.getSelectionManager().clearSelection();
        List<GNode> rootNodes = get3Nodes();

        System.out.println("found " + rootNodes.size() + " root nodes");
        activeSkinController.set(cecaSkinController);
        rootNodes.forEach(this::beginTransformationIntoDiagram);
        deleteSelection();
        for (GNode node : graphEditor.getModel().getNodes()) {
            List<GConnector> toremove =  node.getConnectors().stream().filter(connector -> connector.getConnections().isEmpty()).collect(Collectors.toList());
            final CompoundCommand command = new CompoundCommand();
            final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(graphEditor.getModel());
            command.append(RemoveCommand.create(editingDomain, node, NODE_CONNECTORS, toremove));
            if (command.canExecute()) {
                editingDomain.getCommandStack().execute(command);
            }
        }
        for (GNode node : graphEditor.getModel().getNodes()) {
            if(node.getId().equals("5")){
                node.getConnectors().forEach(connector -> {
                    System.out.println("Connector : " + connector);
                    System.out.println("It's Connections: " + connector.getConnections().get(0));
                    System.out.println("That connection's source: + " + connector.getConnections().get(0).getSource());
                    System.out.println("That connection's Target: + " + connector.getConnections().get(0).getTarget());
                });
            }
        }
        graphEditor.reload();
    }

//    private void observeConnections() {
//        graphEditor.getModel().getConnections()
//    }


    private List<GNode> get3Nodes() {
        return graphEditor.getModel().getNodes().stream()
                .filter(node -> node.getDescription().equals("3"))
                .collect(Collectors.toList());
    }

    public void beginTransformationIntoDiagram(GNode rootNode) {
        System.out.println("Called begin transform into diagram on node " + rootNode);
        List<GConnector> inputConnectors = rootNode.getConnectors().stream()
                .filter(conector -> isInput(conector.getType()))
                .filter(connector -> !connector.getConnections().isEmpty())
                .collect(Collectors.toList());

        System.out.println("found " + inputConnectors.size() + " input connectors");

        if (inputConnectors.isEmpty()) {
            System.out.println("fucked up, somehow " + rootNode);
            return;
        }

        if (inputConnectors.size() == 1) {
            System.out.println("1");
            GNode predecessorNode = (GNode) inputConnectors.get(0).getConnections().get(0).getSource().getParent();
            String description = inputConnectors.get(0).getConnections().get(0).getDescription();
            procesPredecesorNode(rootNode, predecessorNode, description);
            GConnection conectionToDetele = inputConnectors.get(0).getConnections().get(0);
            graphEditor.getSkinLookup().lookupConnection(conectionToDetele).setSelected(true);
        } else {
            System.out.println("2");

            List<GConnection> orConnections = new ArrayList<>();
            HashMap<List<String>, List<GConnection>> connectionsMap = new HashMap<>();
            inputConnectors.forEach(connector -> {
                GConnection connection = connector.getConnections().get(0);
                String description = connection.getDescription();
                if (description.contains("&")) {
                    List<String> conditions = Arrays.stream(description.split("&")).map(String::trim).collect(Collectors.toList());
                    System.out.println("conditions + " + conditions);

                    if (connectionsMap.containsKey(conditions)) {
                        connectionsMap.get(conditions).add(connection);
                        System.out.println("was previous");
                    } else {
                        System.out.println("adding new");
                        List<GConnection> storedConenctions = new ArrayList<>();
                        storedConenctions.add(connection);
                        connectionsMap.put(conditions, storedConenctions);
                    }
                } else {
                    orConnections.add(connection);
                }
                graphEditor.getSkinLookup().lookupConnection(connection).setSelected(true);
            });

            connectionsMap.forEach((conditions, connections) -> {
                if (connections.size() < 2) {
                    throw new RuntimeException("insufficient connections for condition: " + conditions);
                }
                if (conditions.size() != connections.size()) {
                    throw new RuntimeException("mismatch between # of connections and # of conditions for condition: " + conditions);
                }
                GNode andGate = cecaSkinController.addAndGate(1500, 1500);
                final GConnector rootNodeInput = stateMachineController.addConnector(rootNode, DefaultConnectorTypes.LEFT_INPUT);
                cecaSkinController.addConnection(andGate.getConnectors().stream().filter(con -> !isInput(con.getType())).collect(Collectors.toList()).get(0), rootNodeInput);
                for (int i = 0; i < conditions.size(); i++) {
                    GConnection connection = connections.get(i);
                    GNode predecessorNode = (GNode) connection.getSource().getParent();
                    procesPredecesorNode(andGate, predecessorNode, conditions.get(i));
                    graphEditor.getSkinLookup().lookupConnection(connection).setSelected(true);
                }
            });

            switch (orConnections.size()) {
                case 0:
                    break;
                case 1: {
                    GConnection connection = orConnections.get(0);
                    String description = connection.getDescription();
                    GNode predecessorNode = (GNode) connection.getSource().getParent();
                    procesPredecesorNode(rootNode, predecessorNode, description);
                    graphEditor.getSkinLookup().lookupConnection(connection).setSelected(true);
                    break;
                }
                default: {
                    GNode orGate = cecaSkinController.addOrGate(1200, 1200, inputConnectors.size());
                    final GConnector rootNodeInput = stateMachineController.addConnector(rootNode, DefaultConnectorTypes.LEFT_INPUT);
                    cecaSkinController.addConnection(orGate.getConnectors().stream().filter(con -> !isInput(con.getType())).collect(Collectors.toList()).get(0), rootNodeInput);
                    orConnections.forEach(connection -> {
                        String description = connection.getDescription();
                        GNode predecessorNode = (GNode) connection.getSource().getParent();
                        procesPredecesorNode(orGate, predecessorNode, description);
                        graphEditor.getSkinLookup().lookupConnection(connection).setSelected(true);
                    });
                    break;
                }

            }

        }

    }

    private void procesPredecesorNode(GNode rootNode, GNode predecessorNode, String description) {
        System.out.println("predecessorNode + " + predecessorNode);
        final GNode actionNode = cecaSkinController.addNode((predecessorNode.getX() + rootNode.getX()) / 2.0, (predecessorNode.getY() + rootNode.getY()) / 2.0, description);

        actionNode.setDescription(description);

        final GModel model = graphEditor.getModel();
        //TODO: limit X so it doest go out of bounds

        final GConnector actionNodeInput = actionNode.getConnectors().stream().filter(connector -> isInput(connector.getType())).collect(Collectors.toList()).get(0);
        final GConnector actionNodeOutput = actionNode.getConnectors().stream().filter(connector -> !isInput(connector.getType())).collect(Collectors.toList()).get(0);


        final GConnector output = stateMachineController.addConnector(predecessorNode, DefaultConnectorTypes.RIGHT_OUTPUT);
        final GConnector input = stateMachineController.addConnector(rootNode, DefaultConnectorTypes.LEFT_INPUT);

        cecaSkinController.addConnection(output, actionNodeInput );
        cecaSkinController.addConnection(actionNodeOutput, input);

    }

    @FXML
    public void transformIntoStateMachine() {
        graphEditor.getSelectionManager().clearSelection();
        List<GNode> rootNodes = get3Nodes();

        System.out.println("found " + rootNodes.size() + " root nodes");

        activeSkinController.set(stateMachineController);

        rootNodes.forEach(this::beginTransformationIntoStateMachine);
        deleteSelection();
        for (GNode node : graphEditor.getModel().getNodes()) {
            System.out.println("NODE: " + node + '\n' + node.getConnectors());
            node.getConnectors().removeIf(connector -> connector.getConnections().isEmpty());
        }
        for (GNode node : graphEditor.getModel().getNodes()) {
            System.out.println("NODE: " + node + '\n' + node.getConnectors());
        }
        graphEditor.reload();


    }

    private static final EReference CONNECTIONS = GraphPackage.Literals.GMODEL__CONNECTIONS;
    private static final EReference CONNECTOR_CONNECTIONS = GraphPackage.Literals.GCONNECTOR__CONNECTIONS;
    private static final EReference CONNECTOR = GraphPackage.Literals.GCONNECTABLE__CONNECTORS;

    private void beginTransformationIntoStateMachine(GNode rootNode) {
        System.out.println("Called begin transform on node " + rootNode);
        List<GConnector> inputConnectors = rootNode.getConnectors().stream()
                .filter(conector -> isInput(conector.getType()))
                .filter(connector -> !connector.getConnections().isEmpty())
                .collect(Collectors.toList());

        System.out.println("found " + inputConnectors.size() + " input connectors");

        if (inputConnectors.isEmpty()) {
            System.out.println("fucked up, somehow " + rootNode);
            return;
        }
        //TRUE STEP
        inputConnectors.forEach(connector -> {
            GNode actionOrGateNode = (GNode) connector.getConnections().get(0).getSource().getParent();
            System.out.println("Node identified as previous: " + actionOrGateNode + "!!!");
            List<GConnector> actionOrGateInputs = actionOrGateNode.getConnectors().stream()
                    .filter(actionOrGateConector -> isInput(actionOrGateConector.getType()))
                    .filter(actionOrGateConector -> !actionOrGateConector.getConnections().isEmpty())
                    .collect(Collectors.toList());

            if (actionOrGateInputs.isEmpty()) {
                System.out.println("found floating action or gate: " + actionOrGateNode);
                return;
            }
            String description = "";
            //TODO:GATE LOGIC HERE
            if (actionOrGateNode.getType().equals(CecaDiagramConstants.GATE_NODE)) {
                if (actionOrGateNode.getSubtype().equals("or")) {
                    List<GNode> nodePredecessors = actionOrGateInputs.stream().map(orConnector -> ((GNode) orConnector.getConnections().get(0).getSource().getParent())).collect(Collectors.toList());
                    nodePredecessors.forEach(predNode -> onActionProcess(rootNode, predNode, predNode.getDescription()));
                    graphEditor.getSkinLookup().lookupNode(actionOrGateNode).setSelected(true);
                }
                if (actionOrGateNode.getSubtype().equals("and")) {
                    ArrayList<String> descriptions = new ArrayList<String>();
                    List<GNode> nodePredecessors = actionOrGateInputs.stream().map(orConnector -> ((GNode) orConnector.getConnections().get(0).getSource().getParent())).collect(Collectors.toList());
                    nodePredecessors.forEach(predNode -> descriptions.add(predNode.getDescription()));
                    nodePredecessors.forEach(predNode -> onActionProcess(rootNode, predNode, String.join(" & ", descriptions)));
                    graphEditor.getSkinLookup().lookupNode(actionOrGateNode).setSelected(true);
                }
            } else {
                description = actionOrGateNode.getDescription();
                onActionProcess(rootNode, actionOrGateNode, description);
            }
        });

    }


    private void onActionProcess(GNode rootNode, GNode actionNode, String description) {
        //TODO: action with more than one inputs
        System.out.println("caleld onActionProcess");
        graphEditor.getSkinLookup().lookupNode(actionNode).setSelected(true);
        List<GConnector> actuionInputs = actionNode.getConnectors().stream()
                .filter(actionConnector -> isInput(actionConnector.getType()))
                .filter(actionConnector -> !actionConnector.getConnections().isEmpty())
                .collect(Collectors.toList());

        actuionInputs.forEach(trueInput -> {
            GConnector predecessorSourceConnector = trueInput.getConnections().get(0).getSource();
            GNode predecessorState = (GNode) predecessorSourceConnector.getParent();

            List<GConnector> predecessorStateInputs = predecessorState.getConnectors().stream()
                    .filter(predecessorStateConector -> isInput(predecessorStateConector.getType()))
                    .filter(predecessorStateConector -> !predecessorStateConector.getConnections().isEmpty())
                    .collect(Collectors.toList());


            System.out.println("found edge state: " + predecessorState);
            beginTransformationIntoStateMachine(predecessorState);

            final GConnector output = stateMachineController.addConnector(predecessorState, StateMachineConstants.STATE_MACHINE_RIGHT_OUTPUT_CONNECTOR);

            final GConnector input = stateMachineController.addConnector(rootNode, StateMachineConstants.STATE_MACHINE_LEFT_INPUT_CONNECTOR);

            stateMachineController.addStateMachineConnection(output, input, description);

            System.out.println("new edge state: " + predecessorState);
        });


    }

    public boolean isInput(String type) {
        return type.contains("input");
    }

    @FXML
    void addAndGate() {
        activeSkinController.get().addAndGate(currentZoomFactor);
    }

    @FXML
    public void addConnector() {
        activeSkinController.get().addConnector(getSelectedConnectorPosition(), inputConnectorTypeButton.isSelected());
    }

    @FXML
    public void clearConnectors() {
        activeSkinController.get().clearConnectors();
    }

    @FXML
    public void setDefaultSkin() {
        activeSkinController.set(defaultSkinController);
    }

    @FXML
    public void setTreeSkin() {
        activeSkinController.set(treeSkinController);
    }

    @FXML
    public void setTitledSkin() {
        activeSkinController.set(titledSkinController);
    }

    @FXML
    public void setCecaSkin() {
        activeSkinController.set(cecaSkinController);
    }

    @FXML
    public void setStateMachineSkin() {
        activeSkinController.set(stateMachineController);
    }

    @FXML
    public void setGappedStyle() {

        graphEditor.getProperties().getCustomProperties().remove(SimpleConnectionSkin.SHOW_DETOURS_KEY);
        graphEditor.reload();
    }

    @FXML
    public void setDetouredStyle() {

        final Map<String, String> customProperties = graphEditor.getProperties().getCustomProperties();
        customProperties.put(SimpleConnectionSkin.SHOW_DETOURS_KEY, Boolean.toString(true));
        graphEditor.reload();
    }

    @FXML
    public void toggleMinimap() {
        graphEditorContainer.getMinimap().visibleProperty().bind(minimapButton.selectedProperty());
    }

    /**
     * Pans the graph editor container to place the window over the center of the content.
     *
     * <p>
     * Only works after the scene has been drawn, when getWidth() & getHeight() return non-zero values.
     * </p>
     */
    public void panToCenter() {
        graphEditorContainer.panTo(WindowPosition.CENTER);
    }

    /**
     * Initializes the menu bar.
     */
    private void initializeMenuBar() {

        scaleTransform = new Scale(currentZoomFactor, currentZoomFactor, 0, 0);
        scaleTransform.yProperty().bind(scaleTransform.xProperty());

        graphEditor.getView().getTransforms().add(scaleTransform);

        final ToggleGroup skinGroup = new ToggleGroup();
        skinGroup.getToggles().addAll(defaultSkinButton, treeSkinButton, titledSkinButton, cecaSkinButton, stateMachineSkinButton);

        final ToggleGroup connectionStyleGroup = new ToggleGroup();
        connectionStyleGroup.getToggles().addAll(gappedStyleButton, detouredStyleButton);

        final ToggleGroup connectorTypeGroup = new ToggleGroup();
        connectorTypeGroup.getToggles().addAll(inputConnectorTypeButton, outputConnectorTypeButton);

        final ToggleGroup positionGroup = new ToggleGroup();
        positionGroup.getToggles().addAll(leftConnectorPositionButton, rightConnectorPositionButton);
        positionGroup.getToggles().addAll(topConnectorPositionButton, bottomConnectorPositionButton);

        graphEditor.getProperties().gridVisibleProperty().bind(showGridButton.selectedProperty());
        graphEditor.getProperties().snapToGridProperty().bind(snapToGridButton.selectedProperty());

        minimapButton.setGraphic(AwesomeIcon.MAP.node());

        initializeZoomOptions();

        final ListChangeListener<? super GNode> selectedNodesListener = change -> {
            checkConnectorButtonsToDisable();
        };

        graphEditor.getSelectionManager().getSelectedNodes().addListener(selectedNodesListener);
        checkConnectorButtonsToDisable();
    }

    /**
     * Initializes the list of zoom options.
     */
    private void initializeZoomOptions() {

        final ToggleGroup toggleGroup = new ToggleGroup();

        for (int i = 1; i <= 5; i++) {

            final RadioMenuItem zoomOption = new RadioMenuItem();
            final double zoomFactor = i;

            zoomOption.setText(i + "00%");
            zoomOption.setOnAction(event -> setZoomFactor(zoomFactor));

            toggleGroup.getToggles().add(zoomOption);
            zoomOptions.getItems().add(zoomOption);

            if (i == 1) {
                zoomOption.setSelected(true);
            }
        }
    }

    /**
     * Sets a new zoom factor.
     *
     * <p>
     * Note that everything will look crap if the zoom factor is non-integer.
     * </p>
     *
     * @param zoomFactor the new zoom factor
     */
    private void setZoomFactor(final double zoomFactor) {

        final double zoomFactorRatio = zoomFactor / currentZoomFactor;

        final double currentCenterX = graphEditorContainer.windowXProperty().get();
        final double currentCenterY = graphEditorContainer.windowYProperty().get();

        if (zoomFactor != 1) {
            // Cache-while-panning is sometimes very sluggish when zoomed in.
            graphEditorContainer.setCacheWhilePanning(false);
        } else {
            graphEditorContainer.setCacheWhilePanning(true);
        }

        scaleTransform.setX(zoomFactor);
        graphEditorContainer.panTo(currentCenterX * zoomFactorRatio, currentCenterY * zoomFactorRatio);
        currentZoomFactor = zoomFactor;
    }

    /**
     * Adds a listener to make changes to available menu options when the skin type changes.
     */
    private void addActiveSkinControllerListener() {

        activeSkinController.addListener((observable, oldValue, newValue) -> {
            handleActiveSkinControllerChange();
        });
    }

    /**
     * Enables & disables certain menu options and sets CSS classes based on the new skin type that was set active.
     */
    private void handleActiveSkinControllerChange() {

        if (treeSkinController.equals(activeSkinController.get())) {

            graphEditor.setConnectorValidator(new TreeConnectorValidator());
            graphEditor.getSelectionManager().setConnectionSelectionPredicate(new TreeConnectionSelectionPredicate());
            graphEditor.getView().getStyleClass().remove(STYLE_CLASS_TITLED_SKINS);
            treeSkinButton.setSelected(true);

        } else if (titledSkinController.equals(activeSkinController.get())) {

            graphEditor.setConnectorValidator(null);
            graphEditor.getSelectionManager().setConnectionSelectionPredicate(null);
            if (!graphEditor.getView().getStyleClass().contains(STYLE_CLASS_TITLED_SKINS)) {
                graphEditor.getView().getStyleClass().add(STYLE_CLASS_TITLED_SKINS);
            }
            titledSkinButton.setSelected(true);

        } else if (cecaSkinController.equals(activeSkinController.get())) {

            graphEditor.setConnectorValidator(null);
            graphEditor.getSelectionManager().setConnectionSelectionPredicate(null);
            graphEditor.getView().getStyleClass().remove(STYLE_CLASS_TITLED_SKINS);
            cecaSkinButton.setSelected(true);

        } else if (stateMachineController.equals(activeSkinController.get())) {

            graphEditor.setConnectorValidator(new StateMachineConnectorValidator());
            graphEditor.getSelectionManager().setConnectionSelectionPredicate(null);
            graphEditor.getView().getStyleClass().remove(STYLE_CLASS_TITLED_SKINS);
            stateMachineSkinButton.setSelected(true);

        } else {

            graphEditor.setConnectorValidator(null);
            graphEditor.getSelectionManager().setConnectionSelectionPredicate(null);
            graphEditor.getView().getStyleClass().remove(STYLE_CLASS_TITLED_SKINS);
            defaultSkinButton.setSelected(true);
        }

        // Demo does not currently support mixing of skin types. Skins don't know how to cope with it.
        //clearAll();
        flushCommandStack();
        checkConnectorButtonsToDisable();
        graphEditor.getSelectionManager().clearMemory();
    }

    /**
     * Crudely inspects the model's first node and sets the new skin type accordingly.
     */
    private void checkSkinType() {

        if (!graphEditor.getModel().getNodes().isEmpty()) {

            final GNode firstNode = graphEditor.getModel().getNodes().get(0);
            final String type = firstNode.getType();

            if (TreeSkinConstants.TREE_NODE.equals(type)) {
                activeSkinController.set(treeSkinController);
            } else if (TitledSkinConstants.TITLED_NODE.equals(type)) {
                activeSkinController.set(titledSkinController);
            } else if (CecaDiagramConstants.CECA_NODE.equals(type)) {
                activeSkinController.set(defaultSkinController);
            } else {
                activeSkinController.set(defaultSkinController);
            }
        }
    }

    /**
     * Checks if the connector buttons need disabling (e.g. because no nodes are selected).
     */
    private void checkConnectorButtonsToDisable() {

        final boolean nothingSelected = graphEditor.getSelectionManager().getSelectedNodes().isEmpty();

        final boolean treeSkinActive = treeSkinController.equals(activeSkinController.get());
        final boolean titledSkinActive = titledSkinController.equals(activeSkinController.get());

        if (titledSkinActive || treeSkinActive) {
            addConnectorButton.setDisable(true);
            clearConnectorsButton.setDisable(true);
            connectorTypeMenu.setDisable(true);
            connectorPositionMenu.setDisable(true);
        } else if (nothingSelected) {
            addConnectorButton.setDisable(true);
            clearConnectorsButton.setDisable(true);
            connectorTypeMenu.setDisable(false);
            connectorPositionMenu.setDisable(false);
        } else if (cecaSkinController.equals(activeSkinController.get())) {
            addConnectorButton.setDisable(false);
            clearConnectorsButton.setDisable(false);
            connectorTypeMenu.setDisable(false);
            connectorPositionMenu.setDisable(false);
        } else {
            addConnectorButton.setDisable(false);
            clearConnectorsButton.setDisable(false);
            connectorTypeMenu.setDisable(false);
            connectorPositionMenu.setDisable(false);
        }

        intersectionStyle.setDisable(treeSkinActive);
    }

    /**
     * Flushes the command stack, so that the undo/redo history is cleared.
     */
    private void flushCommandStack() {

        final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(graphEditor.getModel());
        if (editingDomain != null) {
            editingDomain.getCommandStack().flush();
        }
    }

    /**
     * Gets the side corresponding to the currently selected connector position in the menu.
     *
     * @return the {@link Side} corresponding to the currently selected connector position
     */
    private Side getSelectedConnectorPosition() {

        if (leftConnectorPositionButton.isSelected()) {
            return Side.LEFT;
        } else if (rightConnectorPositionButton.isSelected()) {
            return Side.RIGHT;
        } else if (topConnectorPositionButton.isSelected()) {
            return Side.TOP;
        } else {
            return Side.BOTTOM;
        }
    }

}
