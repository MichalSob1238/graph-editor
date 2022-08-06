package de.tesis.dynaware.grapheditor.demo.CoherencyChecker;

import de.tesis.dynaware.grapheditor.GNodeSkin;
import de.tesis.dynaware.grapheditor.GraphEditor;
import de.tesis.dynaware.grapheditor.SkinLookup;
import de.tesis.dynaware.grapheditor.demo.customskins.state.machine.StateMachineConstants;
import de.tesis.dynaware.grapheditor.model.GConnection;
import de.tesis.dynaware.grapheditor.model.GConnector;
import de.tesis.dynaware.grapheditor.model.GNode;
import de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram.CecaDiagramConstants;
import de.tesis.dynaware.grapheditor.demo.customskins.NodeTraversalUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CoherencyChecker {

    private final GraphEditor graphEditor;
    private final SkinLookup skinLookup;

    public CoherencyChecker(GraphEditor graphEditor) {
        this.graphEditor = graphEditor;
        this.skinLookup = graphEditor.getSkinLookup();
    }

    public void getNotified(GConnection connection) {

//        updateCorrectnesStatus(NodeTraversalUtils.getSourceNode(connection));
//        updateCorrectnesStatus(NodeTraversalUtils.getTargetNode(connection));
    }

    private void updateCorrectnesStatus(GNode node) {
        GNodeSkin sourceNodeSkin = skinLookup.lookupNode(node);

        switch (node.getType()) {
            case CecaDiagramConstants.CECA_NODE:
                sourceNodeSkin.updateStatus(checkForDiagramNode(node));
                break;
            case StateMachineConstants.STATE_MACHINE_NODE:
                sourceNodeSkin.updateStatus(checkForStateMachineNode(node));
                break;
            case CecaDiagramConstants.GATE_NODE:
                sourceNodeSkin.updateStatus(checkForGateNode(node));
                break;
        }
    }

    private List<String> checkForGateNode(GNode gate) {
        List<String> errorList = new ArrayList<>();
        int inputs = 0;
        boolean outputCondition = false;
        for (GConnector connector : gate.getConnectors()) {
            if (NodeTraversalUtils.isInput(connector)) {
                for (GConnection connection : connector.getConnections()) {
                    inputs++;
                    GNode preceedingNode = NodeTraversalUtils.getTargetNode(connection);
                    if (!Objects.equals(preceedingNode.getSubtype(), "action")) {
                        errorList.add("Gate Node can only be preceeded by action nodes, currently one of the connections leads to " + preceedingNode.getSubtype() + " node");
                    }

                }
            } else {
                for (GConnection connection : connector.getConnections()) {
                    outputCondition = true;
                    GNode preceedingNode = NodeTraversalUtils.getTargetNode(connection);
                    String preceedingNodeSubtype = preceedingNode.getSubtype();
                    if (Objects.equals(preceedingNodeSubtype, "action") || Objects.equals(preceedingNode.getType(), CecaDiagramConstants.GATE_NODE)) {
                        errorList.add("Gate Node can only be leading to a disadvantage node, currently one of the connections leads to " + preceedingNodeSubtype + " node");
                    }
                }
            }
        }
        if (!(inputs > 1)) {
            errorList.add("Gate node must have at least 2 input connections");
        }
        if (!outputCondition) {
            errorList.add("Gate node must have an output connection");
        }
        return errorList;
    }

    private List<String> checkForStateMachineNode(GNode node) {
        List<String> errorList = new ArrayList<>();
        switch (node.getSubtype()) {
            case "initial-state": {
                //has to have at least 1 output (input is blocked by system)
                Optional<String> error = Optional.ofNullable(subcheckForInitialOrTargetDisadvantage(node, "Initial State"));
                error.ifPresent(errorList::add);
                break;
            }
            case "target-disadvantage": {
                //has to have at least 1 output (input is blocked by system)
                Optional<String> error = Optional.ofNullable(subcheckForInitialOrTargetDisadvantage(node, "Target Disadvantage"));
                error.ifPresent(errorList::add);
                break;
            }
            case "intermediate-disadvantage": {
                //needs at least one input and one output, leading to actions each
                errorList.addAll(subcheckForStateMachineIntermediateDisadvantage(node));
                break;
            }
        }
        return errorList;
    }

    private List<String> subcheckForStateMachineIntermediateDisadvantage(GNode node) {
        List<String> errorList = new ArrayList<>();
        boolean inputCondition = false;
        boolean outputcondition = false;
        for (GConnector connector : node.getConnectors()) {
            if (NodeTraversalUtils.isInput(connector)) {
                if (connector.getConnections().size() > 0) {
                    inputCondition = true;
                }
            } else {
                if (connector.getConnections().size() > 0) {
                    outputcondition = true;
                }
            }
        }
        if (!inputCondition) {
            errorList.add("Intermediate Dissadvantage needs at least one input connection");
        }
        if (!outputcondition) {
            errorList.add("Intermediate Dissadvantage needs at least one output connection");
        }
        return errorList;
    }

    private String subcheckForInitialOrTargetDisadvantage(GNode node, String subtype) {
        for (GConnector connector : node.getConnectors()) {
            if (connector.getConnections().size() > 0) {
                return null;
            }
        }
        return subtype + " must have at least one input connection";
    }


    private List<String> checkForDiagramNode(GNode node) {
        List<String> errorList = new ArrayList<>();
        switch (node.getSubtype()) {
            case "initial-state": {
                //has to have at least 1 output (input is blocked by system) and the following tile must be action
                Optional<String> result = Optional.ofNullable(checkForInitialState(node));
                result.ifPresent(errorList::add);
                break;
            }
            case "action": {
                //Has to have at least one connected input leading to "initial-state" or "intermediate-dissadvantage" and at least one output leading to not-action
                List<String> errors = checkForAction(node);
                errorList.addAll(errors);
                break;
            }
            case "target-disadvantage": {
                //needs one input from an action or a gate
                Optional<String> result = Optional.ofNullable(subcheckForDiagramTargetDisadvantage(node));
                result.ifPresent(errorList::add);
                break;
            }
            case "intermediate-disadvantage": {
                //needs at least one input and one output, leading to actions each
                List<String> errors = subcheckForDiagramIntermediateDissadvantage(node);
                errorList.addAll(errors);
                break;
            }
            default: {
                System.out.println("Default case in coherency checker");
            }
        }

        return errorList;
    }

    private List<String> subcheckForDiagramIntermediateDissadvantage(GNode node) {
        List<String> errors = new ArrayList<>();
        boolean outputExists = false;
        boolean inputExists = false;
        for (GConnector connector : node.getConnectors()) {
            if (NodeTraversalUtils.isInput(connector)) {
                //TODO: refactor this to a function with a list of conditions
                for (GConnection connection : connector.getConnections()) {
                    inputExists = true;
                    String sourceNodeSubtype = NodeTraversalUtils.getSourceNode(connection).getSubtype();
                    //TODO: these cannot actually be null - you can use equals - OR do contains("disadvantage")
                    if (!Objects.equals(sourceNodeSubtype, "action")) {
                        errors.add("Intermediate Disadvantage can only follow  an action node, currently one of the connections leads to a "+ sourceNodeSubtype +" node");
                    }
                }
            } else {
                for (GConnection connection : connector.getConnections()) {
                    outputExists = true;
                    String targetNodeSubtype = NodeTraversalUtils.getTargetNode(connection).getSubtype();
                    //TODO: these cannot actually be null - you can use equals
                    if (!targetNodeSubtype.equals("action")) {
                        errors.add("Intermediate Disadvantage can only lead to an action node, , currently one of the connections leads to a" + targetNodeSubtype +" node");
                    }
                }
            }
        }
        if (!inputExists) {
            errors.add("Intermediate dissadvantage must have at least one input");
        }
        if (!outputExists) {
            errors.add("Intermediate dissadvantage must have at least one output");
        }
        return errors;
    }

    private String subcheckForDiagramTargetDisadvantage(GNode node) {
        for (GConnector connector : node.getConnectors()) {
            //TODO: check for null pointer error
            for (GConnection connection : connector.getConnections()) {
                String sourceNodeSubtype = NodeTraversalUtils.getSourceNode(connection).getSubtype();
                //TODO: these cannot actually be null - you can use equals
                if (Objects.equals(sourceNodeSubtype, "action") || Objects.equals(sourceNodeSubtype, "AND") || Objects.equals(sourceNodeSubtype, "OR")) {
                    return null;
                }
            }
        }
        return "Target Disadvantage must have at least one input";
    }

    private String checkForInitialState(GNode node) {

        for (GConnector connector : node.getConnectors()) {
            //TODO: check for null pointer error
            for (GConnection connection : connector.getConnections()) {
                String targetNodeSubtype = NodeTraversalUtils.getTargetNode(connection).getSubtype();
                //TODO: these cannot actually be null - you can use equals
                if (Objects.equals(targetNodeSubtype, "action") || Objects.equals(targetNodeSubtype, "AND") || Objects.equals(targetNodeSubtype, "OR")) {
                    return null;
                }
            }
        }
        return "Initial state must have an output";
    }

    private List<String> checkForAction(GNode node) {
        List<String> errorList = new ArrayList<>();
        boolean inputCondition = false;
        boolean outputcondition = false;
        for (GConnector connector : node.getConnectors()) {
            if (NodeTraversalUtils.isInput(connector)) {
                //TODO: refactor this to a function with a list of conditions
                for (GConnection connection : connector.getConnections()) {
                    inputCondition = true;
                    String sourceNodeSubtype = NodeTraversalUtils.getSourceNode(connection).getSubtype();
                    //TODO: these cannot actually be null - you can use equals - OR do contains("disadvantage")
                    if (Objects.equals(sourceNodeSubtype, "initial-disadvantage") || Objects.equals(sourceNodeSubtype, "intermediate-disadvantage")) {
                        errorList.add("action node can only follw a disadvantage node, curretly one of the inputs is connected to a " + sourceNodeSubtype+ " node");
                    }
                }
            } else {
                for (GConnection connection : connector.getConnections()) {
                    outputcondition = true;
                    String targetNodeSubtype = NodeTraversalUtils.getTargetNode(connection).getSubtype();
                    //TODO: these cannot actually be null - you can use equals
                    if (!targetNodeSubtype.equals("action")) {
                        errorList.add("action node can only lead to a disadvantage or a gate node, curretly one of the outputs leads to a " + targetNodeSubtype+ " node");
                    }
                }
            }
        }
        if (!inputCondition) {
            errorList.add("Action must have at least one input");
        }
        if (!outputcondition) {
            errorList.add("Action must have at least one output");
        }
        return errorList;
    }
}

