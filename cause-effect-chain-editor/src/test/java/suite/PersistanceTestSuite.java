package suite;

import cause.effect.chain.editor.controller.CauseEffectChainEditorController;
import de.tesis.dynaware.grapheditor.demo.GraphEditorPersistence;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.Semaphore;

public class PersistanceTestSuite extends BaseTest {
    GraphEditorPersistence graphEditorPersistence = new GraphEditorPersistence();

    public void addNodeTest(String last, String type) throws InterruptedException {
        press(KeyCode.CONTROL, KeyCode.N);
        release(KeyCode.CONTROL, KeyCode.N);

        Node dialogPane = lookup(".dialog-pane").query();
        clickOn(last);
        clickOn(type);

        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

    }

    public void addNodeTest(String type) throws InterruptedException {
        addNodeTest("root-cause", type);
    }
    public static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(semaphore::release);
        semaphore.acquire();
    }
    @Test
    public void saveToFile() throws InterruptedException {
        addNodeTest("action");
        addNodeTest("action", "root-cause");
        File file = new File("src\\test\\resources\\suite\\testSaveCreated.graph");
        Platform.runLater(
                () -> {
                    graphEditorPersistence.saveModel(file, CauseEffectChainEditorController.instance.getModel().getGraphEditor().getModel());
                    CauseEffectChainEditorController.instance.clearAll();
                }
        );
        waitForRunLater();

        Assert.assertEquals(0, CauseEffectChainEditorController.instance.getModel().getGraphEditor().getModel().getNodes().size());

        Platform.runLater(
                () -> graphEditorPersistence.loadFromFile(CauseEffectChainEditorController.instance.getModel().getGraphEditor(), file)
        );
        waitForRunLater();
        Assert.assertEquals(2, CauseEffectChainEditorController.instance.getModel().getGraphEditor().getModel().getNodes().size());
        file.delete();
    }


}
