package cause.effect.chain.editor.controller.transformations;

import cause.effect.chain.editor.controller.modes.CauseActionModeController;
import cause.effect.chain.editor.controller.modes.StateMachineModeController;
import de.tesis.dynaware.grapheditor.GraphEditor;
import de.tesis.dynaware.grapheditor.core.DefaultGraphEditor;
import de.tesis.dynaware.grapheditor.core.connections.ConnectionCommands;
import de.tesis.dynaware.grapheditor.core.skins.defaults.utils.DefaultConnectorTypes;
import de.tesis.dynaware.grapheditor.demo.customskins.NodeTraversalUtils;
import de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram.CauseActionDiagramSubtypes;
import cause.effect.chain.editor.model.skins.CauseActionModel.CecaDiagramConstants;
import cause.effect.chain.editor.model.skins.statemachine.StateMachineConstants;
import de.tesis.dynaware.grapheditor.model.*;
import javafx.scene.control.Alert;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import java.util.*;
import java.util.stream.Collectors;

public class ModelTransformationController {

    private static final EReference NODES = GraphPackage.Literals.GMODEL__NODES;
    private static final EReference NODE_CONNECTORS = GraphPackage.Literals.GCONNECTABLE__CONNECTORS;
    private final CauseActionModeController causeActionModeController;
    private final StateMachineModeController stateMachineController;
    private final GraphEditor graphEditor;

    private static final EReference CONNECTIONS = GraphPackage.Literals.GMODEL__CONNECTIONS;
    private static final EReference CONNECTOR_CONNECTIONS = GraphPackage.Literals.GCONNECTOR__CONNECTIONS;
    private static final EReference CONNECTOR = GraphPackage.Literals.GCONNECTABLE__CONNECTORS;

    public ModelTransformationController(CauseActionModeController causeActionModeController, StateMachineModeController stateMachineController, GraphEditor graphEditor) {
        this.causeActionModeController = causeActionModeController;
        this.stateMachineController = stateMachineController;
        this.graphEditor = graphEditor;
    }

    public void transformIntoStateMachine() {
        graphEditor.getSelectionManager().clearSelection();
        List<GNode> rootNodes = getRootCauseNodes();

        ////System.out.println("found " + rootNodes.size() + " root nodes");

        rootNodes.forEach(this::beginTransformationIntoStateMachine2);
        graphEditor.getSelectionManager().deleteSelection();
        //((DefaultGraphEditor) graphEditor).getController().reloadView();
        ////System.out.println("added this");
        //TODO: also remove their skins?
        for (GNode node : graphEditor.getModel().getNodes()) {
            ////System.out.println("NODE: " + node + '\n' + node.getConnectors());
            node.getConnectors().removeIf(connector -> connector.getConnections().isEmpty());
        }
//        for (GNode node : graphEditor.getModel().getNodes()) {
//            //System.out.println("NODE: " + node + '\n' + node.getConnectors());
//        }
//        for (GNode node : graphEditor.getModel().getNodes()) {
//            node.setType(StateMachineConstants.STATE_MACHINE_NODE);
//        }
//        //System.out.println("==========================");
//        for (GNode node : graphEditor.getModel().getNodes()) {
//            //System.out.println("NODE: " + node + '\n' + node.getConnectors());
//        }
//        graphEditor.reload();
        ((DefaultGraphEditor) graphEditor).getController().setModel(graphEditor.getModel());

    }

    private void beginTransformationIntoStateMachine2(GNode rootNode) {
        List<GConnector> outputConnectors = rootNode.getConnectors().stream()
                .filter(conector -> !isInput(conector.getType()))
                .filter(connector -> !connector.getConnections().isEmpty())
                .collect(Collectors.toList());

        ////System.out.println("found " + outputConnectors.size() + " outputConnectors ");
        rootNode.setType(StateMachineConstants.STATE_MACHINE_NODE);

        outputConnectors.forEach(connector -> {
            seekCondition(rootNode, NodeTraversalUtils.getTargetNode(connector));
        });
    }

