package suite;

import cause.effect.chain.editor.controller.CauseEffectChainEditorController;
import cause.effect.chain.editor.model.skins.CauseActionModel.CecaDiagramNodeSkin;
import de.tesis.dynaware.grapheditor.core.connections.ConnectionCommands;
import de.tesis.dynaware.grapheditor.demo.GraphEditorPersistence;
import de.tesis.dynaware.grapheditor.model.GConnection;
import de.tesis.dynaware.grapheditor.model.GModel;
import de.tesis.dynaware.grapheditor.model.GNode;
import javafx.application.Platform;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class CorrectnessTestSuite extends BaseTest{
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
    public void basicCorrectnessTestCA() throws InterruptedException {
        loadTest("src/test/resources/correctnessTestBasic.graph");
        waitForRunLater();
        GModel model = CauseEffectChainEditorController.instance.getModel().getGraphEditor().getModel();
        GConnection connection = CauseEffectChainEditorController.instance.getModel().getGraphEditor().getModel().getConnections().get(1);
        Platform.runLater(
                () -> {
                    ConnectionCommands.removeConnection(model, connection);
                    CauseEffectChainEditorController.instance.coherencyChecker.getNotified(connection);
                }
        );
        waitForRunLater();


        List<GNode> invalidNodes = model.getNodes().stream().filter(nod -> Objects.equals(nod.getId(), "2") || Objects.equals(nod.getId(), "3")).collect(Collectors.toList());
        System.out.println(invalidNodes);
        List<CecaDiagramNodeSkin> skins = invalidNodes.stream().map(gnode ->(CecaDiagramNodeSkin) CauseEffectChainEditorController.instance.getModel().getGraphEditor().getSkinLookup().lookupNode(gnode))
                .collect(Collectors.toList());
        sleep(100);
        Assert.assertEquals(skins.size(), 2);
        Assert.assertEquals(skins.get(0).isCorrect, false);
        Assert.assertEquals(skins.get(1).isCorrect, false);
    }
}
