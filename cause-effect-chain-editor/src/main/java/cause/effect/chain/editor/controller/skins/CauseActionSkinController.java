package cause.effect.chain.editor.controller.skins;

import de.tesis.dynaware.grapheditor.Commands;
import de.tesis.dynaware.grapheditor.GraphEditor;
import de.tesis.dynaware.grapheditor.GraphEditorContainer;
import de.tesis.dynaware.grapheditor.SkinLookup;
import de.tesis.dynaware.grapheditor.core.connections.ConnectionCommands;
import de.tesis.dynaware.grapheditor.core.skins.defaults.utils.DefaultConnectorTypes;
import de.tesis.dynaware.grapheditor.demo.customskins.SkinController;
import de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram.CecaDiagramConnectorSkin;
import de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram.CecaDiagramConstants;
import de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram.CecaDiagramGateSkin;
import de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram.CecaDiagramNodeSkin;
import de.tesis.dynaware.grapheditor.model.*;
import javafx.geometry.Side;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class CauseActionSkinController implements SkinController {

    private final GraphEditor graphEditor;
    private final GraphEditorContainer graphEditorContainer;
    private static final EReference CONNECTIONS = GraphPackage.Literals.GMODEL__CONNECTIONS;
    private static final EReference CONNECTOR_CONNECTIONS = GraphPackage.Literals.GCONNECTOR__CONNECTIONS;

    public CauseActionSkinController(final GraphEditor graphEditor, final GraphEditorContainer graphEditorContainer) {
        this.graphEditor = graphEditor;
        this.graphEditorContainer = graphEditorContainer;

        graphEditor.setNodeSkin(CecaDiagramConstants.CECA_NODE, CecaDiagramNodeSkin.class);
        graphEditor.setNodeSkin(CecaDiagramConstants.GATE_NODE, CecaDiagramGateSkin.class);
        graphEditor.setConnectorSkin(CecaDiagramConstants.DIAGRAM_INPUT_CONNECTOR, CecaDiagramConnectorSkin.class);
        graphEditor.setConnectorSkin(CecaDiagramConstants.DIAGRAM_OUTPUT_CONNECTOR, CecaDiagramConnectorSkin.class);
    }

    @Override
    public void addNode(double currentZoomFactor) {
        System.out.println("called add node");
        final double windowXOffset = graphEditorContainer.windowXProperty().get() / currentZoomFactor;
        final double windowYOffset = graphEditorContainer.windowYProperty().get() / currentZoomFactor;

        final GNode node = GraphFactory.eINSTANCE.createGNode();
        node.setY(10 + windowYOffset);

        node.setX(10 + windowXOffset);
        node.setId(allocateNewId());

        final GConnector input = GraphFactory.eINSTANCE.createGConnector();
        node.getConnectors().add(input);
        input.setType(DefaultConnectorTypes.LEFT_INPUT);

        final GConnector output = GraphFactory.eINSTANCE.createGConnector();
        node.getConnectors().add(output);
        output.setType(DefaultConnectorTypes.RIGHT_OUTPUT);

        node.setType(CecaDiagramConstants.CECA_NODE);
        node.setDescription("DESCRIPTION!");

        Commands.addNode(graphEditor.getModel(), node);
    }

    public GNode addNode(double X, double Y, String description) {
        System.out.println("called add using coords node");

        final GNode node = GraphFactory.eINSTANCE.createGNode();
        node.setY(Y);

        node.setX(X);
        node.setId(allocateNewId());

        final GConnector input = GraphFactory.eINSTANCE.createGConnector();
        node.getConnectors().add(input);
        input.setType(DefaultConnectorTypes.LEFT_INPUT);

        final GConnector output = GraphFactory.eINSTANCE.createGConnector();
        node.getConnectors().add(output);
        output.setType(DefaultConnectorTypes.RIGHT_OUTPUT);

        node.setType(CecaDiagramConstants.CECA_NODE);
        node.setDescription(description);

        Commands.addNode(graphEditor.getModel(), node);

        return node;
    }

    @Override
    public void addConnector(final Side position, final boolean input) {

        final String type = getType(position, input);

        final GModel model = graphEditor.getModel();
        final SkinLookup skinLookup = graphEditor.getSkinLookup();
        final CompoundCommand command = new CompoundCommand();
        final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(model);

        for (final GNode node : model.getNodes()) {

            if (skinLookup.lookupNode(node).isSelected()) {
                final GConnector connector = GraphFactory.eINSTANCE.createGConnector();
                connector.setType(type);

                final EReference connectors = GraphPackage.Literals.GCONNECTABLE__CONNECTORS;
                command.append(AddCommand.create(editingDomain, node, connectors, connector));
            }
        }

        if (command.canExecute()) {
            editingDomain.getCommandStack().execute(command);
        }
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
        Commands.clearConnectors(graphEditor.getModel(), graphEditor.getSelectionManager().getSelectedNodes());
    }

    @Override
    public void handlePaste() {
        graphEditor.getSelectionManager().paste();
    }

    @Override
    public void handleSelectAll() {
        graphEditor.getSelectionManager().selectAllNodes();
        graphEditor.getSelectionManager().selectAllJoints();
    }

    @Override
    public void addAndGate(double currentZoomFactor) {
        System.out.println("called and gate");
        final double windowXOffset = graphEditorContainer.windowXProperty().get() / currentZoomFactor;
        final double windowYOffset = graphEditorContainer.windowYProperty().get() / currentZoomFactor;

        final GNode node = GraphFactory.eINSTANCE.createGNode();
        node.setY(10 + windowYOffset);

        node.setX(10 + windowXOffset);
        node.setId(allocateNewId());

        final GConnector input = GraphFactory.eINSTANCE.createGConnector();
        node.getConnectors().add(input);
        input.setType(DefaultConnectorTypes.LEFT_INPUT);

        final GConnector input2 = GraphFactory.eINSTANCE.createGConnector();
        node.getConnectors().add(input2);
        input2.setType(DefaultConnectorTypes.LEFT_INPUT);

        final GConnector output = GraphFactory.eINSTANCE.createGConnector();
        node.getConnectors().add(output);
        output.setType(DefaultConnectorTypes.RIGHT_OUTPUT);

        node.setType(CecaDiagramConstants.GATE_NODE);
        node.setSubtype("and");
        node.setDescription("DESCRIPTION2!");

        Commands.addNode(graphEditor.getModel(), node);
    }

    public GNode addAndGate(double X, double Y) {
        System.out.println("called and gate");

        final GNode node = GraphFactory.eINSTANCE.createGNode();
        node.setY(Y);
        node.setX(X);
        node.setId(allocateNewId());

        final GConnector output = GraphFactory.eINSTANCE.createGConnector();
        node.getConnectors().add(output);
        output.setType(DefaultConnectorTypes.RIGHT_OUTPUT);

        node.setType(CecaDiagramConstants.GATE_NODE);
        node.setSubtype("and");
        node.setDescription("DESCRIPTION2!");

        Commands.addNode(graphEditor.getModel(), node);
        return node;
    }

    @Override
    public void addOrGate(double currentZoomFactor) {
        System.out.println("called or gate");
        final double windowXOffset = graphEditorContainer.windowXProperty().get() / currentZoomFactor;
        final double windowYOffset = graphEditorContainer.windowYProperty().get() / currentZoomFactor;

        final GNode node = GraphFactory.eINSTANCE.createGNode();
        node.setY(10 + windowYOffset);

        node.setX(10 + windowXOffset);
        node.setId(allocateNewId());

        final GConnector input = GraphFactory.eINSTANCE.createGConnector();
        node.getConnectors().add(input);
        input.setType(DefaultConnectorTypes.LEFT_INPUT);

        final GConnector input2 = GraphFactory.eINSTANCE.createGConnector();
        node.getConnectors().add(input2);
        input2.setType(DefaultConnectorTypes.LEFT_INPUT);

        final GConnector output = GraphFactory.eINSTANCE.createGConnector();
        node.getConnectors().add(output);
        output.setType(DefaultConnectorTypes.RIGHT_OUTPUT);

        node.setType(CecaDiagramConstants.GATE_NODE);
        node.setSubtype("or");
        node.setDescription("DESCRIPTION3!");

        Commands.addNode(graphEditor.getModel(), node);
    }

    public GNode addOrGate(double X, double Y, int inputs) {
        System.out.println("called or gate");

        final GNode node = GraphFactory.eINSTANCE.createGNode();
        node.setY(Y);

        node.setX(X);
        node.setId(allocateNewId());
        for (int i= 0; i < inputs; i++) {
            final GConnector input = GraphFactory.eINSTANCE.createGConnector();
            node.getConnectors().add(input);
            input.setType(DefaultConnectorTypes.LEFT_INPUT);
        }

        final GConnector output = GraphFactory.eINSTANCE.createGConnector();
        node.getConnectors().add(output);
        output.setType(DefaultConnectorTypes.RIGHT_OUTPUT);

        node.setType(CecaDiagramConstants.GATE_NODE);
        node.setSubtype("or");
        node.setDescription("DESCRIPTION3!");

        Commands.addNode(graphEditor.getModel(), node);
        return node;
    }

    private String allocateNewId() {

        final List<GNode> nodes = graphEditor.getModel().getNodes();
        final OptionalInt max = nodes.stream().mapToInt(node -> Integer.parseInt(node.getId())).max();

        if (max.isPresent()) {
            return Integer.toString(max.getAsInt() + 1);
        } else {
            return "1";
        }
    }

    public void addConnection(GConnector source, GConnector target) {
        // Set the rest of the values via EMF commands because they touch the currently-edited model.
        GModel model = graphEditor.getModel();
        final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(model);
        final CompoundCommand command = new CompoundCommand();

        final List<GJoint> joints = new ArrayList<>();

        final GJoint joint = GraphFactory.eINSTANCE.createGJoint();
        joint.setX(((((GNode) target.getParent()).getX() + ((GNode) source.getParent()).getX())/2.0) + ((GNode) source.getParent()).getWidth() );
        joint.setY(((GNode) source.getParent()).getY());
        joints.add(joint);
        final GJoint joint2 = GraphFactory.eINSTANCE.createGJoint();
        joint2.setX((((GNode) target.getParent()).getX()+ ((GNode) source.getParent()).getX())/2.0);
        joint2.setY(((GNode) target.getParent()).getX());
        joints.add(joint2);
        System.out.println("JOINTS " + joints);
        ConnectionCommands.addConnection(model, source, target, null, joints);

        if (command.canExecute()) {
            editingDomain.getCommandStack().execute(command);
        }

    }
}