    private void seekCondition(GNode rootNode, GNode targetNode) {
        ////System.out.println("found " + targetNode + " condition ");
        String type = targetNode.getSubtype();
        switch (type) {
            case CecaDiagramConstants.CONDITION:
                ////System.out.println("found condition");
                graphEditor.getSkinLookup().lookupNode(targetNode).setSelected(true);
                seekTargetAction(rootNode, targetNode);
                break;
            case "and":
                ////System.out.println("IMPOSSIBLE");
                break;
            case "or":
                graphEditor.getSkinLookup().lookupNode(targetNode).setSelected(true);
                List<GConnector> outputConnectors = targetNode.getConnectors().stream()
                        .filter(conector -> !isInput(conector.getType()))
                        .filter(connector -> !connector.getConnections().isEmpty())
                        .collect(Collectors.toList());
                GNode condition = NodeTraversalUtils.getTargetNode(outputConnectors.get(0));
                seekCondition(rootNode, condition);
                break;
        }
    }


    private void seekTargetAction(GNode rootNode, GNode targetNode) {
        ////System.out.println("found " + targetNode + " seekTargetAction ");
        String description = targetNode.getDescription();
        List<GConnector> outputConnectors = targetNode.getConnectors().stream()
                .filter(conector -> !isInput(conector.getType()))
                .filter(connector -> !connector.getConnections().isEmpty())
                .collect(Collectors.toList());
        List<String> gates = new ArrayList<>();
        outputConnectors.forEach(connector -> {
            seekAction(rootNode, NodeTraversalUtils.getTargetNode(connector), description);
        });

    }

    private void seekAction(GNode rootNode, GNode targetNode, String description) {
        ////System.out.println("found " + targetNode + " seekAction ");
        String type = targetNode.getSubtype();
        switch (type) {
            case StateMachineConstants.INTERMEDIATE_DISADVANTAGE:
            case CecaDiagramConstants.TARGET_DISADVANTAGE:
            case CecaDiagramConstants.ACTION: {
                final GConnector output = stateMachineController.addConnector(rootNode, StateMachineConstants.STATE_MACHINE_RIGHT_OUTPUT_CONNECTOR);
                final GConnector input = stateMachineController.addConnector(targetNode, StateMachineConstants.STATE_MACHINE_LEFT_INPUT_CONNECTOR);

                stateMachineController.addStateMachineConnection(output, input, description);
                if (!Objects.equals(targetNode.getType(), StateMachineConstants.STATE_MACHINE_NODE)) {
                    List<GConnector> outputConnectors = targetNode.getConnectors().stream()
                            .filter(conector -> !isInput(conector.getType()))
                            .filter(connector -> !connector.getConnections().isEmpty())
                            .collect(Collectors.toList());
                    outputConnectors.forEach(connector -> seekCondition(targetNode, NodeTraversalUtils.getTargetNode(connector)));

                    targetNode.setType(StateMachineConstants.STATE_MACHINE_NODE);
                    if (type.equals(CecaDiagramConstants.ACTION)) {
                        targetNode.setSubtype(StateMachineConstants.INTERMEDIATE_DISADVANTAGE);
                    } else {
                        targetNode.setSubtype(StateMachineConstants.TARGET_DISADVANTAGE);
                    }
                }
                break;
            }
            case "and": {
                graphEditor.getSkinLookup().lookupNode(targetNode).setSelected(true);

                List<GConnector> inputConnectors = targetNode.getConnectors().stream()
                        .filter(conector -> isInput(conector.getType()))
                        .filter(connector -> !connector.getConnections().isEmpty())
                        .collect(Collectors.toList());
                ArrayList<String> descriptions = new ArrayList<>();

                inputConnectors.forEach(connector -> {
                    GNode source = (GNode) NodeTraversalUtils.getSourceNode(connector.getConnections().get(0));
                    descriptions.add(source.getDescription());
                });

                String newDescription = String.join(" & ", descriptions);
                ////System.out.println("descriptions: " + newDescription);
                List<GConnector> outputConnectors = targetNode.getConnectors().stream()
                        .filter(conector -> !isInput(conector.getType()))
                        .filter(connector -> !connector.getConnections().isEmpty())
                        .collect(Collectors.toList());
                outputConnectors.forEach(connector -> seekAction(rootNode, NodeTraversalUtils.getTargetNode(connector), newDescription));
                break;
            }
            case "or":
                graphEditor.getSkinLookup().lookupNode(targetNode).setSelected(true);
                List<GConnector> outputConnectors = targetNode.getConnectors().stream()
                        .filter(conector -> !isInput(conector.getType()))
                        .filter(connector -> !connector.getConnections().isEmpty())
                        .collect(Collectors.toList());
                GNode action = NodeTraversalUtils.getTargetNode(outputConnectors.get(0));
                seekAction(rootNode, action, description);
                break;
        }
    }

