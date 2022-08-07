package de.tesis.dynaware.grapheditor.demo.customskins;

import de.tesis.dynaware.grapheditor.core.skins.defaults.utils.DefaultConnectorTypes;
import de.tesis.dynaware.grapheditor.model.GConnection;
import de.tesis.dynaware.grapheditor.model.GConnector;
import de.tesis.dynaware.grapheditor.model.GNode;

public class NodeTraversalUtils {
    public static GNode getTargetNode(GConnection connection) {
        return getTargetNode(connection.getSource(), connection.getTarget());
    }

    public static GNode getSourceNode(GConnection connection) {
        return getSourceNode(connection.getSource(), connection.getTarget());
    }

    public static GNode getTargetNode(GConnector connector) {
        return (GNode) connector.getConnections().get(0).getTarget().getParent();
    }

    public static GNode getTargetNode(GConnector source, GConnector target) {
        GConnector input = isInput(source) ? source : target;
        return (GNode) input.getParent();
    }

    public static GNode getSourceNode(GConnector source, GConnector target) {
        GConnector output = isInput(source) ? target : source;
        return (GNode) output.getParent();
    }

    public static boolean isInput(GConnector connector) {
        return DefaultConnectorTypes.isInput(connector.getType());
    }
}
