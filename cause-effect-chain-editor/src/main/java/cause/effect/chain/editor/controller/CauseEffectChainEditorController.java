package cause.effect.chain.editor.controller;

import cause.effect.chain.editor.controller.modes.CauseActionModeController;
import cause.effect.chain.editor.controller.modes.StateMachineModeController;
import cause.effect.chain.editor.controller.transformations.ModelTransformationController;
import cause.effect.chain.editor.model.CauseEffectChainModel;
import de.tesis.dynaware.grapheditor.*;
import de.tesis.dynaware.grapheditor.core.skins.defaults.connection.SimpleConnectionSkin;
import cause.effect.chain.editor.controller.coherency.CoherencyChecker;
import de.tesis.dynaware.grapheditor.demo.GraphEditorPersistence;
import de.tesis.dynaware.grapheditor.demo.customskins.SkinController;
import de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram.CecaDiagramConstants;
import de.tesis.dynaware.grapheditor.demo.customskins.state.machine.StateMachineConnectorValidator;
import de.tesis.dynaware.grapheditor.demo.utils.AwesomeIcon;
import de.tesis.dynaware.grapheditor.model.GNode;
import de.tesis.dynaware.grapheditor.model.impl.GModelImpl;
import de.tesis.dynaware.grapheditor.window.WindowPosition;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import java.util.Map;

public class CauseEffectChainEditorController {

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

    private final CauseEffectChainModel chainModel = new CauseEffectChainModel();
    private final GraphEditorPersistence graphEditorPersistence = new GraphEditorPersistence();

    private Scale scaleTransform;
    private double currentZoomFactor = 1;

    private CauseActionModeController cecaSkinController;
    private StateMachineModeController stateMachineController;
    public CoherencyChecker coherencyChecker;

    private ModelTransformationController modelTransformationController;
    public static CauseEffectChainEditorController instance;

    private final ObjectProperty<SkinController> activeSkinController = new SimpleObjectProperty<>();

    public CauseEffectChainEditorController() {
        if (instance == null) {
            instance = this;
        }
    }


    /**
     * Called by JavaFX when FXML is loaded.
     */
    public void initialize() {

        graphEditorContainer.setGraphEditor(chainModel.getGraphEditor());
        setDetouredStyle();

        cecaSkinController = new CauseActionModeController(chainModel, graphEditorContainer);
        stateMachineController = new StateMachineModeController(chainModel, graphEditorContainer);
        coherencyChecker = new CoherencyChecker(chainModel.getGraphEditor());
        modelTransformationController = new ModelTransformationController(cecaSkinController, stateMachineController, chainModel.getGraphEditor());

        activeSkinController.set(cecaSkinController);

        initializeMenuBar();
        addActiveSkinControllerListener();
        chainModel.getGraphEditor().setOnConnectionCreated((connection, command) -> {
            //System.out.println("connection added" + connection);
            coherencyChecker.getNotified(connection);
        });
        chainModel.getGraphEditor().setOnConnectionRemoved((connection, command) -> {
            //System.out.println("connection removed" + connection);
            coherencyChecker.getNotified(connection);
        });
        graphEditorContainer.setOnMouseClicked(event -> graphEditorContainer.requestFocus());
    }

    @FXML
    public void load() {
        graphEditorPersistence.loadFromFile(chainModel.getGraphEditor());
        checkSkinType();
    }

    @FXML
    public void save() {
        graphEditorPersistence.saveToFile(chainModel.getGraphEditor());
    }

    @FXML
    public void clearAll() {
        Commands.clear(chainModel.getGraphEditor().getModel());
    }

    @FXML
    public void exit() {
        Platform.exit();
    }

    @FXML
    public void undo() {
        //System.out.println("AAA + " + chainModel.getGraphEditor().getModel().getConnections());
        //System.out.println("AAA + " + ((GModelImpl) chainModel.getGraphEditor().getModel()).connections);
        Commands.undo(chainModel.getGraphEditor().getModel());
        chainModel.getGraphEditor().getModel().getNodes().forEach(coherencyChecker::updateCorrectnesStatus);
    }

    @FXML
    public void redo() {
        Commands.redo(chainModel.getGraphEditor().getModel());
        chainModel.getGraphEditor().getModel().getNodes().forEach(coherencyChecker::updateCorrectnesStatus);
    }

    @FXML
    public void cut() {
        chainModel.getGraphEditor().getSelectionManager().cut();
    }

    @FXML
    public void copy() {
        chainModel.getGraphEditor().getSelectionManager().copy();
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
        chainModel.getGraphEditor().getSelectionManager().deleteSelection();
    }

    @FXML
    public void addNode() {
        activeSkinController.get().addNode(currentZoomFactor);
    }


    @FXML
    public void addOrGate() {
        activeSkinController.get().addOrGate(currentZoomFactor);
    }

