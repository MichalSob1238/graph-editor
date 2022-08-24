package cause.effect.chain.editor.controller.modes;

import cause.effect.chain.editor.model.CauseEffectChainModel;
import cause.effect.chain.editor.model.skins.statemachine.StateMachineConnectorSkin;
import cause.effect.chain.editor.model.skins.statemachine.StateMachineConstants;
import cause.effect.chain.editor.model.skins.statemachine.StateMachineTailSkin;
import de.tesis.dynaware.grapheditor.GraphEditorContainer;
import de.tesis.dynaware.grapheditor.core.skins.defaults.utils.DefaultConnectorTypes;
import de.tesis.dynaware.grapheditor.demo.customskins.SkinController;
import cause.effect.chain.editor.model.skins.statemachine.StateMachineNodeSkin;
import cause.effect.chain.editor.model.skins.statemachine.StateMachineConnectionSkin;
import de.tesis.dynaware.grapheditor.model.*;
import javafx.geometry.Side;
import javafx.scene.control.ChoiceDialog;
import org.eclipse.emf.ecore.EReference;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class StateMachineModeController implements SkinController {

    private final CauseEffectChainModel graphEditor;
    private final GraphEditorContainer graphEditorContainer;
    private final ChoiceDialog dialog;
    private final List<String> dialogData = Arrays.asList(StateMachineConstants.ROOT_CAUSE, StateMachineConstants.INTERMEDIATE_DISADVANTAGE, StateMachineConstants.TARGET_DISADVANTAGE);


    private static final EReference CONNECTIONS = GraphPackage.Literals.GMODEL__CONNECTIONS;
    private static final EReference CONNECTOR_CONNECTIONS = GraphPackage.Literals.GCONNECTOR__CONNECTIONS;

    public StateMachineModeController(final CauseEffectChainModel graphEditor, final GraphEditorContainer graphEditorContainer) {
        this.graphEditor = graphEditor;
        this.graphEditorContainer = graphEditorContainer;

        graphEditor.setNodeSkin(StateMachineConstants.STATE_MACHINE_NODE, StateMachineNodeSkin.class);
        graphEditor.setConnectorSkin(StateMachineConstants.STATE_MACHINE_LEFT_INPUT_CONNECTOR, StateMachineConnectorSkin.class);
        graphEditor.setConnectorSkin(StateMachineConstants.STATE_MACHINE_RIGHT_INPUT_CONNECTOR, StateMachineConnectorSkin.class);
        graphEditor.setConnectorSkin(StateMachineConstants.STATE_MACHINE_BOTTOM_INPUT_CONNECTOR, StateMachineConnectorSkin.class);
        graphEditor.setConnectorSkin(StateMachineConstants.STATE_MACHINE_TOP_INPUT_CONNECTOR, StateMachineConnectorSkin.class);
        graphEditor.setConnectorSkin(StateMachineConstants.STATE_MACHINE_RIGHT_OUTPUT_CONNECTOR, StateMachineConnectorSkin.class);
        graphEditor.setConnectorSkin(StateMachineConstants.STATE_MACHINE_LEFT_OUTPUT_CONNECTOR, StateMachineConnectorSkin.class);
        graphEditor.setConnectorSkin(StateMachineConstants.STATE_MACHINE_TOP_OUTPUT_CONNECTOR, StateMachineConnectorSkin.class);
        graphEditor.setConnectorSkin(StateMachineConstants.STATE_MACHINE_BOTTOM_OUTPUT_CONNECTOR, StateMachineConnectorSkin.class);
        graphEditor.setConnectionSkin(StateMachineConstants.STATE_MACHINE_CONNECTION, StateMachineConnectionSkin.class);
        graphEditor.setTailSkin(StateMachineConstants.STATE_MACHINE_LEFT_INPUT_CONNECTOR, StateMachineTailSkin.class);
        graphEditor.setTailSkin(StateMachineConstants.STATE_MACHINE_TOP_INPUT_CONNECTOR, StateMachineTailSkin.class);
        graphEditor.setTailSkin(StateMachineConstants.STATE_MACHINE_RIGHT_INPUT_CONNECTOR, StateMachineTailSkin.class);
        graphEditor.setTailSkin(StateMachineConstants.STATE_MACHINE_BOTTOM_INPUT_CONNECTOR, StateMachineTailSkin.class);
        graphEditor.setTailSkin(StateMachineConstants.STATE_MACHINE_RIGHT_OUTPUT_CONNECTOR, StateMachineTailSkin.class);
        graphEditor.setTailSkin(StateMachineConstants.STATE_MACHINE_BOTTOM_OUTPUT_CONNECTOR, StateMachineTailSkin.class);
        graphEditor.setTailSkin(StateMachineConstants.STATE_MACHINE_TOP_OUTPUT_CONNECTOR, StateMachineTailSkin.class);
        graphEditor.setTailSkin(StateMachineConstants.STATE_MACHINE_LEFT_OUTPUT_CONNECTOR, StateMachineTailSkin.class);
        ////System.out.println("CREATING SM CONTROLLER");

        dialog = new ChoiceDialog(dialogData.get(0), dialogData);
        dialog.setTitle("Add a node");
        dialog.setHeaderText("Select your choice");
    }

    @Override
    public void addNode(double currentZoomFactor) {
        ////System.out.println("called add node");
        final double windowXOffset = graphEditorContainer.windowXProperty().get() / currentZoomFactor;
        final double windowYOffset = graphEditorContainer.windowYProperty().get() / currentZoomFactor;
        Optional<String> result = dialog.showAndWait();
        String selected;

        if (result.isPresent()) {
            selected = result.get();
            graphEditor.addStateMachineNode(windowXOffset+10, windowYOffset +10, "description", selected);
        }
    }

    public GNode addNode(double X, double Y, String description) {
        ////System.out.println("called add using coords node");

        return graphEditor.addStateMachineNode(X, Y, description);
    }

    @Override
    public void addConnector(final Side position, final boolean input) {

        final String type = getType(position, input);
        ////System.out.println("connector type:" + type);

        graphEditor.addStateMachineConnector(type);
    }

    public GConnector addConnector(GNode node, String type) {

        final GConnector connector = graphEditor.addStateMachineConnector(node, type);
        ////System.out.println("connector type: with type sm " + type);
        return connector;
    }

    public GConnection addStateMachineConnection(GConnector source, GConnector target, String description) {
        final GConnection connection = graphEditor.addStateMachineConnection(source, target, description);
        return connection;
    }

    /**
     * Gets the connector type string corresponding to the given position and input values.
     *
     * @param position a {@link Side} value
     * @param input {@code true} for input, {@code false} for output
     * @return the connector type corresponding to these values
     */
    //TODO: add methods not reliant on demo gui
    private String getType(final Side position, final boolean input) {

        switch (position) {
            case TOP:
                if (input) {
                    return DefaultConnectorTypes.TOP_INPUT;
                } else {
                    return DefaultConnectorTypes.TOP_OUTPUT;
                }
            case RIGHT:
                if (input) {
                    return DefaultConnectorTypes.RIGHT_INPUT;
                } else {
                    return DefaultConnectorTypes.RIGHT_OUTPUT;
                }
            case BOTTOM:
                if (input) {
                    return DefaultConnectorTypes.BOTTOM_INPUT;
                } else {
                    return DefaultConnectorTypes.BOTTOM_OUTPUT;
                }
            case LEFT:
                if (input) {
                    return DefaultConnectorTypes.LEFT_INPUT;
                } else {
                    return DefaultConnectorTypes.LEFT_OUTPUT;
                }
        }

        return null;
    }
    @Override
    public void clearConnectors() {
        graphEditor.clearConnectors();;
    }

    @Override
    public void handlePaste() {
        graphEditor.handlePaste();
    }

    @Override
    public void handleSelectAll() {
        graphEditor.handleSelectAll();
    }

    @Override
    public void addAndGate(double currentZoomFactor) {

    }

    @Override
    public void addOrGate(double currentZoomFactor) {

    }

}

