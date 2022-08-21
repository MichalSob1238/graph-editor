package cause.effect.chain.editor.controller.coherency;

import de.tesis.dynaware.grapheditor.GNodeSkin;
import de.tesis.dynaware.grapheditor.GraphEditor;
import de.tesis.dynaware.grapheditor.SkinLookup;
import de.tesis.dynaware.grapheditor.demo.customskins.NodeTraversalUtils;
import cause.effect.chain.editor.model.skins.CauseActionModel.CecaDiagramConstants;
import de.tesis.dynaware.grapheditor.demo.customskins.state.machine.StateMachineConstants;
import de.tesis.dynaware.grapheditor.model.GConnection;
import de.tesis.dynaware.grapheditor.model.GConnector;
import de.tesis.dynaware.grapheditor.model.GNode;

import java.util.*;

public class CoherencyChecker {

    private final GraphEditor graphEditor;
    private final SkinLookup skinLookup;

    public CoherencyChecker(GraphEditor graphEditor) {
        this.graphEditor = graphEditor;
        this.skinLookup = graphEditor.getSkinLookup();
    }

    public void getNotified(GConnection connection) {
        //System.out.println("got notified for connection " + connection);
        updateCorrectnesStatus(NodeTraversalUtils.getSourceNode(connection));
        updateCorrectnesStatus(NodeTraversalUtils.getTargetNode(connection));
    }

    public int updateCorrectnesStatus(GNode node) {
        GNodeSkin sourceNodeSkin = skinLookup.lookupNode(node);

        switch (node.getType()) {
            case CecaDiagramConstants.CECA_NODE:
                return sourceNodeSkin.updateStatus(checkForDiagramNode(node));
            case StateMachineConstants.STATE_MACHINE_NODE:
                return sourceNodeSkin.updateStatus(checkForStateMachineNode(node));
            case CecaDiagramConstants.GATE_NODE:
                return sourceNodeSkin.updateStatus(checkForGateNode(node));
        }
        ;
        return 0;
    }

    private List<String> checkForGateNode(GNode gate) {
        List<String> errorList = new ArrayList<>();
        int inputs = 0;
        boolean outputCondition = false;
        String subtype = gate.getSubtype();
        for (GConnector connector : gate.getConnectors()) {
            if (NodeTraversalUtils.isInput(connector)) {
                for (GConnection connection : connector.getConnections()) {
                    inputs++;
                    GNode preceedingNode = NodeTraversalUtils.getSourceNode(connection);
                    if (!Objects.equals(preceedingNode.getSubtype(), "condition")) {
                        errorList.add( subtype.toUpperCase(Locale.ROOT) + " gate can only be preceeded by condition nodes, currently one of the connections leads to " + preceedingNode.getSubtype() + " node");
                    }
                }
            } else {
                for (GConnection connection : connector.getConnections()) {
                    outputCondition = true;
                    GNode preceedingNode = NodeTraversalUtils.getTargetNode(connection);
                    String preceedingNodeSubtype = preceedingNode.getSubtype();
                    switch (preceedingNodeSubtype) {
                        case "action":
                        case CecaDiagramConstants.TARGET_DISADVANTAGE:
                        {
                            break;
                        }
                        default: {
                            errorList.add(subtype.toUpperCase(Locale.ROOT) + " gate can only be leading to a action or target disadvantage, currently one of the connections leads to " + preceedingNodeSubtype + " node");
                        }
                    }
                }
            }
        }


//        if (subtype == "and") {
//            for (GConnector connector : gate.getConnectors()) {
//                if (NodeTraversalUtils.isInput(connector)) {
//                    for (GConnection connection : connector.getConnections()) {
//                        inputs++;
//                        GNode preceedingNode = NodeTraversalUtils.getSourceNode(connection);
//                        if (!Objects.equals(preceedingNode.getSubtype(), "condition")) {
//                            errorList.add("AND gate can only be preceeded by condition nodes, currently one of the connections leads to " + preceedingNode.getSubtype() + " node");
//                        }
//                    }
//                } else {
//                    for (GConnection connection : connector.getConnections()) {
//                        outputCondition = true;
//                        GNode preceedingNode = NodeTraversalUtils.getTargetNode(connection);
//                        String preceedingNodeSubtype = preceedingNode.getSubtype();
//                        switch (preceedingNodeSubtype) {
//                            case "action":
//                            case CecaDiagramConstants.TARGET_DISADVANTAGE:
//                            {
//                                break;
//                            }
//                            default: {
//                                errorList.add("AND gate can only be leading to a action or target disadvantage, currently one of the connections leads to " + preceedingNodeSubtype + " node");
//                            }
//                        }
//                    }
//                }
//            }
//        } else
//        {  List<String> precedingTypes = new ArrayList<>();
//            for (GConnector connector : gate.getConnectors()) {
//                if (NodeTraversalUtils.isInput(connector)) {
//                    for (GConnection connection : connector.getConnections()) {
//                        inputs++;
//                        GNode preceedingNode = NodeTraversalUtils.getSourceNode(connection);
//                        String preceedingNodeSubtype = preceedingNode.getSubtype();
//                        precedingTypes.add(preceedingNodeSubtype);
//                    }
//                } else {
//                    for (GConnection connection : connector.getConnections()) {
//                        outputCondition = true;
//                        GNode preceedingNode = NodeTraversalUtils.getTargetNode(connection);
//                        String followingNodeSuptype = preceedingNode.getSubtype();
//                        switch (followingNodeSuptype) {
//                            case "condition":
//                            {
//                                if(precedingTypes.contains("condition") || precedingTypes.contains("and") || precedingTypes.contains("or")  )
//                                    errorList.add("OR gate with condition on output cannot have condition or gate on inputs");
//                                    break;
//                            }
//                            default: {
//                                if(precedingTypes.contains("action") || precedingTypes.contains(CecaDiagramConstants.CAUSE_ACTION_ROOT) ||precedingTypes.contains("and") || precedingTypes.contains("or")  )
//                                    errorList.add("OR gate with action on output cannot have action or gate on inputs");
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        }
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
            case CecaDiagramConstants.CAUSE_ACTION_ROOT: {
                //has to have at least 1 output (input is blocked by system) and the following tile must be action
                Optional<String> result = Optional.ofNullable(checkForInitialState(node));
                //TODO: check output
                result.ifPresent(errorList::add);
                break;
            }
            case "condition": {
                List<String> errors = checkForCondition(node);
                errorList.addAll(errors);
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
            default: {
                //System.out.println("Default case in coherency checker");
            }
        }

