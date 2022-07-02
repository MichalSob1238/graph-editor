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

import java.util.Objects;

public class CoherencyChecker {

    private final GraphEditor graphEditor;
    private final SkinLookup skinLookup;

    public CoherencyChecker(GraphEditor graphEditor) {
        this.graphEditor = graphEditor;
        this.skinLookup = graphEditor.getSkinLookup();
    }

    public void getNotified(GConnection connection) {
        updateCorrectnesStatus(connection);
    }

    private void updateCorrectnesStatus(GConnection connection) {
        GNode sourceNode = (GNode) connection.getSource().getParent();
        GNode targetNode = (GNode) connection.getTarget().getParent();
        GNodeSkin sourceNodeSkin = skinLookup.lookupNode(sourceNode);
        GNodeSkin targetNodeSkin = skinLookup.lookupNode(targetNode);

        switch (sourceNode.getType()) {
            case CecaDiagramConstants.CECA_NODE:
                sourceNodeSkin.updateStatus(checkForDiagramNode(sourceNode));
                break;
            case StateMachineConstants.STATE_MACHINE_NODE:
                break;
            case CecaDiagramConstants.GATE_NODE:
                break;
        }
    }


    private boolean checkForDiagramNode(GNode node) {
        boolean isCorrect = false;
        switch (node.getSubtype()) {
            case "initial-state": {
                //has to have at least 1 output (input is blocked by system) and the following tile must be action
                for (GConnector connector : node.getConnectors()) {
                    //TODO: check for null pointer error
                    for (GConnection connection : connector.getConnections()) {
                        String targetNodeSubtype = NodeTraversalUtils.getTargetNode(connection).getSubtype();
                        //TODO: these cannot actually be null - you can use equals
                        if (Objects.equals(targetNodeSubtype, "action") || Objects.equals(targetNodeSubtype, "AND") || Objects.equals(targetNodeSubtype, "OR")) {
                            isCorrect = true;
                        }
                    }
                }
                break;
            }
            case "action": {
                //Has to have at least one connected input leading to "initial-state" or "intermediate-dissadvantage" and at least one output leading to not-action
                boolean inputCondition = false;
                boolean outputcondition = false;
                for (GConnector connector : node.getConnectors()) {
                    if (NodeTraversalUtils.isInput(connector)) {
                        //TODO: refactor this to a function with a list of conditions
                        for (GConnection connection : connector.getConnections()) {
                            String sourceNodeSubtype = NodeTraversalUtils.getSourceNode(connection).getSubtype();
                            //TODO: these cannot actually be null - you can use equals - OR do contains("disadvantage")
                            if (Objects.equals(sourceNodeSubtype, "initial-disadvantage") || Objects.equals(sourceNodeSubtype, "intermediate-disadvantage")) {
                                inputCondition = true;
                            }
                        }
                    } else {
                        for (GConnection connection : connector.getConnections()) {
                            String targetNodeSubtype = NodeTraversalUtils.getTargetNode(connection).getSubtype();
                            //TODO: these cannot actually be null - you can use equals
                            if (!targetNodeSubtype.equals("action")) {
                                outputcondition = true;
                            }
                        }
                    }
                }
                isCorrect = (inputCondition && outputcondition);
                break;
            }
            case "target-disadvantage": {
                //needs one input from an action or a gate
                for (GConnector connector : node.getConnectors()) {
                    //TODO: check for null pointer error
                    for (GConnection connection : connector.getConnections()) {
                        String sourceNodeSubtype = NodeTraversalUtils.getSourceNode(connection).getSubtype();
                        //TODO: these cannot actually be null - you can use equals
                        if (Objects.equals(sourceNodeSubtype, "action") || Objects.equals(sourceNodeSubtype, "AND") || Objects.equals(sourceNodeSubtype, "OR")) {
                            isCorrect = true;
                        }
                    }
                }
                break;
            }
            case "intermediate-disadvantage": {
                //needs at least one input and one output, leading to actions each
                boolean inputCondition = false;
                boolean outputcondition = false;
                for (GConnector connector : node.getConnectors()) {
                    if (NodeTraversalUtils.isInput(connector)) {
                        //TODO: refactor this to a function with a list of conditions
                        for (GConnection connection : connector.getConnections()) {
                            String sourceNodeSubtype = NodeTraversalUtils.getSourceNode(connection).getSubtype();
                            //TODO: these cannot actually be null - you can use equals - OR do contains("disadvantage")
                            if (Objects.equals(sourceNodeSubtype, "action")) {
                                inputCondition = true;
                            }
                        }
                    } else {
                        for (GConnection connection : connector.getConnections()) {
                            String targetNodeSubtype = NodeTraversalUtils.getTargetNode(connection).getSubtype();
                            //TODO: these cannot actually be null - you can use equals
                            if (targetNodeSubtype.equals("action")) {
                                outputcondition = true;
                            }
                        }
                    }
                }
                isCorrect = (inputCondition && outputcondition);
                break;
            }
        }

        return isCorrect;
    }

}
