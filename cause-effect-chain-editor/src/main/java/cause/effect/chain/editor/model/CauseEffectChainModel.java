package cause.effect.chain.editor.model;

import de.tesis.dynaware.grapheditor.*;
import de.tesis.dynaware.grapheditor.core.DefaultGraphEditor;
import de.tesis.dynaware.grapheditor.core.GraphEditorController;
import de.tesis.dynaware.grapheditor.core.connections.ConnectionCommands;
import de.tesis.dynaware.grapheditor.core.skins.defaults.utils.DefaultConnectorTypes;
import cause.effect.chain.editor.model.skins.CauseActionModel.CecaDiagramConstants;
import cause.effect.chain.editor.model.skins.statemachine.StateMachineConnectionSkin;
import cause.effect.chain.editor.model.skins.statemachine.StateMachineConstants;
import cause.effect.chain.editor.model.skins.statemachine.StateMachineTailSkin;
import de.tesis.dynaware.grapheditor.model.*;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class CauseEffectChainModel {
    private final GraphEditor graphEditor = new DefaultGraphEditor();
    private static final EReference CONNECTIONS = GraphPackage.Literals.GMODEL__CONNECTIONS;
    private static final EReference CONNECTOR_CONNECTIONS = GraphPackage.Literals.GCONNECTOR__CONNECTIONS;

    public CauseEffectChainModel() {
        final GModel model = GraphFactory.eINSTANCE.createGModel();

        graphEditor.setModel(model);
    }

    public DefaultGraphEditor getGraphEditor() {return  (DefaultGraphEditor) graphEditor;}

    public GraphEditorController getGraphEditorController() {
        return getGraphEditor().getController();
    }

    public void setNodeSkin(final String type, final Class<? extends GNodeSkin> skin) {
        graphEditor.setNodeSkin(type, skin);
    }

    public void setConnectorSkin(final String type, final Class<? extends GConnectorSkin> skin) {
        graphEditor.setConnectorSkin(type, skin);
    }

    public GNode addCauseActionNode(double X, double Y, String description) {

        final GNode node = GraphFactory.eINSTANCE.createGNode();
        node.setY(10 + Y);

        node.setX(10 + X);
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

    public GNode addStateMachineNode(double X, double Y, String description) {
        final GNode node = GraphFactory.eINSTANCE.createGNode();
        node.setType(StateMachineConstants.STATE_MACHINE_NODE);
        node.setY(Y);

        node.setX(X);
        node.setId(allocateNewId());

        final GConnector input = GraphFactory.eINSTANCE.createGConnector();
        node.getConnectors().add(input);
        input.setType(StateMachineConstants.STATE_MACHINE_LEFT_INPUT_CONNECTOR);

        final GConnector output = GraphFactory.eINSTANCE.createGConnector();
        node.getConnectors().add(output);
        output.setType(StateMachineConstants.STATE_MACHINE_RIGHT_OUTPUT_CONNECTOR);

        node.setDescription(description);

        Commands.addNode(graphEditor.getModel(), node);
        ////System.out.printlnprintln(node);
        return node;
    }

    public GNode addStateMachineNode(double X, double Y, String description, String selected) {
        final GNode node = GraphFactory.eINSTANCE.createGNode();
        node.setType(StateMachineConstants.STATE_MACHINE_NODE);
        node.setSubtype(selected);
        node.setY(Y);

        node.setX(X);
        node.setId(allocateNewId());

        addInitialConnectorsStateMachine(node);


        node.setDescription(description);

        Commands.addNode(graphEditor.getModel(), node);
        ////System.out.printlnprintln(node);
        return node;
    }

    private void addInitialConnectorsStateMachine(GNode node) {
        String subtype = node.getSubtype();
        switch (subtype){
            case CecaDiagramConstants.CAUSE_ACTION_ROOT:
            {
                final GConnector output = GraphFactory.eINSTANCE.createGConnector();
                node.getConnectors().add(output);
                output.setType(StateMachineConstants.STATE_MACHINE_RIGHT_OUTPUT_CONNECTOR);
                break;
            }
            case StateMachineConstants.TARGET_DISADVANTAGE: {
                final GConnector input = GraphFactory.eINSTANCE.createGConnector();
                node.getConnectors().add(input);
                input.setType(StateMachineConstants.STATE_MACHINE_LEFT_INPUT_CONNECTOR);
                break;
            }
            case StateMachineConstants.INTERMEDIATE_DISADVANTAGE:{
                final GConnector input = GraphFactory.eINSTANCE.createGConnector();
                node.getConnectors().add(input);
                input.setType(StateMachineConstants.STATE_MACHINE_LEFT_INPUT_CONNECTOR);

                final GConnector output = GraphFactory.eINSTANCE.createGConnector();
                node.getConnectors().add(output);
                output.setType(StateMachineConstants.STATE_MACHINE_RIGHT_OUTPUT_CONNECTOR);
                break;
            }
        }



    }


    public void addConditionActionConnector(final String type) {
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

    public void addStateMachineConnector(final String type){
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

    public GConnector addStateMachineConnector(GNode node, String type) {
        final GConnector connector = GraphFactory.eINSTANCE.createGConnector();
        connector.setType(type);
        final GModel model = graphEditor.getModel();
        final CompoundCommand command = new CompoundCommand();
        final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(model);
        final EReference connectors = GraphPackage.Literals.GCONNECTABLE__CONNECTORS;
        command.append(AddCommand.create(editingDomain, node, connectors, connector));
        if (command.canExecute()) {
            editingDomain.getCommandStack().execute(command);
        }
        return connector;
    }

    public GConnection addStateMachineConnection(GConnector source, GConnector target, String description) {
        final GConnection connection = GraphFactory.eINSTANCE.createGConnection();

        connection.setType(StateMachineConstants.STATE_MACHINE_CONNECTION);
        if (source.getType().contains("output"))
        {
            connection.setSource(source);
            connection.setTarget(target);
        } else {
            connection.setSource(target);
            connection.setTarget(source);
        }

        connection.setDescription(description);

        source.getConnections().add(connection);

        // Set the rest of the values via EMF commands because they touch the currently-edited model.
        GModel model = graphEditor.getModel();
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


    public void clearConnectors() {
        Commands.clearConnectors(graphEditor.getModel(), graphEditor.getSelectionManager().getSelectedNodes());
    }

    public void handlePaste() {
        graphEditor.getSelectionManager().paste();
    }

    public void handleSelectAll() {
        graphEditor.getSelectionManager().selectAllNodes();
        graphEditor.getSelectionManager().selectAllJoints();
    }

    public GNode addConditionActionOrGate(double X, double Y, int inputs) {
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
        node.setDescription("");

        Commands.addNode(graphEditor.getModel(), node);
        return node;

    }

    public GNode addConditionActionAndGate(double X, double Y) {
        ////System.out.printlnprintln("called and gate");

        final GNode node = GraphFactory.eINSTANCE.createGNode();
        node.setY(Y);
        node.setX(X);
        node.setId(allocateNewId());

        final GConnector output = GraphFactory.eINSTANCE.createGConnector();
        node.getConnectors().add(output);
        output.setType(DefaultConnectorTypes.RIGHT_OUTPUT);

        final GConnector input = GraphFactory.eINSTANCE.createGConnector();
        node.getConnectors().add(input);
        input.setType(DefaultConnectorTypes.LEFT_INPUT);

        final GConnector input2 = GraphFactory.eINSTANCE.createGConnector();
        node.getConnectors().add(input2);
        input2.setType(DefaultConnectorTypes.LEFT_INPUT);

        node.setType(CecaDiagramConstants.GATE_NODE);
        node.setSubtype("and");
        node.setDescription("description2!");

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

    public void addConditionActionConnection(GConnector source, GConnector target) {
        // Set the rest of the values via EMF commands because they touch the currently-edited model.
        GModel model = graphEditor.getModel();
        final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(model);
        final CompoundCommand command = new CompoundCommand();
        System.out.println("created new connection");

        final List<GJoint> joints = new ArrayList<>();
        GConnector realSource;
        GConnector realTarget;
        if (source.getType().contains("output"))
        {
            realSource = source;
            realTarget = target;
        } else {
            realSource = target;
            realTarget =  source;
        }

        final GJoint joint = GraphFactory.eINSTANCE.createGJoint();
        joint.setX(((((GNode) realTarget.getParent()).getX() + ((GNode) realSource.getParent()).getX())/2.0) + ((GNode) realSource.getParent()).getWidth() );
        joint.setY(((GNode) realSource.getParent()).getY());
        joints.add(joint);
        final GJoint joint2 = GraphFactory.eINSTANCE.createGJoint();
        joint2.setX((((GNode) realTarget.getParent()).getX()+ ((GNode) realSource.getParent()).getX())/2.0);
        joint2.setY(((GNode) realTarget.getParent()).getX());
        joints.add(joint2);
        ////System.out.printlnprintln("JOINTS " + joints);
        ConnectionCommands.addConnection(model, realSource, realTarget, null, joints);

        if (command.canExecute()) {
            editingDomain.getCommandStack().execute(command);
        }

    }

    public void setConnectionSkin(String stateMachineConnection, Class<StateMachineConnectionSkin> stateMachineConnectionSkinClass) {
        graphEditor.setConnectionSkin(stateMachineConnection, stateMachineConnectionSkinClass);
    }

    public void setTailSkin(String stateMachineLeftInputConnector, Class<StateMachineTailSkin> stateMachineTailSkinClass) {
        graphEditor.setTailSkin(stateMachineLeftInputConnector, stateMachineTailSkinClass);
    }

    public GNode addCauseActionNode(double X, double Y, String description, String subtype) {

        final GNode node = GraphFactory.eINSTANCE.createGNode();
        node.setY(10 + Y);

        node.setX(10 + X);
        node.setId(allocateNewId());

        node.setType(CecaDiagramConstants.CECA_NODE);
        node.setDescription(description);
        node.setSubtype(subtype);
        addInitialConnectors(node);

        Commands.addNode(graphEditor.getModel(), node);

        return node;

    }

    private void addInitialConnectors(GNode node){
        ////System.out.printlnprintln("adding connectors");
        switch (node.getSubtype()){
            case CecaDiagramConstants.CAUSE_ACTION_ROOT: {
                final GConnector output = GraphFactory.eINSTANCE.createGConnector();
                node.getConnectors().add(output);
                output.setType(DefaultConnectorTypes.RIGHT_OUTPUT);
                break;
            }
            case CecaDiagramConstants.TARGET_DISADVANTAGE: {
                final GConnector input = GraphFactory.eINSTANCE.createGConnector();
                node.getConnectors().add(input);
                input.setType(DefaultConnectorTypes.LEFT_INPUT);
                break;
            }
            case CecaDiagramConstants.CONDITION:
            case CecaDiagramConstants.ACTION: {
                final GConnector input = GraphFactory.eINSTANCE.createGConnector();
                node.getConnectors().add(input);
                input.setType(DefaultConnectorTypes.LEFT_INPUT);

                final GConnector output = GraphFactory.eINSTANCE.createGConnector();
                node.getConnectors().add(output);
                output.setType(DefaultConnectorTypes.RIGHT_OUTPUT);
                break;
            }
            default:
                ////System.out.printlnprintln("NODE MUST HAVE A SUBTYPE");
                break;
        }

    }


}
