package suite;

import cause.effect.chain.editor.controller.CauseEffectChainEditorController;
import de.tesis.dynaware.grapheditor.GConnectorSkin;
import de.tesis.dynaware.grapheditor.SkinLookup;
import de.tesis.dynaware.grapheditor.demo.GraphEditorPersistence;
import de.tesis.dynaware.grapheditor.model.GConnector;
import de.tesis.dynaware.grapheditor.model.GNode;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.Semaphore;

public class BasicConnectionTestSuite extends BaseTest{

    GraphEditorPersistence graphEditorPersistence = new GraphEditorPersistence();

    public void loadTest(String testfile) throws InterruptedException {
        File file = new File(testfile);
        Platform.runLater(
                () -> graphEditorPersistence.loadFromFile(CauseEffectChainEditorController.instance.getModel().getGraphEditor(), file)
        );

    }

    public static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(semaphore::release);
        semaphore.acquire();
    }



    @Test
    public void addConnection() throws InterruptedException {
        loadTest("src\\test\\resources\\connectionTest1.graph");
        waitForRunLater();
        GConnector outputConnector = CauseEffectChainEditorController.instance.getModel().getGraphEditor().getModel().getNodes().get(0).getConnectors().get(0);
        GConnectorSkin outputConnectorNode =  CauseEffectChainEditorController.instance.getModel().getGraphEditor().getSkinLookup().lookupConnector(outputConnector);

        GConnector inputConnector = CauseEffectChainEditorController.instance.getModel().getGraphEditor().getModel().getNodes().get(1).getConnectors().get(0);
        GConnectorSkin inputConnectorNode =  CauseEffectChainEditorController.instance.getModel().getGraphEditor().getSkinLookup().lookupConnector(inputConnector);

        drag(outputConnectorNode.getRoot(), MouseButton.PRIMARY). dropTo(inputConnectorNode.getRoot());

        Assert.assertEquals(1, CauseEffectChainEditorController.instance.getModel().getGraphEditor().getModel().getConnections().size());
    }
}
