package node;

import cause.effect.chain.editor.controller.CauseEffectChainEditorController;
import cause.effect.chain.editor.model.skins.StateActionModel.CecaDiagramConstants;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.junit.Assert;
import org.junit.Test;
import suite.BaseTest;

public class BasicNodeTestSuite extends BaseTest {

    public void addNodeTest(String type) throws InterruptedException {

        press(KeyCode.CONTROL, KeyCode.N);
        release(KeyCode.CONTROL, KeyCode.N);

        Node dialogPane = lookup(".dialog-pane").query();
        clickOn("root-cause");
        clickOn(type);

        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

        Assert.assertEquals(CauseEffectChainEditorController.instance.getModel().getGraphEditor().getModel().getNodes().size(), 1);

    }

    @Test
    public void addRootCauseTest() throws InterruptedException {
        addNodeTest("root-cause");
        Assert.assertEquals(CauseEffectChainEditorController.instance.getModel().getGraphEditor().getModel().getNodes().size(), 1);
        Assert.assertEquals(CauseEffectChainEditorController.instance.getModel().getGraphEditor().getModel().getNodes().get(0).getType(), CecaDiagramConstants.CECA_NODE);
    }

    @Test
    public void addActionTest() throws InterruptedException {
        addNodeTest("action");
        Assert.assertEquals(CauseEffectChainEditorController.instance.getModel().getGraphEditor().getModel().getNodes().size(), 1);
        Assert.assertEquals(CauseEffectChainEditorController.instance.getModel().getGraphEditor().getModel().getNodes().get(0).getType(), CecaDiagramConstants.CECA_NODE);
    }

    @Test
    public void addConditionTest() throws InterruptedException {
        addNodeTest("condition");
        Assert.assertEquals(CauseEffectChainEditorController.instance.getModel().getGraphEditor().getModel().getNodes().size(), 1);
        Assert.assertEquals(CauseEffectChainEditorController.instance.getModel().getGraphEditor().getModel().getNodes().get(0).getType(), CecaDiagramConstants.CECA_NODE);
    }

    @Test
    public void addTargetDisadvantageTest() throws InterruptedException {
        addNodeTest("target-disadvantage");
        Assert.assertEquals(CauseEffectChainEditorController.instance.getModel().getGraphEditor().getModel().getNodes().size(), 1);
        Assert.assertEquals(CauseEffectChainEditorController.instance.getModel().getGraphEditor().getModel().getNodes().get(0).getSubtype(), CecaDiagramConstants.TARGET_DISADVANTAGE);
    }

}
