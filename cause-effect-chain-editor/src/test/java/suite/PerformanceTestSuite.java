package suite;

import cause.effect.chain.editor.controller.CauseEffectChainEditorController;
import cause.effect.chain.editor.model.skins.CauseActionModel.CecaDiagramConstants;
import cause.effect.chain.editor.utils.NodeTraversalUtils;
import de.tesis.dynaware.grapheditor.model.GNode;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.api.FxRobot;

public class PerformanceTestSuite extends BaseTest {

    public void addNodes(ResultStore result, int n) {
        CauseEffectChainEditorController.instance.clearAll();
        GNode prevCondition = null;
        for (int i = 0; i < n; i = i + 2){
            long startTimeFor2Nodes = System.currentTimeMillis();
            GNode condition = CauseEffectChainEditorController.instance.getModel().addCauseActionNode(100 + 40*i,100,"condition", CecaDiagramConstants.CONDITION);
            long endTimeFor2Nodes = System.currentTimeMillis();
            long durationFor2Nodes = (endTimeFor2Nodes - startTimeFor2Nodes);
            result.addElementTime(i,durationFor2Nodes);

            startTimeFor2Nodes = System.currentTimeMillis();
            GNode action = CauseEffectChainEditorController.instance.getModel().addCauseActionNode(120 + 40*i,100,"action", CecaDiagramConstants.ACTION);
            endTimeFor2Nodes = System.currentTimeMillis();
            durationFor2Nodes = (endTimeFor2Nodes - startTimeFor2Nodes);
            result.addElementTime(i+1,durationFor2Nodes);

            long startTimeForConnection = System.currentTimeMillis();
            CauseEffectChainEditorController.instance.getModel().addConditionActionConnection(NodeTraversalUtils.getNodeInput(condition), NodeTraversalUtils.getNodeOutput(action));
            long endTimeForConnection = System.currentTimeMillis();
            long durationForConnection = endTimeForConnection - startTimeForConnection;
            result.addConnectionTime(i, durationForConnection);

            if (prevCondition != null) {
                startTimeForConnection = System.currentTimeMillis();
                CauseEffectChainEditorController.instance.getModel().addConditionActionConnection(NodeTraversalUtils.getNodeInput(prevCondition), NodeTraversalUtils.getNodeOutput(condition));
                endTimeForConnection = System.currentTimeMillis();
                durationForConnection = endTimeForConnection - startTimeForConnection;
                result.addConnectionTime(i+1, durationForConnection);
            }
            prevCondition = action;
        }
    }

    @Test
    public void testNodeAddDelay() throws InterruptedException {
        ResultStore results = new ResultStore();
        FxRobot x = new FxRobot();
        x.interact(new Runnable() {
            @Override
            public void run() {
                addNodes(results, 250);
                results.printElementTimes();
                results.printConnectionTimes();
                addNodes(results, 250);
                results.printElementTimes();
                results.printConnectionTimes();
                addNodes(results, 250);
                results.printElementTimes();
                results.printConnectionTimes();
                addNodes(results, 250);
                results.printElementTimes();
                results.printConnectionTimes();
                addNodes(results, 250);
                results.printElementTimes();
                results.printConnectionTimes();
                addNodes(results, 250);
                results.printElementTimes();
                results.printConnectionTimes();
                addNodes(results, 250);
                results.printElementTimes();
                results.printConnectionTimes();
                addNodes(results, 250);
            }
        });
            results.printElementTimes();
            results.printConnectionTimes();
        Assert.assertEquals(CauseEffectChainEditorController.instance.getModel().getGraphEditor().getModel().getNodes().size(), 250);
    }
}