    @FXML
    public void transformIntoDiagram() {
        activeSkinController.set(cecaSkinController);
        modelTransformationController.transformIntoCauseActionDiagram();
        flushCommandStack();
    }

    @FXML
    public void transformIntoStateMachine() {
        setStateMachineSkin();
        modelTransformationController.transformIntoStateMachine();
        flushCommandStack();
//        ArrayList<GNode> list = new ArrayList<>(chainModel.getGraphEditor().getModel().getNodes());
//        chainModel.getGraphEditor().getSkinManager().swapNodeSkin(list);
//        chainModel.getGraphEditor().reload();
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
    public void setCecaSkin() {
        activeSkinController.set(cecaSkinController);
    }

    @FXML
    public void setStateMachineSkin() {
        activeSkinController.set(stateMachineController);
    }

    @FXML
    public void setGappedStyle() {

        chainModel.getGraphEditor().getProperties().getCustomProperties().remove(SimpleConnectionSkin.SHOW_DETOURS_KEY);
        chainModel.getGraphEditor().reload();
    }

    @FXML
    public void setDetouredStyle() {

        final Map<String, String> customProperties = chainModel.getGraphEditor().getProperties().getCustomProperties();
        customProperties.put(SimpleConnectionSkin.SHOW_DETOURS_KEY, Boolean.toString(true));
        chainModel.getGraphEditor().reload();
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

        chainModel.getGraphEditor().getView().getTransforms().add(scaleTransform);

        final ToggleGroup skinGroup = new ToggleGroup();
        skinGroup.getToggles().addAll(cecaSkinButton, stateMachineSkinButton);

        final ToggleGroup connectionStyleGroup = new ToggleGroup();
        connectionStyleGroup.getToggles().addAll(gappedStyleButton, detouredStyleButton);

        final ToggleGroup connectorTypeGroup = new ToggleGroup();
        connectorTypeGroup.getToggles().addAll(inputConnectorTypeButton, outputConnectorTypeButton);

        final ToggleGroup positionGroup = new ToggleGroup();
        positionGroup.getToggles().addAll(leftConnectorPositionButton, rightConnectorPositionButton);
        positionGroup.getToggles().addAll(topConnectorPositionButton, bottomConnectorPositionButton);

        chainModel.getGraphEditor().getProperties().gridVisibleProperty().bind(showGridButton.selectedProperty());
        chainModel.getGraphEditor().getProperties().snapToGridProperty().bind(snapToGridButton.selectedProperty());

        minimapButton.setGraphic(AwesomeIcon.MAP.node());

        initializeZoomOptions();

        final ListChangeListener<? super GNode> selectedNodesListener = change -> checkConnectorButtonsToDisable();

        chainModel.getGraphEditor().getSelectionManager().getSelectedNodes().addListener(selectedNodesListener);
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
        activeSkinController.addListener((observable, oldValue, newValue) -> handleActiveSkinControllerChange());
    }

    /**
     * Enables & disables certain menu options and sets CSS classes based on the new skin type that was set active.
     */
    private void handleActiveSkinControllerChange() {

        if (stateMachineController.equals(activeSkinController.get())) {
            chainModel.getGraphEditor().setConnectorValidator(new StateMachineConnectorValidator());
            chainModel.getGraphEditor().getSelectionManager().setConnectionSelectionPredicate(null);
            stateMachineSkinButton.setSelected(true);

        } else {
            chainModel.getGraphEditor().setConnectorValidator(null);
            chainModel.getGraphEditor().getSelectionManager().setConnectionSelectionPredicate(null);
            cecaSkinButton.setSelected(true);
        }

        //clearAll();
        flushCommandStack();
        checkConnectorButtonsToDisable();
        chainModel.getGraphEditor().getSelectionManager().clearMemory();
    }

    /**
     * Crudely inspects the model's first node and sets the new skin type accordingly.
     */
    private void checkSkinType() {

        if (!chainModel.getGraphEditor().getModel().getNodes().isEmpty()) {

            final GNode firstNode = chainModel.getGraphEditor().getModel().getNodes().get(0);
            final String type = firstNode.getType();

            if (CecaDiagramConstants.CECA_NODE.equals(type)) {
                activeSkinController.set(cecaSkinController);
            } else {
                activeSkinController.set(stateMachineController);
            }
        }
    }

    /**
     * Checks if the connector buttons need disabling (e.g. because no nodes are selected).
     */
    private void checkConnectorButtonsToDisable() {

        final boolean nothingSelected = chainModel.getGraphEditor().getSelectionManager().getSelectedNodes().isEmpty();

        if (nothingSelected) {
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

    }

    /**
     * Flushes the command stack, so that the undo/redo history is cleared.
     */
    public void flushCommandStack() {

        final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(chainModel.getGraphEditor().getModel());
        if (editingDomain != null) {
            editingDomain.getCommandStack().flush();
        }
    }

    public CauseEffectChainModel getModel() {
        return this.chainModel;
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