        return errorList;
    }

    private List<String> checkForCondition(GNode node) {
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
                        errorList.add("Condition node can only follow a disadvantage node, currently one of the inputs is connected to a " + sourceNodeSubtype + " node");
                    }
                    switch (sourceNodeSubtype) {
                        case CecaDiagramConstants.CONDITION: {
                            errorList.add("Condition node canot be connected to another condition node");
                            break;
                        }
                        case "and":
                        case "or":
                        case CecaDiagramConstants.CAUSE_ACTION_ROOT:
                        case CecaDiagramConstants.TARGET_DISADVANTAGE:
                        case CecaDiagramConstants.ACTION: {
                            //System.out.println("good stuff");
                        }
                    }
                }
            } else {
                for (GConnection connection : connector.getConnections()) {
                    outputcondition = true;
                    String targetNodeSubtype = NodeTraversalUtils.getTargetNode(connection).getSubtype();
                    //TODO: these cannot actually be null - you can use equals
                    if (targetNodeSubtype.equals(CecaDiagramConstants.CONDITION)) {
                        errorList.add("Condition cannot lead to another condition node\n");
                    }
                }
            }
        }
        if (!inputCondition) {
            errorList.add("Condition must have at least one input");
        }
        if (!outputcondition) {
            errorList.add("Condition must have at least one output");
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
                        errors.add("Intermediate Disadvantage can only follow  an action node, currently one of the connections leads to a " + sourceNodeSubtype + " node");
                    }
                }
            } else {
                for (GConnection connection : connector.getConnections()) {
                    outputExists = true;
                    String targetNodeSubtype = NodeTraversalUtils.getTargetNode(connection).getSubtype();
                    //TODO: these cannot actually be null - you can use equals
                    if (!targetNodeSubtype.equals("action")) {
                        errors.add("Intermediate Disadvantage can only lead to an action node, , currently one of the connections leads to a" + targetNodeSubtype + " node");
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
                if (Objects.equals(sourceNodeSubtype, CecaDiagramConstants.CONDITION) || Objects.equals(sourceNodeSubtype, "and") || Objects.equals(sourceNodeSubtype, "or")) {
                    return null;
                } else {
                    return "Target Disadvantage cannot be led to by an " + sourceNodeSubtype;
                }
            }
        }
        return "Target Disadvantage must have at least one input";
    }

    private String checkForInitialState(GNode node) {
        //System.out.println("check initial state");
        for (GConnector connector : node.getConnectors()) {
            //TODO: check for null pointer error
            //System.out.println("check connector " + connector + " in initial state");
            for (GConnection connection : connector.getConnections()) {
                //System.out.println("check connection " + connection + " in initial state");
                String targetNodeSubtype = NodeTraversalUtils.getTargetNode(connection).getSubtype();
                //TODO: these cannot actually be null - you can use equals
                if (Objects.equals(targetNodeSubtype, CecaDiagramConstants.CONDITION) || Objects.equals(targetNodeSubtype, "or")) {
                    return null;
                }
            }
        }
        return "Initial state must have an output leading to a condition or an OR gate";
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
                    //System.out.println("source for node " + node + " has subtype " + sourceNodeSubtype);
                    switch (sourceNodeSubtype) {
                        case CecaDiagramConstants.ACTION:
                        case CecaDiagramConstants.CAUSE_ACTION_ROOT: {
                            errorList.add("Action node can only follow a condition node or a gate. Currently one of the inputs is connected to a " + sourceNodeSubtype + " node");
                            break;
                        }
                        case "and":
                        case CecaDiagramConstants.CONDITION:
                        case "or": {
                            break;
                        }
                    }
                }
            } else {
                for (GConnection connection : connector.getConnections()) {
                    outputcondition = true;
                    String targetNodeSubtype = NodeTraversalUtils.getTargetNode(connection).getSubtype();
                    switch (targetNodeSubtype) {
                        case CecaDiagramConstants.ACTION:
                        case CecaDiagramConstants.TARGET_DISADVANTAGE: {
                            errorList.add("action node can only lead to a condition node or an OR gate. Currently one of the outputs is connected to a " + targetNodeSubtype + " node");
                            break;
                        }
                        case "and":
                        case CecaDiagramConstants.CONDITION:
                        case "or": {
                            break;
                        }
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

