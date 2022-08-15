package suite;

import cause.effect.chain.editor.controller.CauseEffectChainEditorController;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import org.junit.Assert;
import org.junit.Test;

public class BasicConnectionTestSuite extends BaseTest{
    public void addNodeTest(String type) throws InterruptedException {

        press(KeyCode.CONTROL, KeyCode.N);
        release(KeyCode.CONTROL, KeyCode.N);

        Node dialogPane = lookup(".dialog-pane").query();
        clickOn("root-cause");
        clickOn(type);

        press(KeyCode.ENTER);
        release(KeyCode.ENTER);
    }

    @Test
    public void addConnection() throws InterruptedException {
        addNodeTest("root-cause");
        addNodeTest("condition");
    }
}
