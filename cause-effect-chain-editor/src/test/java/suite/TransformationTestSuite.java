package suite;

import cause.effect.chain.editor.controller.CauseEffectChainEditorController;
import de.tesis.dynaware.grapheditor.demo.GraphEditorPersistence;
import javafx.application.Platform;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.Semaphore;

public class TransformationTestSuite extends BaseTest {

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
    public void testOfTransfromIntoStateMachine() throws InterruptedException {
        loadTest("src\\test\\resources\\suite\\testConvert2.graph");

        Platform.runLater(
                () -> CauseEffectChainEditorController.instance.transformIntoStateMachine()
        );
        waitForRunLater();
        Assert.assertEquals(CauseEffectChainEditorController.instance.getModel().getGraphEditor().getModel().getNodes().size(), 7);
    }


}
