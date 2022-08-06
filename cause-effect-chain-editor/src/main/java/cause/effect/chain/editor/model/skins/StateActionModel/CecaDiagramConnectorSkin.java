package cause.effect.chain.editor.model.skins.StateActionModel;

import de.tesis.dynaware.grapheditor.GConnectorSkin;
import de.tesis.dynaware.grapheditor.GConnectorStyle;
import de.tesis.dynaware.grapheditor.model.GConnector;
import javafx.scene.Node;

public class CecaDiagramConnectorSkin extends GConnectorSkin {
    /**
     * Creates a new {@link GConnectorSkin}.
     *
     * @param connector the {@link GConnector} represented by the skin
     */
    public CecaDiagramConnectorSkin(GConnector connector) {
        super(connector);
    }

    @Override
    public double getWidth() {
        return 10;
    }

    @Override
    public double getHeight() {
        return 10;
    }

    @Override
    public void applyStyle(GConnectorStyle style) {

    }

    @Override
    public Node getRoot() {
        return null;
    }
}
