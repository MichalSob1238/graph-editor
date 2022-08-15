package cause.effect.chain.editor.controller.modes;

import cause.effect.chain.editor.model.CauseEffectChainModel;
import de.tesis.dynaware.grapheditor.GraphEditorContainer;
import de.tesis.dynaware.grapheditor.core.skins.defaults.utils.DefaultConnectorTypes;
import de.tesis.dynaware.grapheditor.demo.customskins.SkinController;
import de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram.CecaDiagramConnectorSkin;
import cause.effect.chain.editor.model.skins.StateActionModel.CecaDiagramConstants;
import cause.effect.chain.editor.model.skins.StateActionModel.CecaDiagramGateSkin;
import cause.effect.chain.editor.model.skins.StateActionModel.CecaDiagramNodeSkin;
import de.tesis.dynaware.grapheditor.model.GConnector;
import de.tesis.dynaware.grapheditor.model.GNode;
import de.tesis.dynaware.grapheditor.model.GraphPackage;
import javafx.geometry.Side;
import javafx.scene.control.ChoiceDialog;
import org.eclipse.emf.ecore.EReference;

import java.util.*;

public class CauseActionModeController implements SkinController {

    private final CauseEffectChainModel graphEditor;
    private final GraphEditorContainer graphEditorContainer;
    private static final EReference CONNECTIONS = GraphPackage.Literals.GMODEL__CONNECTIONS;
    private static final EReference CONNECTOR_CONNECTIONS = GraphPackage.Literals.GCONNECTOR__CONNECTIONS;
    private final ChoiceDialog dialog;
    private final List<String> dialogData = Arrays.asList(CecaDiagramConstants.CAUSE_ACTION_ROOT, CecaDiagramConstants.ACTION, CecaDiagramConstants.CONDITION, CecaDiagramConstants.TARGET_DISADVANTAGE);
    public CauseActionModeController(final CauseEffectChainModel graphEditor, final GraphEditorContainer graphEditorContainer) {
        this.graphEditor = graphEditor;
        this.graphEditorContainer = graphEditorContainer;

        graphEditor.setNodeSkin(CecaDiagramConstants.CECA_NODE, CecaDiagramNodeSkin.class);
        graphEditor.setNodeSkin(CecaDiagramConstants.GATE_NODE, CecaDiagramGateSkin.class);
        graphEditor.setConnectorSkin(CecaDiagramConstants.DIAGRAM_INPUT_CONNECTOR, CecaDiagramConnectorSkin.class);
        graphEditor.setConnectorSkin(CecaDiagramConstants.DIAGRAM_OUTPUT_CONNECTOR, CecaDiagramConnectorSkin.class);

        dialog = new ChoiceDialog(dialogData.get(0), dialogData);
        dialog.setTitle("test");
        dialog.setHeaderText("Select your choice");

    }

    @Override
    public void addNode(double currentZoomFactor) {
        //System.out.println("called add node");
        final double windowXOffset = graphEditorContainer.windowXProperty().get() / currentZoomFactor;
        final double windowYOffset = graphEditorContainer.windowYProperty().get() / currentZoomFactor;

        Optional<String> result = dialog.showAndWait();
        String selected = "cancelled.";

        if (result.isPresent()) {

            selected = result.get();
        }
        //System.out.println("choice box test" + selected);
        graphEditor.addCauseActionNode(windowXOffset, windowYOffset, "DESCRIPTION!", selected);
    }

    public GNode addNode(double X, double Y, String description) {
        //System.out.println("called add using coords node");

        return graphEditor.addCauseActionNode(X, Y, description);
    }

    @Override
    public void addConnector(final Side position, final boolean input) {

        final String type = getType(position, input);
        graphEditor.addConditionActionConnector(type);

    }

    /**
     * Gets the connector type string corresponding to the given position and input values.
     *
     * @param position a {@link Side} value
     * @param input    {@code true} for input, {@code false} for output
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
        graphEditor.clearConnectors();
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
        //System.out.println("called and gate");
        final double windowXOffset = graphEditorContainer.windowXProperty().get() / currentZoomFactor;
        final double windowYOffset = graphEditorContainer.windowYProperty().get() / currentZoomFactor;

        graphEditor.addConditionActionAndGate(windowXOffset+10,windowYOffset+10);
    }

    public GNode addAndGate(double X, double Y) {
        //System.out.println("called and gate");

        return graphEditor.addConditionActionAndGate(X,Y);
    }

    @Override
    public void addOrGate(double currentZoomFactor) {
        //System.out.println("called or gate from zoom");
        final double windowXOffset = graphEditorContainer.windowXProperty().get() / currentZoomFactor;
        final double windowYOffset = graphEditorContainer.windowYProperty().get() / currentZoomFactor;

        graphEditor.addConditionActionOrGate(windowXOffset + 10, windowYOffset + 10, 2);
    }

    public GNode addOrGate(double X, double Y, int inputs) {
        //System.out.println("called or gate");

        final GNode node = graphEditor.addConditionActionOrGate(X, Y, inputs);
        return node;
    }

//    private String allocateNewId() {
//
//        final List<GNode> nodes = graphEditor.getModel().getNodes();
//        final OptionalInt max = nodes.stream().mapToInt(node -> Integer.parseInt(node.getId())).max();
//
//        if (max.isPresent()) {
//            return Integer.toString(max.getAsInt() + 1);
//        } else {
//            return "1";
//        }
//    }

    public void addConnection(GConnector source, GConnector target) {
        // Set the rest of the values via EMF commands because they touch the currently-edited model.
        graphEditor.addConditionActionConnection(source, target);
    }
}