    private void beginTransformationIntoStateMachine(GNode rootNode) {
        ////System.out.println("Called begin transform on node " + rootNode);
        List<GConnector> inputConnectors = rootNode.getConnectors().stream()
                .filter(conector -> isInput(conector.getType()))
                .filter(connector -> !connector.getConnections().isEmpty())
                .collect(Collectors.toList());

        ////System.out.println("found " + inputConnectors.size() + " input connectors");

        if (inputConnectors.isEmpty()) {
            ////System.out.println("fucked up, somehow " + rootNode);
            return;
        }
        //TRUE STEP
        inputConnectors.forEach(connector -> {
            GNode actionOrGateNode = (GNode) connector.getConnections().get(0).getSource().getParent();
            ////System.out.println("Node identified as previous: " + actionOrGateNode + "!!!");
            List<GConnector> actionOrGateInputs = actionOrGateNode.getConnectors().stream()
                    .filter(actionOrGateConector -> isInput(actionOrGateConector.getType()))
                    .filter(actionOrGateConector -> !actionOrGateConector.getConnections().isEmpty())
                    .collect(Collectors.toList());

            if (actionOrGateInputs.isEmpty()) {
                ////System.out.println("found floating action or gate: " + actionOrGateNode);
                return;
            }
            String description;
            //TODO:GATE LOGIC HERE
            if (actionOrGateNode.getType().equals(CecaDiagramConstants.GATE_NODE)) {
                if (actionOrGateNode.getSubtype().equals("or")) {
                    List<GNode> nodePredecessors = actionOrGateInputs.stream().map(orConnector -> ((GNode) orConnector.getConnections().get(0).getSource().getParent())).collect(Collectors.toList());
                    nodePredecessors.forEach(predNode -> onActionProcess(rootNode, predNode, predNode.getDescription()));
                    graphEditor.getSkinLookup().lookupNode(actionOrGateNode).setSelected(true);
                }
                if (actionOrGateNode.getSubtype().equals("and")) {
                    ArrayList<String> descriptions = new ArrayList<>();
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
        ////System.out.println("caleld onActionProcess");
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


            ////System.out.println("found edge state: " + predecessorState);
            beginTransformationIntoStateMachine(predecessorState);

            final GConnector output = stateMachineController.addConnector(predecessorState, StateMachineConstants.STATE_MACHINE_RIGHT_OUTPUT_CONNECTOR);

            final GConnector input = stateMachineController.addConnector(rootNode, StateMachineConstants.STATE_MACHINE_LEFT_INPUT_CONNECTOR);

            stateMachineController.addStateMachineConnection(output, input, description);

            ////System.out.println("new edge state: " + predecessorState);
        });
    }

    private List<GNode> getTargetDisadvantageNodes() {
        return graphEditor.getModel().getNodes().stream()
                .filter(node -> node.getSubtype().equals(CauseActionDiagramSubtypes.TARGET_DISADVANTAGE))
                .collect(Collectors.toList());
    }

    private List<GNode> getWarnNodes() {
        return graphEditor.getModel().getNodes().stream()
                .filter(node -> node.getSubtype().equals(StateMachineConstants.WARN))
                .collect(Collectors.toList());
    }

    private List<GNode> getRootCauseNodes() {
        return graphEditor.getModel().getNodes().stream()
                .filter(node -> node.getSubtype().equals(CecaDiagramConstants.CAUSE_ACTION_ROOT))
                .collect(Collectors.toList());
    }

    private List<GNode> getNodesWithDescription3() {
        return graphEditor.getModel().getNodes().stream()
                .filter(node -> node.getDescription().equals("3"))
                .collect(Collectors.toList());
    }

    public boolean isInput(String type) {
        return type.contains("input");
    }

    public void transformIntoCauseActionDiagram() {
        graphEditor.getSelectionManager().clearSelection();
        List<GNode> warnNodes = getWarnNodes();
        removeWarnNodes(warnNodes);
        List<GNode> mitigationNodes = getMitigationNodes();
        removeMitigation(mitigationNodes);
        graphEditor.getSelectionManager().deleteSelection();
        List<GNode> rootNodes = getTargetDisadvantageNodes();
        rootNodes.forEach(node -> {
            node.setSubtype(CecaDiagramConstants.TARGET_DISADVANTAGE);
            node.setType(CecaDiagramConstants.CECA_NODE);
        });
        //System.out.println("found " + rootNodes.size() + " root nodes");
        try {
            rootNodes.forEach(this::beginTransformationIntoDiagram);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error while transforming:");
            alert.setHeaderText("An error occured when transforming");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        graphEditor.getSelectionManager().deleteSelection();
        for (GNode node : graphEditor.getModel().getNodes()) {
            List<GConnector> toremove = node.getConnectors().stream().filter(connector -> connector.getConnections().isEmpty()).collect(Collectors.toList());
            final CompoundCommand command = new CompoundCommand();
            final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(graphEditor.getModel());
            command.append(RemoveCommand.create(editingDomain, node, NODE_CONNECTORS, toremove));
            if (command.canExecute()) {
                editingDomain.getCommandStack().execute(command);
            }
        }
        graphEditor.reload();
        ((DefaultGraphEditor) graphEditor).getController().setModel(graphEditor.getModel());
    }

    private void removeMitigation(List<GNode> mitigationNodes) {
        mitigationNodes.stream().forEach(node -> graphEditor.getSkinLookup().lookupNode(node).setSelected(true));
    }

    private List<GNode> getMitigationNodes() {
        return graphEditor.getModel().getNodes().stream()
                .filter(node -> node.getSubtype().equals(StateMachineConstants.WARN) || node.getSubtype().equals(StateMachineConstants.MITIGATION))
                .collect(Collectors.toList());
    }

    private void removeWarnNodes(List<GNode> warnNodes) {
        for (GNode warnNode : warnNodes) {
            GConnector prevOutput = warnNode.getConnectors().stream()
                    .filter(conector -> isInput(conector.getType()))
                    .filter(connector -> !connector.getConnections().isEmpty())
                    .collect(Collectors.toList()).get(0).getConnections().get(0).getSource();

            GConnector nextInput = warnNode.getConnectors().stream()
                    .filter(conector -> !isInput(conector.getType()))
                    .filter(connector -> !connector.getConnections().isEmpty())
                    .collect(Collectors.toList()).get(0).getConnections().get(0).getTarget();

            String description = warnNode.getConnectors().stream()
                    .filter(conector -> !isInput(conector.getType()))
                    .filter(connector -> !connector.getConnections().isEmpty())
                    .collect(Collectors.toList()).get(0).getConnections().get(0).getDescription();

           // System.out.println("pre-warn was " + prevOutput + " post was " + nextInput);

            warnNode.getConnectors().stream()
                    .map(conctr -> conctr.getConnections().get(0))
                    .forEach(conction -> ConnectionCommands.removeConnection(graphEditor.getModel(), conction));

            stateMachineController.addStateMachineConnection(prevOutput, nextInput, description);

        }
    }

    public void beginTransformationIntoDiagram(GNode rootNode) {
        //System.out.println("Called begin transform into diagram on node " + rootNode);
        List<GConnector> inputConnectors = rootNode.getConnectors().stream()
                .filter(conector -> isInput(conector.getType()))
                .filter(connector -> !connector.getConnections().isEmpty())
                .collect(Collectors.toList());

       // System.out.println("found " + inputConnectors.size() + " input connectors");


        if (inputConnectors.isEmpty()) {
            //System.out.println("trying to make root node " + rootNode);
            rootNode.setType(CecaDiagramConstants.CECA_NODE);
            rootNode.setSubtype(CecaDiagramConstants.CAUSE_ACTION_ROOT);
            return;
        }

        if (inputConnectors.size() == 1) {
            //System.out.println("1");
            //System.out.println("output is " + inputConnectors.get(0).getConnections().get(0).getSource().getParent());
           // System.out.println("input is " + inputConnectors.get(0).getConnections().get(0).getTarget().getParent());
            GNode predecessorNode = (GNode) inputConnectors.get(0).getConnections().get(0).getSource().getParent();
            String description = inputConnectors.get(0).getConnections().get(0).getDescription();
            procesPredecesorNode(rootNode, predecessorNode, description);
            GConnection conectionToDetele = inputConnectors.get(0).getConnections().get(0);
            graphEditor.getSkinLookup().lookupConnection(conectionToDetele).setSelected(true);
        } else {
            ////System.out.println("2");

            List<GConnection> orConnections = new ArrayList<>();
            HashMap<List<String>, List<GConnection>> connectionsMap = new HashMap<>();

            inputConnectors.forEach(connector -> {
                GConnection connection = connector.getConnections().get(0);
                String description = connection.getDescription();

                if (description.contains("&")) {
                    List<String> conditions = Arrays.stream(description.split("&")).map(String::trim).collect(Collectors.toList());
                    ////System.out.println("conditions + " + conditions);

                    if (connectionsMap.containsKey(conditions)) {
                        connectionsMap.get(conditions).add(connection);
                        ////System.out.println("was previous");
                    } else {
                        ////System.out.println("adding new");
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
                GNode andGate = causeActionModeController.addAndGate(rootNode.getX() - rootNode.getWidth() - 10, rootNode.getY());
                final GConnector rootNodeInput = stateMachineController.addConnector(rootNode, DefaultConnectorTypes.LEFT_INPUT);
                causeActionModeController.addConnection(andGate.getConnectors().stream().filter(con -> !isInput(con.getType())).collect(Collectors.toList()).get(0), rootNodeInput);
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
                    GNode orGate = causeActionModeController.addOrGate(rootNode.getX() - rootNode.getWidth() - 10, rootNode.getY(), inputConnectors.size());
                    final GConnector rootNodeInput = stateMachineController.addConnector(rootNode, DefaultConnectorTypes.LEFT_INPUT);
                    causeActionModeController.addConnection(orGate.getConnectors().stream().filter(con -> !isInput(con.getType())).collect(Collectors.toList()).get(0), rootNodeInput);
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
        ////System.out.println("predecessorNode + " + predecessorNode);
        final GNode conditionNode = causeActionModeController.addConditionNode((predecessorNode.getX() + rootNode.getX())/2 , (predecessorNode.getY() + rootNode.getY())/2 , description);

        conditionNode.setDescription(description);

        final GModel model = graphEditor.getModel();
        //TODO: limit X so it doest go out of bounds

        final GConnector conditionNodeInput = conditionNode.getConnectors().stream().filter(connector -> isInput(connector.getType())).collect(Collectors.toList()).get(0);
        final GConnector conditionNodeOutput = conditionNode.getConnectors().stream().filter(connector -> !isInput(connector.getType())).collect(Collectors.toList()).get(0);
       // System.out.println("connectors: " + conditionNodeInput + "," + conditionNodeOutput);

        final GConnector output = stateMachineController.addConnector(predecessorNode, DefaultConnectorTypes.RIGHT_OUTPUT);
        final GConnector input = stateMachineController.addConnector(rootNode, DefaultConnectorTypes.LEFT_INPUT);

        //System.out.println("rootNode " + rootNode + " predecessorNode " + predecessorNode + "predecessorNode");

        causeActionModeController.addConnection(output, conditionNodeInput);
        //System.out.println("adding connection between  " + (GNode) output.getParent() + " and " + (GNode) conditionNodeInput.getParent());
        causeActionModeController.addConnection(conditionNodeOutput, input);
        //System.out.println("adding connection between  " + (GNode) conditionNodeOutput.getParent() + " and " + (GNode) input.getParent());
        predecessorNode.setType(CecaDiagramConstants.CECA_NODE);
        predecessorNode.setSubtype(CecaDiagramConstants.ACTION);
        beginTransformationIntoDiagram(predecessorNode);

    }
}
