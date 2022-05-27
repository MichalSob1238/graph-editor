package de.tesis.dynaware.grapheditor.demo.customskins;

import de.tesis.dynaware.grapheditor.Commands;
import de.tesis.dynaware.grapheditor.GraphEditor;
import de.tesis.dynaware.grapheditor.GraphEditorContainer;
import de.tesis.dynaware.grapheditor.core.skins.defaults.utils.DefaultConnectorTypes;
import de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram.CecaDiagramConnectorSkin;
import de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram.CecaDiagramConstants;
import de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram.CecaDiagramNodeSkin;
import de.tesis.dynaware.grapheditor.demo.customskins.ceca.diagram.CecaDiagramTailSkin;
import de.tesis.dynaware.grapheditor.demo.customskins.titled.TitledSkinConstants;
import de.tesis.dynaware.grapheditor.demo.customskins.tree.TreeSkinConstants;
import de.tesis.dynaware.grapheditor.model.GConnector;
import de.tesis.dynaware.grapheditor.model.GNode;
import de.tesis.dynaware.grapheditor.model.GraphFactory;
import javafx.geometry.Side;

import java.util.List;
import java.util.OptionalInt;

public class CecaDiagramSkinController implements SkinController{

    private final GraphEditor graphEditor;
    private final GraphEditorContainer graphEditorContainer;

    public CecaDiagramSkinController(final GraphEditor graphEditor, final GraphEditorContainer graphEditorContainer) {
        this.graphEditor = graphEditor;
        this.graphEditorContainer = graphEditorContainer;

        graphEditor.setNodeSkin(CecaDiagramConstants.DIAGRAM_NODE, CecaDiagramNodeSkin.class);
        graphEditor.setConnectorSkin(CecaDiagramConstants.DIAGRAM_INPUT_CONNECTOR, CecaDiagramConnectorSkin.class);
        graphEditor.setConnectorSkin(CecaDiagramConstants.DIAGRAM_OUTPUT_CONNECTOR, CecaDiagramConnectorSkin.class);
        graphEditor.setTailSkin(CecaDiagramConstants.DIAGRAM_INPUT_CONNECTOR, CecaDiagramTailSkin.class);
        graphEditor.setTailSkin(CecaDiagramConstants.DIAGRAM_OUTPUT_CONNECTOR, CecaDiagramTailSkin.class);
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

        node.setType(CecaDiagramConstants.DIAGRAM_NODE);
        node.setDescription("DESCRIPTION!");

        Commands.addNode(graphEditor.getModel(), node);
    }

    @Override
    public void addConnector(Side position, boolean input) {

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

    private String allocateNewId() {

        final List<GNode> nodes = graphEditor.getModel().getNodes();
        final OptionalInt max = nodes.stream().mapToInt(node -> Integer.parseInt(node.getId())).max();

        if (max.isPresent()) {
            return Integer.toString(max.getAsInt() + 1);
        } else {
            return "1";
        }
    }
}
