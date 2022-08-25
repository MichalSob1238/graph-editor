package cause.effect.chain.editor.controller;

import cause.effect.chain.editor.controller.modes.CauseActionModeController;
import cause.effect.chain.editor.controller.modes.StateMachineModeController;
import cause.effect.chain.editor.controller.transformations.ModelTransformationController;
import cause.effect.chain.editor.model.CauseEffectChainModel;
import cause.effect.chain.editor.model.skins.statemachine.StateMachineNodeSkin;
import de.tesis.dynaware.grapheditor.*;
import de.tesis.dynaware.grapheditor.core.skins.defaults.connection.SimpleConnectionSkin;
import cause.effect.chain.editor.controller.coherency.CoherencyChecker;
import de.tesis.dynaware.grapheditor.demo.GraphEditorPersistence;
import de.tesis.dynaware.grapheditor.demo.customskins.SkinController;
import de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram.CecaDiagramConstants;
import cause.effect.chain.editor.model.skins.statemachine.StateMachineConnectorValidator;
import de.tesis.dynaware.grapheditor.demo.utils.AwesomeIcon;
import de.tesis.dynaware.grapheditor.model.GNode;
import de.tesis.dynaware.grapheditor.window.WindowPosition;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CauseEffectChainEditorController {

    @FXML
    private AnchorPane root;
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem transformIntoCauseAction;
    @FXML
    private MenuItem transformIntoStateMachine;
    @FXML
    private RadioMenuItem showGridButton;
    @FXML
    private RadioMenuItem snapToGridButton;
    @FXML
    private MenuItem cecaSkinButton;
    @FXML
    private MenuItem stateMachineSkinButton;
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
            ////System.out.println("connection added" + connection);
            coherencyChecker.getNotified(connection);
        });
        chainModel.getGraphEditor().setOnConnectionRemoved((connection, command) -> {
            ////System.out.println("connection removed" + connection);
            coherencyChecker.getNotified(connection);
        });
        graphEditorContainer.setOnMouseClicked(event -> graphEditorContainer.requestFocus());
    }

    @FXML
    public void load() {
        graphEditorPersistence.loadFromFile(chainModel.getGraphEditor());
        checkSkinType();
        chainModel.getGraphEditor().getModel().getNodes().forEach(coherencyChecker::updateCorrectnesStatus);
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
        ////System.out.println("AAA + " + chainModel.getGraphEditor().getModel().getConnections());
        ////System.out.println("AAA + " + ((GModelImpl) chainModel.getGraphEditor().getModel()).connections);
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
        chainModel.getGraphEditor().getModel().getNodes().forEach(coherencyChecker::updateCorrectnesStatus);
    }

    @FXML
    public void copy() {
        chainModel.getGraphEditor().getSelectionManager().copy();
    }

    @FXML
    public void paste() {
        activeSkinController.get().handlePaste();
        chainModel.getGraphEditor().getModel().getNodes().forEach(coherencyChecker::updateCorrectnesStatus);
    }

    @FXML
    public void selectAll() {
        activeSkinController.get().handleSelectAll();
    }

    @FXML
    public void deleteSelection() {
        chainModel.getGraphEditor().getSelectionManager().deleteSelection();
        chainModel.getGraphEditor().getModel().getNodes().forEach(coherencyChecker::updateCorrectnesStatus);
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
        List<GNode> nodes = chainModel.getGraphEditor().getModel().getNodes();
        List<StateMachineNodeSkin> skins = nodes.stream().map(node -> (StateMachineNodeSkin) chainModel.getGraphEditor().getSkinLookup().lookupNode(node)).collect(Collectors.toList());
        for (StateMachineNodeSkin skin : skins) {
            if (!skin.isCorrect) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Cannot convert");
                alert.setHeaderText("Cannot convert");
                alert.setContentText("Conversion was impossible due to errors in the model. Please fix them and try again");
                alert.showAndWait();
                return ;
            }
        }
        activeSkinController.set(cecaSkinController);
        modelTransformationController.transformIntoCauseActionDiagram();
        chainModel.getGraphEditor().getModel().getNodes().forEach(coherencyChecker::updateCorrectnesStatus);
        flushCommandStack();
    }

    @FXML
    public void transformIntoStateMachine() {
        List<GNode> nodes = chainModel.getGraphEditor().getModel().getNodes();
        List<GNodeSkin> skins = nodes.stream().map(node -> chainModel.getGraphEditor().getSkinLookup().lookupNode(node)).collect(Collectors.toList());
        ////System.out.println("found " + skins.size() + skins);
        for (GNodeSkin skin : skins) {
            ////System.out.println("found " + skin + skin.isCorrect);
            if (!skin.isCorrect) {
                ////System.out.println("found incorrect skin: " + skin + skin.isCorrect);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Cannot convert");
                alert.setHeaderText("Cannot convert");
                alert.setContentText("Conversion was impossible due to errors in the model. Please fix them and try again");
                alert.showAndWait();
                return ;
            }

        }
        activeSkinController.set(stateMachineController);
        modelTransformationController.transformIntoStateMachine();
        chainModel.getGraphEditor().getModel().getNodes().forEach(coherencyChecker::updateCorrectnesStatus);
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
    public void setCecaSkin() {
        if (cecaSkinController.equals(activeSkinController.get())){
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("WARNING");
        alert.setHeaderText("Changing the mode of edited diagram");
        alert.setContentText("Changing the mode of the currently edited diagram will clean current screen. \n Proceed regardless?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get().equals(ButtonType.OK)){
                activeSkinController.set(cecaSkinController);
                clearAll();
            }
        }

    }

    @FXML
    public void setStateMachineSkinControler() {
        if (stateMachineController.equals(activeSkinController.get())){
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("WARNING");
        alert.setHeaderText("Changing the mode of edited diagram");
        alert.setContentText("Changing the mode of the currently edited diagram will clean current screen. \n Proceed regardless?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get().equals(ButtonType.OK)){
                activeSkinController.set(stateMachineController);
                clearAll();
            }
        }
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

        final ToggleGroup connectionStyleGroup = new ToggleGroup();
        connectionStyleGroup.getToggles().addAll(gappedStyleButton, detouredStyleButton);

        chainModel.getGraphEditor().getProperties().gridVisibleProperty().bind(showGridButton.selectedProperty());
        chainModel.getGraphEditor().getProperties().snapToGridProperty().bind(snapToGridButton.selectedProperty());

        minimapButton.setGraphic(AwesomeIcon.MAP.node());

        initializeZoomOptions();
        checkTransformToDisable();
    }

    /**
     * Initializes the list of zoom options.
     */
    private void initializeZoomOptions() {

        final ToggleGroup toggleGroup = new ToggleGroup();

        for (double i = 1; i <= 3; i=i+0.5) {

            final RadioMenuItem zoomOption = new RadioMenuItem();
            final double zoomFactor = 1.0/i;

            zoomOption.setText("x" + new BigDecimal(String.valueOf(1.0 / i)).setScale(3, BigDecimal.ROUND_HALF_UP));
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

        } else {
            chainModel.getGraphEditor().setConnectorValidator(null);
            chainModel.getGraphEditor().getSelectionManager().setConnectionSelectionPredicate(null);
        }

        flushCommandStack();
        checkTransformToDisable();
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

    private void checkTransformToDisable() {

         if (cecaSkinController.equals(activeSkinController.get())) {
             transformIntoCauseAction.setDisable(true);
             transformIntoStateMachine.setDisable(false);
        } else {
             transformIntoCauseAction.setDisable(false);
             transformIntoStateMachine.setDisable(true);
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


}
